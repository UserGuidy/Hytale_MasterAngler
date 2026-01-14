package com.masterangler.events;

import com.masterangler.gear.DynamicRod;
import com.masterangler.gear.FishingWorkbenchManager;
import com.masterangler.mechanics.FishingSession;
import com.masterangler.mechanics.FishingSessionManager;
import com.masterangler.mechanics.TensionManager;
import com.masterangler.mock.ServerContext;
import com.masterangler.mock.events.PlayerInteractEvent;
import com.masterangler.mock.events.PlayerMouseButtonEvent;
import com.masterangler.mock.events.UseBlockEvent;
import com.masterangler.progression.FishingPlayerLevelManager;

public class FishingEventHandler {

    private FishingSessionManager sessionManager;
    private TensionManager tensionManager;
    private FishingPlayerLevelManager levelManager;
    private ServerContext serverContext;
    private FishingWorkbenchManager workbenchManager; // Needed to get rod stats from item (mock)

    public FishingEventHandler(FishingSessionManager sessionManager,
                               TensionManager tensionManager,
                               FishingPlayerLevelManager levelManager,
                               ServerContext serverContext) {
        this.sessionManager = sessionManager;
        this.tensionManager = tensionManager;
        this.levelManager = levelManager;
        this.serverContext = serverContext;
        this.workbenchManager = new FishingWorkbenchManager(); // In real app, dependency injection
    }

    // Launch Fishing
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() != null && event.getItem().getItem().hasTag("fishing_rod")) {
            // In a real scenario, we would retrieve the DynamicRod data attached to the ItemStack.
            // For this mock/phase 1, we assume a default rod is being used.
             // Mocking a rod assembly for the session
             DynamicRod rod = workbenchManager.assembleRod(
                 new com.masterangler.gear.RodBody(100f, 1f, 50f),
                 new com.masterangler.gear.RodLine(20f, 0.5f, 10f),
                 new com.masterangler.gear.RodReel(5f, 1f),
                 new com.masterangler.gear.RodBait(1f, 1f)
             );

            if (sessionManager.getSession(event.getPlayer()) == null) {
                sessionManager.startSession(event.getPlayer(), rod);
                // Simulate hooking a fish immediately for testing Phase 1 mechanics
                FishingSession session = sessionManager.getSession(event.getPlayer());
                session.hookFish(5.0f, 10.0f); // 5kg fish, 10 strength
            }
        }
    }

    // Tension Control
    public void onPlayerMouseButton(PlayerMouseButtonEvent event) {
        FishingSession session = sessionManager.getSession(event.getPlayer());
        if (session == null || !session.isFishHooked()) return;

        // Button 1 is Right Click (Reel)
        if (event.getButton() == 1) {
            session.setReeling(event.isDown());
        }

        if (event.isDown()) {
             // Calculate physics frame (simulating a tick here for the event, ideally this is in a loop)
             float deltaTime = 0.05f; // 20 ticks per second assumption

             float reelSpeed = session.isReeling() ? session.getRod().getReel().getReelSpeed() : 0.0f;

             // Apply Level 10 Bonus: +1kg resistance (breaking strength)
             float baseBreakingStrength = session.getRod().getLine().getBreakingStrength();
             float bonusStrength = levelManager.getStrengthBonus(event.getPlayer().getLevel());
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

             // Sync
             tensionManager.updateAndSync(serverContext, event.getPlayer(), newTension, isBroken);

             if (isBroken) {
                 sessionManager.endSession(event.getPlayer());
             }
        }
    }

    // AFK Tripod Deployment
    public void onUseBlock(UseBlockEvent event) {
        if (event.getItem() != null && event.getItem().getItem().hasTag("tripod")) {
            int allowedSlots = levelManager.getAfkSlots(event.getPlayer().getLevel());
            // Mock check for existing tripods (always 0 here so it passes if slots > 0)
            int currentTripods = 0;

            if (currentTripods >= allowedSlots) {
                event.setCancelled(true);
                System.out.println("[FishingEventHandler] Tripod placement cancelled. Max slots reached for level " + event.getPlayer().getLevel());
            } else {
                System.out.println("[FishingEventHandler] Tripod placed.");
            }
        }
    }
}
