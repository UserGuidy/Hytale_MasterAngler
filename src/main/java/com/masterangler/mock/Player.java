package com.masterangler.mock;

public class Player extends BaseEntity {
    private String name;

    public Player(int entityId, String name) {
        super(entityId);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
