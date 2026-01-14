package com.masterangler.mock.events;

import com.masterangler.mock.ItemStack;
import com.masterangler.mock.Player;

public class PlayerInteractEvent {
    private Player player;
    private ItemStack itemStack;

    public PlayerInteractEvent(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return itemStack;
    }
}
