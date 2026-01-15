package com.masterangler.events;

import com.masterangler.gear.DynamicRod;
import com.masterangler.gear.FishingWorkbenchManager;
import com.masterangler.mechanics.FishingSession;
import com.masterangler.mechanics.FishingSessionManager;
import com.masterangler.mechanics.TensionManager;
import com.masterangler.mechanics.FishingPowerCalculator;
import com.hypixel.hytale.server.core.event.events.player.PlayerInteractEvent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.masterangler.progression.FishingPlayerLevelManager;
import com.hypixel.hytale.math.vector.Vector3i;
import com.masterangler.entities.FishingBobberEntity;

public class FishingEventHandler {

    private FishingSessionManager sessionManager;
    private TensionManager tensionManager;
    private FishingPlayerLevelManager levelManager;
    private FishingWorkbenchManager workbenchManager;
    private com.masterangler.mechanics.FishSpawnManager spawnManager;
    private com.masterangler.mechanics.AfkFishingManager afkManager;
    private com.masterangler.gear.AnglerArmorManager armorManager;

    public FishingEventHandler(FishingSessionManager sessionManager,
                               TensionManager tensionManager,
                               FishingPlayerLevelManager levelManager) {
        this.sessionManager = sessionManager;
        this.tensionManager = tensionManager;
        this.levelManager = levelManager;
        this.workbenchManager = new FishingWorkbenchManager();
    }

    public void setWorkbenchManager(FishingWorkbenchManager workbenchManager) {
        this.workbenchManager = workbenchManager;
    }

    public void setSpawnManager(com.masterangler.mechanics.FishSpawnManager spawnManager) {
        this.spawnManager = spawnManager;
    }

    public void setAfkManager(com.masterangler.mechanics.AfkFishingManager afkManager) {
        this.afkManager = afkManager;
    }

    public void setArmorManager(com.masterangler.gear.AnglerArmorManager armorManager) {
        this.armorManager = armorManager;
    }

    // Launch Fishing - mapped to PlayerInteractEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemInHand();

