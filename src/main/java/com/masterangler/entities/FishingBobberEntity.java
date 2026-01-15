package com.masterangler.entities;

import com.hypixel.hytale.server.core.entity.Entity;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.universe.world.World;

public class FishingBobberEntity extends Entity {

    public enum State {
        CASTING,
        IDLE,
        BITE,
        REELING
    }

    private State currentState = State.CASTING;
    private boolean isInWater = false;
    private Vector3d velocity = new Vector3d(0, 0, 0);
    private long spawnTime;

    public FishingBobberEntity(World world) {
        super(world);
        this.spawnTime = System.currentTimeMillis();
    }

    public void setState(State state) {
        if (this.currentState != state) {
            this.currentState = state;
            playAnimation(state.name().toLowerCase()); // Mock animation trigger

            if (state == State.BITE) {
                spawnSplashParticles();
            }
        }
    }

    public State getState() {
        return currentState;
    }

    public void tick() {
        if (world == null) return;

        // Simple Physics Simulation
        if (currentState == State.CASTING) {
            // Gravity
            velocity = velocity.add(0, -0.05, 0);

            // Mock Landing: After 1 second, land in water
            if (System.currentTimeMillis() - spawnTime > 1000 && !isInWater) {
                isInWater = true;
                setState(State.IDLE);
                spawnSplashParticles();
            }
        } else if (currentState == State.IDLE) {
            // Bobbing effect
            velocity = new Vector3d(0, Math.sin(System.currentTimeMillis() / 200.0) * 0.01, 0);
        } else if (currentState == State.BITE) {
            // Violent shaking
            velocity = new Vector3d(
                (Math.random() - 0.5) * 0.1,
                -0.1,
                (Math.random() - 0.5) * 0.1
            );
        }

        // Apply velocity (Mock)
        // this.setPosition(this.getPosition().add(velocity));
    }

    public void setVelocity(Vector3d vel) {
        this.velocity = vel;
    }

    private void playAnimation(String animName) {
        // In real implementation:
        // this.getComponent(ActiveAnimationComponent.class).play(animName);
        // System.out.println("[Bobber] Playing animation: " + animName);
    }

    private void spawnSplashParticles() {
        // In real implementation:
        // world.spawnParticle("splash", this.getPosition());
        // System.out.println("[Bobber] Splash!");
    }
}
