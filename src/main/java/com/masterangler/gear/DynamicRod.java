package com.masterangler.gear;

public class DynamicRod {
    private RodBody body;
    private RodLine line;
    private RodReel reel;
    private RodBait bait;

    public DynamicRod(RodBody body, RodLine line, RodReel reel, RodBait bait) {
        this.body = body;
        this.line = line;
        this.reel = reel;
        this.bait = bait;
    }

    public RodBody getBody() { return body; }
    public RodLine getLine() { return line; }
    public RodReel getReel() { return reel; }
    public RodBait getBait() { return bait; }

    public void setBait(RodBait bait) {
        this.bait = bait;
    }
}
