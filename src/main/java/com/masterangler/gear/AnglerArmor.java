package com.masterangler.gear;

public class AnglerArmor {
    public enum ArmorType {
        HAT, VEST, PANTS, BOOTS
    }

    private ArmorType type;
    private float waterResistance;
    private float fishingPowerBonus; // Renamed from luck, as it contributes to Fishing Power
    private String setId;

    public AnglerArmor(ArmorType type, float waterResistance, float fishingPowerBonus) {
        this(type, waterResistance, fishingPowerBonus, null);
    }

    public AnglerArmor(ArmorType type, float waterResistance, float fishingPowerBonus, String setId) {
        this.type = type;
        this.waterResistance = waterResistance;
        this.fishingPowerBonus = fishingPowerBonus;
        this.setId = setId;
    }

    public ArmorType getType() { return type; }
    public float getWaterResistance() { return waterResistance; }
    public float getFishingPowerBonus() { return fishingPowerBonus; }
    public String getSetId() { return setId; }
}
