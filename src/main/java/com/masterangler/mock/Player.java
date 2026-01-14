package com.masterangler.mock;

public class Player extends BaseEntity {
    private String name;
    private Inventory inventory;
    private int level = 1;

    public Player(int entityId, String name) {
        super(entityId);
        this.name = name;
        this.inventory = new Inventory();
    }

    public String getName() {
        return name;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
