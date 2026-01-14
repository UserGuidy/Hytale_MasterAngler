package com.masterangler.mechanics;

import com.masterangler.data.DataLoader;
import com.masterangler.data.FishDefinition;
import com.masterangler.gear.RodBait;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class FishSpawnManager {
    private DataLoader dataLoader;
    private Random random = new Random();

    public FishSpawnManager(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Selects a fish based on environment and bait.
     *
     * @param biomeId The current biome ID.
     * @param weather The current weather (e.g. "rain", "clear").
     * @param bait The bait used.
     * @return A selected FishDefinition, or null if nothing bites.
     */
    public FishDefinition selectFish(String biomeId, String weather, RodBait bait) {
        List<FishDefinition> availableFish = dataLoader.getAllFish().stream()
                .filter(f -> f.getBiomeID().equalsIgnoreCase(biomeId))
                .collect(Collectors.toList());

        if (availableFish.isEmpty()) {
            return null;
        }

        // Simple random selection for now.
        // Phase 2 TODO: Implement weight based on bait attractivity and weather multiplier.
        // For now, if bait attractivity is high, we just ensure we pick *something*.

        return availableFish.get(random.nextInt(availableFish.size()));
    }

    public float generateWeight(FishDefinition fish) {
        float range = fish.getMaxWeight() - fish.getMinWeight();
        return fish.getMinWeight() + (random.nextFloat() * range);
    }
}
