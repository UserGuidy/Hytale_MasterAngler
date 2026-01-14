package com.masterangler.gear;

public class RodBody {
    private float durability;
    private float weight;
    private float maxWeightCapacity; // in kg

    public RodBody(float durability, float weight, float maxWeightCapacity) {
        this.durability = durability;
        this.weight = weight;
        this.maxWeightCapacity = maxWeightCapacity;
    }

    public float getDurability() { return durability; }
    public float getWeight() { return weight; }
    public float getMaxWeightCapacity() { return maxWeightCapacity; }

    public void decreaseDurability(float amount) {
        this.durability -= amount;
        if (this.durability < 0) this.durability = 0;
    }
}
