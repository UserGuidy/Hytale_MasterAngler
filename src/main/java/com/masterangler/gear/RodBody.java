package com.masterangler.gear;

public class RodBody {
    private float durability;
    private float weight;
    private float maxWeightCapacity; // in kg
    private String color;

    public RodBody(float durability, float weight, float maxWeightCapacity, String color) {
        this.durability = durability;
        this.weight = weight;
        this.maxWeightCapacity = maxWeightCapacity;
        this.color = color;
    }

    // Legacy/Mock constructor for manual creation without color
    public RodBody(float durability, float weight, float maxWeightCapacity) {
        this(durability, weight, maxWeightCapacity, "#FFFFFF");
    }

    public float getDurability() { return durability; }
    public float getWeight() { return weight; }
    public float getMaxWeightCapacity() { return maxWeightCapacity; }
    public String getColor() { return color; }

    public void decreaseDurability(float amount) {
        this.durability -= amount;
        if (this.durability < 0) this.durability = 0;
    }
}
