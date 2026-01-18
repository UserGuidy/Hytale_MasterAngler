package com.masterangler.mechanics;

import com.masterangler.data.FishDefinition;
import com.masterangler.gear.DynamicRod;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.masterangler.progression.FishingPlayerLevelManager;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AfkFishingManager {

    private static class TripodEntry {
        Player owner;
        DynamicRod rod;
        long lastCheckTime;

        TripodEntry(Player owner, DynamicRod rod) {
            this.owner = owner;
            this.rod = rod;
            this.lastCheckTime = System.currentTimeMillis();
        }
    }

    private Map<Vector3i, TripodEntry> activeTripods = new ConcurrentHashMap<>();
    private FishSpawnManager spawnManager;
    private FishingPlayerLevelManager levelManager;
    private Random random = new Random();
    private static final long TICK_INTERVAL_MS = 1000; // Check every second
    private volatile boolean running = true;

    public AfkFishingManager(FishSpawnManager spawnManager, FishingPlayerLevelManager levelManager) {
        this.spawnManager = spawnManager;
        this.levelManager = levelManager;
    }

    public boolean registerTripod(Player player, Vector3i pos, DynamicRod rod) {
        if (activeTripods.containsKey(pos)) return false;

        activeTripods.put(pos, new TripodEntry(player, rod));
        // System.out.println("[AFK] Tripod placed at " + pos + " by " + player.getName());
        return true;
    }

    public void removeTripod(Vector3i pos) {
        activeTripods.remove(pos);
    }

    public void stop() {
        this.running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void tick() {
        if (!running) return;

        long now = System.currentTimeMillis();

        Iterator<Map.Entry<Vector3i, TripodEntry>> it = activeTripods.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Vector3i, TripodEntry> entry = it.next();
            TripodEntry tripod = entry.getValue();

            if (now - tripod.lastCheckTime >= TICK_INTERVAL_MS) {
                tripod.lastCheckTime = now;
                processTripod(tripod, entry.getKey());
            }
        }
    }

    private void processTripod(TripodEntry tripod, Vector3i pos) {
        // Simple logic:
        // 1. Roll for bite based on Bait Attractivity (simplified)
        // 2. Roll for catch based on Reel AFK Efficiency

        float biteChance = 0.1f * tripod.rod.getBait().getAttractivity(); // 10% base * attractivity
        if (random.nextFloat() < biteChance) {
            // Fish bit!
            float catchChance = 0.5f * tripod.rod.getReel().getAfkEfficiency(); // 50% base * efficiency

            if (random.nextFloat() < catchChance) {
                // SUCCESS
                // AFK Fishing Power: Uses Rod Body Power + Owner Armor (if available via Calculator)
                // Note: We use a static helper, need to ensure armor manager is accessible if we want armor bonuses.
                // For now, simpler approach: Just use Rod Body Power + simple armor lookup if we had the manager instance.
                // Since AfkFishingManager doesn't hold ArmorManager, we rely on Rod Body Power primarily.
                float power = tripod.rod.getBody().getFishingPower();

                FishDefinition fish = spawnManager.selectFish("river", "clear", tripod.rod.getBait(), tripod.owner, power);
                if (fish != null) {
                    float weight = spawnManager.generateWeight(fish, power);
                    float size = spawnManager.generateSize(fish, power);
                    int xp = spawnManager.calculateXp(fish, weight, size);

                    // System.out.println("[AFK] Tripod at " + pos + " caught " + fish.getName() + " (" + weight + "kg)");
                    tripod.owner.sendMessage(com.hypixel.hytale.server.core.Message.raw("[AFK] Catch: " + fish.getName() + " (" + weight + "kg)"));
                    levelManager.addXp(tripod.owner, xp / 2); // 50% XP for AFK

                    // Decrease durability
                    tripod.rod.getBody().decreaseDurability(1.0f);
                    if (tripod.rod.getBody().getDurability() <= 0) {
                        // System.out.println("[AFK] Rod at " + pos + " broke!");
                        tripod.owner.sendMessage(com.hypixel.hytale.server.core.Message.raw("[AFK] Rod broke!"));
                        // In real logic, drop items or destroy block
                        activeTripods.remove(pos);
                    }
                }
            }
        }
    }
}
