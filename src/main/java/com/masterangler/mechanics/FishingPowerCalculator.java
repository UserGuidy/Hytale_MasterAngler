package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.masterangler.gear.AnglerArmorManager;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class FishingPowerCalculator {

    public static float calculateFishingPower(DynamicRod rod, Player player, AnglerArmorManager armorManager) {
        float power = 0.0f;

        if (rod != null) {
            // Base power from Rod Body (Primary source of power)
            power += rod.getBody().getFishingPower();
        }

        if (player != null && armorManager != null) {
            // Power bonus from Armor
            power += armorManager.getTotalFishingPowerBonus(player);
        }

        return power;
    }
}
