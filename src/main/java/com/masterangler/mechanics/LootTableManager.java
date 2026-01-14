package com.masterangler.mechanics;

import com.masterangler.data.DataLoader;
import com.masterangler.data.LootDefinition;
import java.util.List;
import java.util.Random;

public class LootTableManager {
    private DataLoader dataLoader;
    private Random random = new Random();

    public LootTableManager(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    /**
     * Tries to find a treasure item.
     * @return LootDefinition or null.
     */
    public LootDefinition rollForTreasure(float luckBonus) {
        // Base treasure chance 5% + luck
        float chance = 0.05f + (luckBonus * 0.1f);

        if (random.nextFloat() < chance) {
            List<LootDefinition> Loot = dataLoader.getAllLoot();
            if (Loot.isEmpty()) return null;
            return Loot.get(random.nextInt(Loot.size()));
        }
        return null;
    }
}
