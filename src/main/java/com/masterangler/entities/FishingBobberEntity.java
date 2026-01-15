package com.masterangler.entities;

import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.World;

public class FishingBobberEntity extends Entity {

    private boolean isInWater = false;
    private Vector3d velocity = new Vector3d(0, 0, 0);

    public FishingBobberEntity(World world) {
        super(world);
    }

    // In a real implementation, we would override a tick() method or register a system.
    // For this mod, we assume there is a way to tick entities or we call it manually from a manager.
    // Since Entity doesn't expose a public 'tick', we might rely on systems.
    // However, to keep it simple for this phase, we add a public tick method that the SessionManager can call.

    public void tick() {
        if (world == null) return;

        // Simple Gravity
        velocity = velocity.add(0, -0.05, 0);

        // Simple Physics integration
        // this.setPosition(this.getPosition().add(velocity));
        // Note: Entity usually has getTransformComponent() or similar.
        // We will assume a helper wrapper or just logic placeholder here.

        // Mock Water Check
        // if (world.getBlock(this.getPosition()).isWater()) {
        //     isInWater = true;
        //     velocity = velocity.multiply(0.8); // Drag
        //     velocity = velocity.add(0, 0.06, 0); // Buoyancy
        // }
    }

    public void setVelocity(Vector3d vel) {
        this.velocity = vel;
    }
}
