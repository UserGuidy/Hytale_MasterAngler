package com.masterangler.mechanics;

public class TensionManager {

    /**
     * Calculates the new tension based on physics mechanics.
     *
     * Formula: currentTension += (fishWeight / breakingStrength) * (fishStrength - (reelSpeed * 0.5f)) * deltaTime.
     *
     * @param currentTension The current tension value (0.0 to 1.0 ideally, but logic handles accumulation).
     * @param fishWeight The weight of the fish in kg.
     * @param fishStrength The strength of the fish.
     * @param breakingStrength The breaking strength of the line in kg.
     * @param reelSpeed The reeling speed of the reel.
     * @param deltaTime Time elapsed since last tick in seconds.
     * @return The calculated delta tension to be added to current tension.
     */
    public float calculateTensionDelta(float fishWeight, float fishStrength, float breakingStrength, float reelSpeed, float deltaTime) {

        float baseDelta = (fishWeight / breakingStrength) * (fishStrength - (reelSpeed * 0.5f)) * deltaTime;

        // Critical Weight logic: If fishWeight > breakingStrength, the tension rises 2x faster.
        if (fishWeight > breakingStrength) {
            baseDelta *= 2.0f;
        }

        return baseDelta;
    }

    /**
     * Checks durability loss for the rod body.
     *
     * @param fishWeight The weight of the fish.
     * @param maxWeightCapacity The max weight capacity of the rod body.
     * @return The amount of durability to lose.
     */
    public float calculateDurabilityLoss(float fishWeight, float maxWeightCapacity) {
        if (fishWeight > maxWeightCapacity) {
            // Durability drops quickly if weight exceeds capacity
            return 5.0f;
        }
        return 1.0f; // Standard loss
    }

    /**
     * Updates the tension state and syncs with the client.
     *
     * @param context The server context to send packets.
     * @param player The player involved in the fishing event.
     * @param currentTension The current accumulated tension.
     * @param isLineBroken Whether the line has snapped.
     */
    public void updateAndSync(com.masterangler.mock.ServerContext context, com.masterangler.mock.Player player, float currentTension, boolean isLineBroken) {
        // Create the sync packet
        com.masterangler.networking.TensionSyncPacket packet = new com.masterangler.networking.TensionSyncPacket(currentTension, isLineBroken);

        // Send to client
        context.sendPacket(player, packet);
    }
}
