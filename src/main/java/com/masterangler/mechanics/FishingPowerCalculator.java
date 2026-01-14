package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.masterangler.gear.AnglerArmorManager;
import com.masterangler.mock.Player;

public class FishingPowerCalculator {

    public static float calculateFishingPower(DynamicRod rod, Player player, AnglerArmorManager armorManager) {
        float power = 0.0f;

        if (rod != null) {
            // Base power from gear
            // Line strength is the primary factor for handling heavy fish
            power += rod.getLine().getBreakingStrength();

            // Reel speed adds a bit of "efficiency" power
            power += rod.getReel().getReelSpeed() * 0.5f;
        }

        if (player != null && armorManager != null) {
            // Luck bonus directly adds to "effective" power for rarity checks
            power += armorManager.getTotalLuckBonus(player) * 10.0f; // Scale luck to power
        }

        return power;
    }
}
