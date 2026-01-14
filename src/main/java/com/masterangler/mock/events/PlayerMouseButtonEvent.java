package com.masterangler.mock.events;

import com.masterangler.mock.Player;

public class PlayerMouseButtonEvent {
    private Player player;
    private int button; // 0 = Left, 1 = Right
    private boolean isDown;

    public PlayerMouseButtonEvent(Player player, int button, boolean isDown) {
        this.player = player;
        this.button = button;
        this.isDown = isDown;
    }

    public Player getPlayer() {
        return player;
    }

    public int getButton() {
        return button;
    }

    public boolean isDown() {
        return isDown;
    }
}
