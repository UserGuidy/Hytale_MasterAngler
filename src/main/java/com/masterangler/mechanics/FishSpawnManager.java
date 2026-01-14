package com.masterangler.mechanics;

import com.masterangler.data.DataLoader;
import com.masterangler.data.FishDefinition;
import com.masterangler.gear.RodBait;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.masterangler.gear.AnglerArmorManager;

public class FishSpawnManager {
    private DataLoader dataLoader;
    private AnglerArmorManager armorManager;
    private LootTableManager lootManager;
    private Random random = new Random();

    public FishSpawnManager(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public void setLootManager(LootTableManager lootManager) {
        this.lootManager = lootManager;
    }

    public void setArmorManager(AnglerArmorManager armorManager) {
        this.armorManager = armorManager;
    }

    /**
     * Selects a fish based on environment, bait, and armor luck.
     *
     * @param biomeId The current biome ID.
     * @param weather The current weather (e.g. "rain", "clear").
     * @param bait The bait used.
     * @param player The player fishing (to check armor).
     * @return A selected FishDefinition, or null if nothing bites.
     */
    public FishDefinition selectFish(String biomeId, String weather, RodBait bait, com.masterangler.mock.Player player) {
        List<FishDefinition> availableFish = dataLoader.getAllFish().stream()
                .filter(f -> f.getBiomeID().equalsIgnoreCase(biomeId))
                .collect(Collectors.toList());

        if (availableFish.isEmpty()) {
            return null;
        }

        // Apply Luck Bonus
        float luckBonus = 0.0f;
        if (armorManager != null && player != null) {
            luckBonus = armorManager.getTotalLuckBonus(player);
        }

        // Simple Luck Logic: Chance to reroll if result is "common" (not implemented here as rarity isn't in definition yet)
        // Or simple multiplier to "catch rate".
        // Here we just print it to verify logic is connected.
        if (luckBonus > 0) {
            System.out.println("Player has " + luckBonus + " luck bonus!");
        }

        // Treasure Check (Mocking return type as FishDefinition for now, ideally we return a CatchResult wrapper)
        // For Phase 4, we just log the treasure if rolled, but return a fish so gameplay loop continues.
        // To properly implement, FishingSession would need to handle "Non-Fish" catches.
        if (lootManager != null) {
            com.masterangler.data.LootDefinition loot = lootManager.rollForTreasure(luckBonus);
            if (loot != null) {
                System.out.println("Wait! You feel something heavy... it's a " + loot.getName());
                // In a full implementation, we would return a special "FishDefinition" representing loot or change method signature.
            }
        }

        return availableFish.get(random.nextInt(availableFish.size()));
    }

    public float generateWeight(FishDefinition fish) {
        float range = fish.getMaxWeight() - fish.getMinWeight();
        return fish.getMinWeight() + (random.nextFloat() * range);
    }

    public float generateSize(FishDefinition fish) {
        float range = fish.getMaxSize() - fish.getMinSize();
        return fish.getMinSize() + (random.nextFloat() * range);
    }
}
