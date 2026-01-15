package com.masterangler.networking;

import com.hypixel.hytale.protocol.Packet;
import io.netty.buffer.ByteBuf;

public class TensionSyncPacket implements Packet {
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

    @Override
    public int getId() {
        return 42002;
    }

    @Override
    public int computeSize() {
        return 5; // float (4) + boolean (1)
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeFloat(tension);
        buf.writeBoolean(isLineBroken);
    }
}
