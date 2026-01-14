package com.masterangler.gear;

public class RodLine {
    private float maxTension;
    private float flexibility;
    private float breakingStrength; // in kg

    public RodLine(float maxTension, float flexibility, float breakingStrength) {
        this.maxTension = maxTension;
        this.flexibility = flexibility;
        this.breakingStrength = breakingStrength;
    }

    public float getMaxTension() { return maxTension; }
    public float getFlexibility() { return flexibility; }
    public float getBreakingStrength() { return breakingStrength; }
}
