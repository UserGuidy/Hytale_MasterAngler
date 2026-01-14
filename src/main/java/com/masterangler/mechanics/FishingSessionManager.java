package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.masterangler.mock.Player;
import java.util.HashMap;
import java.util.Map;

public class FishingSessionManager {
    private Map<Player, FishingSession> sessions = new HashMap<>();

    public FishingSession startSession(Player player, DynamicRod rod) {
        FishingSession session = new FishingSession(player, rod);
        sessions.put(player, session);
        System.out.println("[FishingSessionManager] Started session for " + player.getName());
        return session;
    }

    public FishingSession getSession(Player player) {
        return sessions.get(player);
    }

    public void endSession(Player player) {
        sessions.remove(player);
        System.out.println("[FishingSessionManager] Ended session for " + player.getName());
    }
}
