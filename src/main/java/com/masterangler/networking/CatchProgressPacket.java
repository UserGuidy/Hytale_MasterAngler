package com.masterangler.networking;

import com.hypixel.hytale.protocol.Packet;
import io.netty.buffer.ByteBuf;

public class CatchProgressPacket implements Packet {
    private float progress; // 0.0 to 1.0

    public CatchProgressPacket(float progress) {
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    @Override
    public int getId() {
        return 42001;
    }

    @Override
    public int computeSize() {
        return 4; // float = 4 bytes
    }

    @Override
    public void serialize(ByteBuf buf) {
        buf.writeFloat(progress);
    }
}
