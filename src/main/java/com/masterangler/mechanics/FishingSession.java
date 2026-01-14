package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.masterangler.mock.Player;
// In a real implementation, Fish would be a class
import com.masterangler.mock.BaseEntity;

public class FishingSession {
    private Player player;
    private DynamicRod rod;
    private float currentTension = 0.0f;
    private boolean isFishHooked = false;
    private float hookedFishWeight = 0.0f;
    private float hookedFishStrength = 0.0f;

    // Status flags
    private boolean reeling = false;

    public FishingSession(Player player, DynamicRod rod) {
        this.player = player;
        this.rod = rod;
    }

    public Player getPlayer() {
        return player;
    }

    public DynamicRod getRod() {
        return rod;
    }

    public float getCurrentTension() {
        return currentTension;
    }

    public void setCurrentTension(float tension) {
        this.currentTension = tension;
    }

    public boolean isFishHooked() {
        return isFishHooked;
    }

    public void hookFish(float weight, float strength) {
        this.isFishHooked = true;
        this.hookedFishWeight = weight;
        this.hookedFishStrength = strength;
    }

    public float getHookedFishWeight() {
        return hookedFishWeight;
    }

    public float getHookedFishStrength() {
        return hookedFishStrength;
    }

    public void setReeling(boolean reeling) {
        this.reeling = reeling;
    }

    public boolean isReeling() {
        return reeling;
    }
}
