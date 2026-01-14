package com.masterangler.gear;

import com.masterangler.mock.Player;
import java.util.HashMap;
import java.util.Map;

import com.masterangler.data.DataLoader;

public class AnglerArmorManager {

    // In a real plugin, this would interface with Hytale's inventory/equipment system.
    // Here we mock the equipped armor for each player.

    private Map<Player, Map<AnglerArmor.ArmorType, AnglerArmor>> equippedArmor = new HashMap<>();
    private DataLoader dataLoader;

    public void setDataLoader(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public void equipArmor(Player player, AnglerArmor armor) {
        equippedArmor.computeIfAbsent(player, k -> new HashMap<>()).put(armor.getType(), armor);
        System.out.println(player.getName() + " equipped " + armor.getType() + " (+Luck: " + armor.getFishingLuckBonus() + ")");
    }

    // Helper to equip by ID (from command or JSON)
    public void equipArmorById(Player player, String armorId) {
        if (dataLoader != null) {
            AnglerArmor armor = dataLoader.getAllArmor().get(armorId.toLowerCase());
            if (armor != null) {
                equipArmor(player, armor);
            } else {
                System.out.println("Armor not found: " + armorId);
            }
        }
    }

    public void unequipArmor(Player player, AnglerArmor.ArmorType type) {
        if (equippedArmor.containsKey(player)) {
            equippedArmor.get(player).remove(type);
        }
    }

    public float getTotalLuckBonus(Player player) {
        if (!equippedArmor.containsKey(player)) return 0.0f;

        Map<AnglerArmor.ArmorType, AnglerArmor> pieces = equippedArmor.get(player);
        float total = (float) pieces.values().stream()
                .mapToDouble(AnglerArmor::getFishingLuckBonus)
                .sum();

        // Check Set Bonus
        // Simplification: Check if 4 pieces exist and share same non-null set ID.
        if (pieces.size() == 4) {
             String firstSet = pieces.values().iterator().next().getSetId();
             if (firstSet != null) {
                 boolean allSame = pieces.values().stream().allMatch(a -> firstSet.equals(a.getSetId()));
                 if (allSame) {
                     System.out.println("Set Bonus Active: " + firstSet);
                     total += 0.5f; // Flat bonus for full set
                 }
             }
        }

        return total;
    }
}
