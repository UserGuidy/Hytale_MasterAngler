package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class FishingSession {
    private Player player;
    private DynamicRod rod;
    private float currentTension = 0.0f;
    private float catchProgress = 0.0f; // 0.0 to 1.0 (100%)
    private boolean isFishHooked = false;
    private com.masterangler.data.FishDefinition hookedFish;
    private float hookedFishWeight = 0.0f;
    private float hookedFishSize = 0.0f;

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

    public float getCatchProgress() {
        return catchProgress;
    }

    public void setCatchProgress(float progress) {
        this.catchProgress = progress;
    }

    public boolean isFishHooked() {
        return isFishHooked;
    }

    public void hookFish(com.masterangler.data.FishDefinition fish, float weight, float size) {
        this.isFishHooked = true;
        this.hookedFish = fish;
        this.hookedFishWeight = weight;
        this.hookedFishSize = size;
    }

    public com.masterangler.data.FishDefinition getHookedFish() {
        return hookedFish;
    }

    public float getHookedFishWeight() {
        return hookedFishWeight;
    }

    public float getHookedFishSize() {
        return hookedFishSize;
    }

    public float getHookedFishStrength() {
        return hookedFish != null ? hookedFish.getFishStrength() : 0.0f;
    }

    public void setReeling(boolean reeling) {
        this.reeling = reeling;
    }

    public boolean isReeling() {
        return reeling;
    }
}
