package com.masterangler.gear;

public class AnglerArmor {
    public enum ArmorType {
        HAT, VEST, PANTS, BOOTS
    }

    private ArmorType type;
    private float waterResistance;
    private float fishingLuckBonus;
    private String setId; // e.g., "set_rain_gear"

    public AnglerArmor(ArmorType type, float waterResistance, float fishingLuckBonus) {
        this(type, waterResistance, fishingLuckBonus, null);
    }

    public AnglerArmor(ArmorType type, float waterResistance, float fishingLuckBonus, String setId) {
        this.type = type;
        this.waterResistance = waterResistance;
        this.fishingLuckBonus = fishingLuckBonus;
        this.setId = setId;
    }

    public ArmorType getType() { return type; }
    public float getWaterResistance() { return waterResistance; }
    public float getFishingLuckBonus() { return fishingLuckBonus; }
    public String getSetId() { return setId; }
}
