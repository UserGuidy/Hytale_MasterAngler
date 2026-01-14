package com.masterangler.mock.events;

import com.masterangler.mock.ItemStack;
import com.masterangler.mock.Player;

public class UseBlockEvent {
    private Player player;
    private ItemStack itemStack;
    private Object targetBlock; // Mock block
    private boolean cancelled = false;

    public UseBlockEvent(Player player, ItemStack itemStack, Object targetBlock) {
        this.player = player;
        this.itemStack = itemStack;
        this.targetBlock = targetBlock;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return itemStack;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
