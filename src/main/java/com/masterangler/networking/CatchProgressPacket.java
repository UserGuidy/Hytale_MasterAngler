package com.masterangler.networking;

import com.masterangler.mock.Packet;

public class CatchProgressPacket extends Packet {
    private float progress; // 0.0 to 1.0

    public CatchProgressPacket(float progress) {
        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }
}
