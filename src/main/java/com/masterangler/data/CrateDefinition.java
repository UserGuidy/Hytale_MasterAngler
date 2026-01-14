package com.masterangler.data;

import java.util.List;

public class CrateDefinition {
    private String id;
    private String name;
    private float minPowerRequired;
    private List<String> lootTable; // List of Item IDs (materials)
    private float rarity;

    public String getId() { return id; }
    public String getName() { return name; }
    public float getMinPowerRequired() { return minPowerRequired; }
    public List<String> getLootTable() { return lootTable; }
    public float getRarity() { return rarity; }
}
