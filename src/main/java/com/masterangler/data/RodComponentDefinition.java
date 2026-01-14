package com.masterangler.data;

public class RodComponentDefinition {
    public enum ComponentType {
        BODY, LINE, REEL, BAIT
    }

    private String id;
    private String name;
    private ComponentType type;

    // Union of all possible stats (some will be 0/null depending on type)
    private float durability;        // Body
    private float weight;            // Body
    private float maxWeightCapacity; // Body

    private float maxTension;        // Line
    private float flexibility;       // Line
    private float breakingStrength;  // Line

    private float reelSpeed;         // Reel
    private float afkEfficiency;     // Reel

    private float attractivity;      // Bait
    private float catchRate;         // Bait

    public String getId() { return id; }
    public String getName() { return name; }
    public ComponentType getType() { return type; }

    public float getDurability() { return durability; }
    public float getWeight() { return weight; }
    public float getMaxWeightCapacity() { return maxWeightCapacity; }

    public float getMaxTension() { return maxTension; }
    public float getFlexibility() { return flexibility; }
    public float getBreakingStrength() { return breakingStrength; }

    public float getReelSpeed() { return reelSpeed; }
    public float getAfkEfficiency() { return afkEfficiency; }

    public float getAttractivity() { return attractivity; }
    public float getCatchRate() { return catchRate; }
}
