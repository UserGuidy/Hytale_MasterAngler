package com.masterangler.gear;

import com.masterangler.data.DataLoader;
import com.masterangler.data.RodComponentDefinition;
import com.masterangler.mock.ItemStack;

public class FishingWorkbenchManager {

    private DataLoader dataLoader;

    public FishingWorkbenchManager() {
    }

    public void setDataLoader(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public DynamicRod assembleRod(RodBody body, RodLine line, RodReel reel, RodBait bait) {
        if (body == null || line == null || reel == null) {
            throw new IllegalArgumentException("Body, Line and Reel are required components.");
        }
        // Bait is optional initially, can be attached later, but here we allow it in assembly
        return new DynamicRod(body, line, reel, bait);
    }

    /**
     * Crafts a rod from ItemStacks by looking up their definitions.
     */
    public DynamicRod validateAndCraft(ItemStack bodyItem, ItemStack lineItem, ItemStack reelItem, ItemStack baitItem) {
        if (dataLoader == null) {
            System.err.println("Workbench cannot craft: DataLoader not set.");
            return null;
        }

        RodBody body = (RodBody) createComponent(bodyItem, RodComponentDefinition.ComponentType.BODY);
        RodLine line = (RodLine) createComponent(lineItem, RodComponentDefinition.ComponentType.LINE);
        RodReel reel = (RodReel) createComponent(reelItem, RodComponentDefinition.ComponentType.REEL);
        RodBait bait = (RodBait) createComponent(baitItem, RodComponentDefinition.ComponentType.BAIT);

        if (body != null && line != null && reel != null) {
            System.out.println("Crafting Rod success!");
            return new DynamicRod(body, line, reel, bait); // bait can be null
        } else {
            System.out.println("Crafting failed: Invalid components.");
            return null;
        }
    }

    private Object createComponent(ItemStack item, RodComponentDefinition.ComponentType expectedType) {
        if (item == null) return null;

        // In real app, getItem().getName() or ID should map to the JSON ID
        // For mock, we assume the item Name IS the ID
        String id = item.getItem().getName();
        RodComponentDefinition def = dataLoader.getComponent(id);

        if (def == null) {
             System.out.println("Unknown component: " + id);
             return null;
        }

        if (def.getType() != expectedType) {
            System.out.println("Wrong component type. Expected " + expectedType + " but got " + def.getType());
            return null;
        }

        switch (expectedType) {
            case BODY: return new RodBody(def.getDurability(), def.getWeight(), def.getMaxWeightCapacity());
            case LINE: return new RodLine(def.getMaxTension(), def.getFlexibility(), def.getBreakingStrength());
            case REEL: return new RodReel(def.getReelSpeed(), def.getAfkEfficiency());
            case BAIT: return new RodBait(def.getAttractivity(), def.getCatchRate());
            default: return null;
        }
    }
}
