package com.masterangler.data;

public class FishDefinition {
    private String name;
    private float minWeight;
    private float maxWeight;
    private float fishStrength; // Base strength
    private String biomeID;
    private float weatherMultiplier; // 1.0 default
    private int xpReward;

    public String getName() { return name; }
    public float getMinWeight() { return minWeight; }
    public float getMaxWeight() { return maxWeight; }
    public float getFishStrength() { return fishStrength; }
    public String getBiomeID() { return biomeID; }
    public float getWeatherMultiplier() { return weatherMultiplier; }
    public int getXpReward() { return xpReward; }
}
