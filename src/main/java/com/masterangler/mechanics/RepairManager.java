package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.masterangler.mock.Player;

public class RepairManager {

    public void repairRod(DynamicRod rod, Player player) {
        // Mock resource check (e.g. check for "Repair Kit")
        // if (player.getInventory().contains("RepairKit")) ...

        float missingDurability = 100.0f - rod.getBody().getDurability(); // Assume 100 max
        if (missingDurability > 0) {
            rod.getBody().decreaseDurability(-missingDurability); // Hack to add durability
            System.out.println("[Repair] Rod repaired fully for " + player.getName());
        } else {
            System.out.println("[Repair] Rod is already full durability.");
        }
    }
}
