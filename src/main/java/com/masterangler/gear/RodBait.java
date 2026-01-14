package com.masterangler.gear;

public class RodBait {
    private String id; // Needed to match preferredBaits
    private float attractivity;
    private float catchRate;

    public RodBait(String id, float attractivity, float catchRate) {
        this.id = id;
        this.attractivity = attractivity;
        this.catchRate = catchRate;
    }

    // Legacy/Mock constructor
    public RodBait(float attractivity, float catchRate) {
        this("unknown_bait", attractivity, catchRate);
    }

    public String getId() { return id; }
    public float getAttractivity() { return attractivity; }
    public float getCatchRate() { return catchRate; }
}
