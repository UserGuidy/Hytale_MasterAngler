package com.masterangler.mechanics;

import com.masterangler.data.FishDefinition;
import com.masterangler.gear.DynamicRod;
import com.masterangler.mock.BlockPosition;
import com.masterangler.mock.Player;
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

    private Map<BlockPosition, TripodEntry> activeTripods = new ConcurrentHashMap<>();
    private FishSpawnManager spawnManager;
    private FishingPlayerLevelManager levelManager;
    private Random random = new Random();
    private static final long TICK_INTERVAL_MS = 1000; // Check every second
    private volatile boolean running = true;

    public AfkFishingManager(FishSpawnManager spawnManager, FishingPlayerLevelManager levelManager) {
        this.spawnManager = spawnManager;
        this.levelManager = levelManager;
    }

    public boolean registerTripod(Player player, BlockPosition pos, DynamicRod rod) {
        if (activeTripods.containsKey(pos)) return false;

        activeTripods.put(pos, new TripodEntry(player, rod));
        System.out.println("[AFK] Tripod placed at " + pos + " by " + player.getName());
        return true;
    }

    public void removeTripod(BlockPosition pos) {
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

        Iterator<Map.Entry<BlockPosition, TripodEntry>> it = activeTripods.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<BlockPosition, TripodEntry> entry = it.next();
            TripodEntry tripod = entry.getValue();

            if (now - tripod.lastCheckTime >= TICK_INTERVAL_MS) {
                tripod.lastCheckTime = now;
                processTripod(tripod, entry.getKey());
            }
        }
    }

    private void processTripod(TripodEntry tripod, BlockPosition pos) {
        // Simple logic:
        // 1. Roll for bite based on Bait Attractivity (simplified)
        // 2. Roll for catch based on Reel AFK Efficiency

        float biteChance = 0.1f * tripod.rod.getBait().getAttractivity(); // 10% base * attractivity
        if (random.nextFloat() < biteChance) {
            // Fish bit!
            float catchChance = 0.5f * tripod.rod.getReel().getAfkEfficiency(); // 50% base * efficiency

            if (random.nextFloat() < catchChance) {
                // SUCCESS
                FishDefinition fish = spawnManager.selectFish("river", "clear", tripod.rod.getBait(), tripod.owner);
                if (fish != null) {
                    System.out.println("[AFK] Tripod at " + pos + " caught " + fish.getName() + "!");
                    levelManager.addXp(tripod.owner, fish.getXpReward() / 2); // 50% XP for AFK

                    // Decrease durability
                    tripod.rod.getBody().decreaseDurability(1.0f);
                    if (tripod.rod.getBody().getDurability() <= 0) {
                        System.out.println("[AFK] Rod at " + pos + " broke!");
                        // In real logic, drop items or destroy block
                        activeTripods.remove(pos);
                    }
                }
            }
        }
    }
}
