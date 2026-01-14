package com.masterangler.gear;

public class RodBait {
    private float attractivity;
    private float catchRate;

    public RodBait(float attractivity, float catchRate) {
        this.attractivity = attractivity;
        this.catchRate = catchRate;
    }

    public float getAttractivity() { return attractivity; }
    public float getCatchRate() { return catchRate; }
}
