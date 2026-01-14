package com.masterangler.mock;

public abstract class BaseEntity {
    private int entityId;

    public BaseEntity(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }
}