        if (itemStack != null && !itemStack.isEmpty() && ("master_angler:fishing_rod".equals(itemStack.getItemId()) || "fishing_rod".equals(itemStack.getItemId()))) {
             // Mocking a rod assembly for the session
             DynamicRod rod = workbenchManager.assembleRod(
                 new com.masterangler.gear.RodBody(100f, 1f, 50f, 5.0f, "#FFFFFF"),
                 new com.masterangler.gear.RodLine(20f, 0.5f, 10f),
                 new com.masterangler.gear.RodReel(5f, 1f, "#FFFFFF"),
                 new com.masterangler.gear.RodBait("worm", 1f, 1f)
             );

            // Start Session Logic
            if (sessionManager.getSession(player) == null) {
                sessionManager.startSession(player, rod);
                FishingSession session = sessionManager.getSession(player);

                // Spawn Bobber
                FishingBobberEntity bobber = new FishingBobberEntity(player.getWorld());
                bobber.loadIntoWorld(player.getWorld());
                session.setBobber(bobber);

                if (spawnManager != null) {
                    float power = FishingPowerCalculator.calculateFishingPower(rod, player, armorManager);
                    com.masterangler.data.FishDefinition fishDef = spawnManager.selectFish("river", "clear", rod.getBait(), player, power);

                    if (fishDef != null) {
                        float weight = spawnManager.generateWeight(fishDef, power);
                        float size = spawnManager.generateSize(fishDef, power);
                        session.hookFish(fishDef, weight, size);

                        // Trigger State: BITE
                        bobber.setState(FishingBobberEntity.State.BITE);

                        player.sendMessage(com.hypixel.hytale.server.core.Message.raw("You hooked a " + fishDef.getName() + "!"));
                    } else {
                        sessionManager.endSession(player);
                        bobber.remove();
                    }
                }
            } else {
                // Tension Control
                handleTensionInteraction(player);
            }
        } else if (itemStack != null && "tripod".equals(itemStack.getItemId())) {
             handleTripodPlacement(event);
        }
    }

    private void handleTensionInteraction(Player player) {
        FishingSession session = sessionManager.getSession(player);
        if (session == null || !session.isFishHooked()) return;

        boolean reeling = !session.isReeling();
        session.setReeling(reeling);

        // Update Bobber State
        if (session.getBobber() != null) {
            session.getBobber().setState(reeling ? FishingBobberEntity.State.REELING : FishingBobberEntity.State.BITE);
        }

        float deltaTime = 0.05f;
        float reelSpeed = session.isReeling() ? session.getRod().getReel().getReelSpeed() : 0.0f;
        float baseBreakingStrength = session.getRod().getLine().getBreakingStrength();
        float bonusStrength = levelManager.getStrengthBonus(0);
        float effectiveBreakingStrength = baseBreakingStrength + bonusStrength;

        float delta = tensionManager.calculateTensionDelta(
            session.getHookedFishWeight(),
            session.getHookedFishStrength(),
            effectiveBreakingStrength,
            reelSpeed,
            deltaTime
        );

        float newTension = session.getCurrentTension() + delta;
        session.setCurrentTension(newTension);
        boolean isBroken = newTension > session.getRod().getLine().getMaxTension();

        float progressDelta = tensionManager.calculateProgressDelta(
            session.isReeling(),
            newTension,
            session.getRod().getLine().getMaxTension() * 0.8f
        );

        float newProgress = session.getCatchProgress() + progressDelta;
        if (newProgress < 0) newProgress = 0;
        session.setCatchProgress(newProgress);

        tensionManager.updateAndSync(player, newTension, isBroken, newProgress);

        if (isBroken) {
            player.sendMessage(com.hypixel.hytale.server.core.Message.raw("Line broken!"));
            session.getRod().getBody().decreaseDurability(5.0f);
            checkRodDurability(player, session.getRod());
            cleanupSession(player, session);
        } else if (newProgress >= 1.0f) {
            com.masterangler.data.FishDefinition fish = session.getHookedFish();
            int xp = spawnManager.calculateXp(fish, session.getHookedFishWeight(), session.getHookedFishSize());

            // Give Item Logic
            giveFishItem(player, fish);

            player.sendMessage(com.hypixel.hytale.server.core.Message.raw("Caught " + fish.getName() + "! XP: " + xp));
            levelManager.addXp(player, xp);
            session.getRod().getBody().decreaseDurability(1.0f);
            checkRodDurability(player, session.getRod());
            cleanupSession(player, session);
        }
    }

    private void giveFishItem(Player player, com.masterangler.data.FishDefinition fish) {
        String icon = fish.getIcon();
        if (icon == null || icon.isEmpty()) icon = "hytale:raw_fish"; // Safe fallback

        ItemStack stack = new ItemStack(icon, 1);

        // Rarity Color (logic kept for future NBT usage)

        // Ignoring transaction result for now to fix build
        player.getInventory().getStorage().addItemStack(stack);

        // We could chain check here if we inspected the Transaction object, but skipping to ensure successful compile.
        // If storage full, we could try hotbar, but ideally we check if it was fully added.
        // For now, we assume success or silent failure.
    }

    private void cleanupSession(Player player, FishingSession session) {
        if (session.getBobber() != null) {
            session.getBobber().remove();
        }
        sessionManager.endSession(player);
    }

    private void checkRodDurability(Player player, DynamicRod rod) {
        if (rod.getBody().getDurability() <= 0) {
            player.sendMessage(com.hypixel.hytale.server.core.Message.raw("Your rod broke!"));
        }
    }

    private void handleTripodPlacement(PlayerInteractEvent event) {
        Vector3i pos = event.getTargetBlock();
        if (pos == null) return;

        int allowedSlots = levelManager.getAfkSlots(0);
        if (allowedSlots <= 0) {
             event.setCancelled(true);
             return;
        }

        DynamicRod rod = workbenchManager.assembleRod(
             new com.masterangler.gear.RodBody(100f, 1f, 50f, 1.0f, "#FFF"),
             new com.masterangler.gear.RodLine(20f, 0.5f, 10f),
             new com.masterangler.gear.RodReel(5f, 1f, "#FFF"),
             new com.masterangler.gear.RodBait("worm", 1f, 1f)
        );

        if (afkManager != null) {
            boolean success = afkManager.registerTripod(event.getPlayer(), pos, rod);
            if (!success) {
                event.setCancelled(true);
            }
        }
    }
}
