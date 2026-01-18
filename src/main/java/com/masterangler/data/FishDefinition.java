package com.masterangler.data;

public class FishDefinition {
    private String name;
    private float minWeight;
    private float maxWeight;
    private float minSize; // cm
    private float maxSize; // cm
    private float fishStrength; // Base strength
    private String biomeID;
    private float weatherMultiplier; // 1.0 default
    private int xpReward;
    private java.util.List<String> preferredBaits; // List of bait IDs

    // Visuals
    private String icon; // Item ID for inventory icon
    private String rarity; // COMMON, RARE, LEGENDARY

    public String getName() { return name; }
    public float getMinWeight() { return minWeight; }
    public float getMaxWeight() { return maxWeight; }
    public float getMinSize() { return minSize; }
    public float getMaxSize() { return maxSize; }
    public float getFishStrength() { return fishStrength; }
    public String getBiomeID() { return biomeID; }
    public float getWeatherMultiplier() { return weatherMultiplier; }
    public int getXpReward() { return xpReward; }

    public java.util.List<String> getPreferredBaits() {
        return preferredBaits != null ? preferredBaits : new java.util.ArrayList<>();
    }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getRarity() { return rarity != null ? rarity : "COMMON"; }
    public void setRarity(String rarity) { this.rarity = rarity; }
}
