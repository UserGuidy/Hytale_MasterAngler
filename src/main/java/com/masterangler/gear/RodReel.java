package com.masterangler.gear;

public class RodReel {
    private float reelSpeed;
    private float afkEfficiency;
    private String color;

    public RodReel(float reelSpeed, float afkEfficiency, String color) {
        this.reelSpeed = reelSpeed;
        this.afkEfficiency = afkEfficiency;
        this.color = color;
    }

    public RodReel(float reelSpeed, float afkEfficiency) {
        this(reelSpeed, afkEfficiency, "#FFFFFF");
    }

    public float getReelSpeed() { return reelSpeed; }
    public float getAfkEfficiency() { return afkEfficiency; }
    public String getColor() { return color; }
}
