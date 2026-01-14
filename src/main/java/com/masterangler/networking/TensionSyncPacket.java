package com.masterangler.networking;

import com.masterangler.mock.Packet;

public class TensionSyncPacket extends Packet {
    private float tension;
    private boolean isLineBroken;

    public TensionSyncPacket(float tension, boolean isLineBroken) {
        this.tension = tension;
        this.isLineBroken = isLineBroken;
    }

    public float getTension() {
        return tension;
    }

    public boolean isLineBroken() {
        return isLineBroken;
    }
}
