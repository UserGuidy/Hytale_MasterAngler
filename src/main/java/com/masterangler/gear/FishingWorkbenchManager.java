package com.masterangler.gear;

public class FishingWorkbenchManager {

    public DynamicRod assembleRod(RodBody body, RodLine line, RodReel reel, RodBait bait) {
        if (body == null || line == null || reel == null) {
            throw new IllegalArgumentException("Body, Line and Reel are required components.");
        }
        // Bait is optional initially, can be attached later, but here we allow it in assembly
        return new DynamicRod(body, line, reel, bait);
    }
}
