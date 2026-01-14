package com.masterangler.gear;

public class RodReel {
    private float reelSpeed;
    private float afkEfficiency;

    public RodReel(float reelSpeed, float afkEfficiency) {
        this.reelSpeed = reelSpeed;
        this.afkEfficiency = afkEfficiency;
    }

    public float getReelSpeed() { return reelSpeed; }
    public float getAfkEfficiency() { return afkEfficiency; }
}
