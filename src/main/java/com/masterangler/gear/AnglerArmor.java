package com.masterangler.gear;

public class AnglerArmor {
    public enum ArmorType {
        HAT, VEST, PANTS, BOOTS
    }

    private ArmorType type;
    private float waterResistance;
    private float fishingLuckBonus;

    public AnglerArmor(ArmorType type, float waterResistance, float fishingLuckBonus) {
        this.type = type;
        this.waterResistance = waterResistance;
        this.fishingLuckBonus = fishingLuckBonus;
    }

    public ArmorType getType() { return type; }
    public float getWaterResistance() { return waterResistance; }
    public float getFishingLuckBonus() { return fishingLuckBonus; }
}
