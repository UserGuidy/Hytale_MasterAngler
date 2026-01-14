package com.masterangler.mechanics;

import com.masterangler.data.CrateDefinition;
import com.masterangler.data.DataLoader;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CrateLootManager {
    private DataLoader dataLoader;
    private Random random = new Random();

    public CrateLootManager(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Tries to fish up a crate based on fishing power.
     */
    public CrateDefinition rollForCrate(float fishingPower) {
        List<CrateDefinition> crates = dataLoader.getAllCrates().stream()
                .filter(c -> fishingPower >= c.getMinPowerRequired())
                .collect(Collectors.toList());

        if (crates.isEmpty()) return null;

        // Simple weighted selection or just random from available for this mock
        for (CrateDefinition crate : crates) {
            if (random.nextFloat() < crate.getRarity()) {
                return crate;
            }
        }
        return null;
    }

    /**
     * Opens a crate and returns a material item ID.
     */
    public String openCrate(CrateDefinition crate) {
        List<String> loot = crate.getLootTable();
        if (loot == null || loot.isEmpty()) return "air";
        return loot.get(random.nextInt(loot.size()));
    }
}
