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

        return (float) equippedArmor.get(player).values().stream()
                .mapToDouble(AnglerArmor::getFishingLuckBonus)
                .sum();
    }
}
