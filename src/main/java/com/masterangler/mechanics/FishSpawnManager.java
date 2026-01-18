package com.masterangler.mechanics;

import com.masterangler.data.DataLoader;
import com.masterangler.data.FishDefinition;
import com.masterangler.gear.RodBait;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.masterangler.gear.AnglerArmorManager;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class FishSpawnManager {
    private DataLoader dataLoader;
    private AnglerArmorManager armorManager;
    private LootTableManager lootManager;
    private CrateLootManager crateManager;
    private Random random = new Random();

    public FishSpawnManager(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public void setLootManager(LootTableManager lootManager) {
        this.lootManager = lootManager;
    }

    public void setCrateManager(CrateLootManager crateManager) {
        this.crateManager = crateManager;
    }

    public void setArmorManager(AnglerArmorManager armorManager) {
        this.armorManager = armorManager;
    }

    /**
     * Selects a fish based on environment, bait, armor luck, and fishing power.
     */
    public FishDefinition selectFish(String biomeId, String weather, RodBait bait, Player player, float fishingPower) {
        List<FishDefinition> availableFish = dataLoader.getAllFish().stream()
                .filter(f -> f.getBiomeID().equalsIgnoreCase(biomeId))
                .collect(Collectors.toList());

        if (availableFish.isEmpty()) {
            return null;
        }

        // Treasure / Crate Check
        if (crateManager != null && random.nextFloat() < 0.05f) { // 5% base chance for crate
             com.masterangler.data.CrateDefinition crate = crateManager.rollForCrate(fishingPower);
             if (crate != null) {
                 String content = crateManager.openCrate(crate);
                 player.sendMessage(com.hypixel.hytale.server.core.Message.raw("CRATE CAUGHT! " + crate.getName() + " -> " + content));
                 return null;
             }
        }

        // Weighted Fish Selection
        double totalWeight = 0.0;
        java.util.Map<FishDefinition, Double> weightedMap = new java.util.HashMap<>();

        for (FishDefinition fish : availableFish) {
            double weight = 1.0;

            float diff = fishingPower - fish.getFishStrength();
            if (diff < -5.0f) {
                weight = 0.1; // Impossible to catch
            } else if (diff < 0) {
                weight = 0.5; // Very Hard
            } else if (diff < 10.0f) {
                weight = 10.0; // Sweet spot (Challenging/Fair)
            } else {
                weight = 2.0; // Too easy/Trash
            }

            // Bait Multiplier
            if (fish.getPreferredBaits().contains(bait.getId())) {
                weight *= 5.0;
            }

            // Rarity/Strength Multiplier
            if (fishingPower > 20.0f) {
                weight *= (1.0f + fish.getFishStrength() * 0.1f);
            }

            weightedMap.put(fish, weight);
            totalWeight += weight;
        }

        // Select
        double value = random.nextDouble() * totalWeight;
        for (java.util.Map.Entry<FishDefinition, Double> entry : weightedMap.entrySet()) {
            value -= entry.getValue();
            if (value <= 0) {
                return entry.getKey();
            }
        }

        // Fallback
        return availableFish.get(random.nextInt(availableFish.size()));
    }

    public float generateWeight(FishDefinition fish, float fishingPower) {
        float range = fish.getMaxWeight() - fish.getMinWeight();
        // Skew towards max weight based on fishing power relative to fish strength
        float bias = Math.min(1.0f, fishingPower / (fish.getFishStrength() * 2.0f));
        float randomFactor = (random.nextFloat() + bias) / 2.0f; // Simple average to skew up

        return fish.getMinWeight() + (Math.min(1.0f, randomFactor) * range);
    }

    public float generateSize(FishDefinition fish, float fishingPower) {
        float range = fish.getMaxSize() - fish.getMinSize();
        float bias = Math.min(1.0f, fishingPower / (fish.getFishStrength() * 2.0f));
        float randomFactor = (random.nextFloat() + bias) / 2.0f;

        return fish.getMinSize() + (Math.min(1.0f, randomFactor) * range);
    }

    public int calculateXp(FishDefinition fish, float weight, float size) {
        float weightRatio = (weight - fish.getMinWeight()) / (fish.getMaxWeight() - fish.getMinWeight());
        float bonusMultiplier = 1.0f + weightRatio; // Up to 2x XP for max weight
        return (int) (fish.getXpReward() * bonusMultiplier);
    }
}
