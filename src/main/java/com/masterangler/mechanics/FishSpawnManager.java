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
    private Random random = new Random();

    public FishSpawnManager(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
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

        return availableFish.get(random.nextInt(availableFish.size()));
    }

    public float generateWeight(FishDefinition fish) {
        float range = fish.getMaxWeight() - fish.getMinWeight();
        return fish.getMinWeight() + (random.nextFloat() * range);
    }
}
