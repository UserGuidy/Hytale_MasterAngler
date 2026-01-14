package com.masterangler.progression;

public class FishingPlayerLevelManager {

    public int getAfkSlots(int level) {
        if (level >= 20) return 3;
        if (level >= 10) return 2;
        if (level >= 5) return 1;
        return 0; // Level 1 (Amateur)
    }

    public float getStrengthBonus(int level) {
        if (level >= 10) return 1.0f; // +1kg resistance
        return 0.0f;
    }

    public boolean hasAdvancedRepair(int level) {
        return level >= 20;
    }
}
