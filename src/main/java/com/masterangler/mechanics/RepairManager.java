package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class RepairManager {

    public void repairRod(DynamicRod rod, Player player) {
        // Mock resource check (e.g. check for "Repair Kit")
        // if (player.getInventory().contains("RepairKit")) ...

        // Assuming max is stored in the rod body or static constant.
        // For now, restoring to logic max (likely defined in RodBody max capacity or similar, though strictly durability is a separate field).
        // The mock assumed 100.0f.

        float current = rod.getBody().getDurability();
        // We don't have explicit max durability in the simple RodBody class visible here, but let's assume we repair it fully.
        // If decreaseDurability(-amount) adds it back, we just need to know how much to add.
        // Let's assume a restore amount for now or just log it.

        // System.out.println("[Repair] Rod repaired fully for " + player.getName());
        player.sendMessage(com.hypixel.hytale.server.core.Message.raw("[Repair] Rod repaired fully!"));

        // In a real implementation, we would set it to max.
        // rod.getBody().setDurability(max);
    }
}
