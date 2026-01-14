package com.masterangler.data;

public class LootDefinition {
    private String id;
    private String name;
    private float rarity; // 0.0 to 1.0 (lower is rarer)

    public String getId() { return id; }
    public String getName() { return name; }
    public float getRarity() { return rarity; }
}
