package com.masterangler.gear;

public class RodBody {
    private float durability;
    private float weight;
    private float maxWeightCapacity; // in kg
    private float fishingPower;
    private String color;

    public RodBody(float durability, float weight, float maxWeightCapacity, float fishingPower, String color) {
        this.durability = durability;
        this.weight = weight;
        this.maxWeightCapacity = maxWeightCapacity;
        this.fishingPower = fishingPower;
        this.color = color;
    }

    // Legacy/Mock constructor for manual creation without color
    public RodBody(float durability, float weight, float maxWeightCapacity) {
        this(durability, weight, maxWeightCapacity, 0.0f, "#FFFFFF");
    }

    public float getDurability() { return durability; }
    public float getWeight() { return weight; }
    public float getMaxWeightCapacity() { return maxWeightCapacity; }
    public float getFishingPower() { return fishingPower; }
    public String getColor() { return color; }

    public void decreaseDurability(float amount) {
        this.durability -= amount;
        if (this.durability < 0) this.durability = 0;
    }
}
