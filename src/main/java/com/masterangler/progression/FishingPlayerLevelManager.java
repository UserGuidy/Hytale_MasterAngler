package com.masterangler.progression;

import com.hypixel.hytale.server.core.entity.entities.Player;

public class FishingPlayerLevelManager {

    private com.masterangler.data.PersistenceManager persistenceManager;

    public void setPersistenceManager(com.masterangler.data.PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

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

    public void addXp(Player player, int amount) {
        int currentXp = 0;
        int currentLevel = 0; // Default to 0

        // Load from persistence if available
        if (persistenceManager != null) {
            com.masterangler.data.PersistenceManager.PlayerProfile profile = persistenceManager.loadProfile(player);
            currentXp = profile.currentXp;
            currentLevel = profile.level;
        }

        // Using simple logging for now
        // System.out.println("Player " + player.getName() + " gained " + amount + " XP.");

        int newXp = currentXp + amount;

        // Simple 100 XP per level logic from mock, ideally this curve should be configurable
        if (newXp >= 100) {
            currentLevel++;
            newXp -= 100;

            // Send message to player
            player.sendMessage(com.hypixel.hytale.server.core.Message.raw("LEVEL UP! You are now level " + currentLevel));
            // System.out.println("LEVEL UP! " + player.getName() + " is now level " + currentLevel);
        }

        // Save
        if (persistenceManager != null) {
            persistenceManager.saveProfile(player, currentLevel, newXp);
        }
    }
}
