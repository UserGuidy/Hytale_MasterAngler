package com.masterangler.mechanics;

import com.masterangler.gear.DynamicRod;
import com.hypixel.hytale.server.core.entity.entities.Player;
import java.util.HashMap;
import java.util.Map;

public class FishingSessionManager {
    private Map<Player, FishingSession> sessions = new HashMap<>();

    public FishingSession startSession(Player player, DynamicRod rod) {
        FishingSession session = new FishingSession(player, rod);
        sessions.put(player, session);
        // Using getDisplayName() or getUsername() depending on exact API, assume getUsername based on memory.
        // If not available, we can use player.toString()
        // Memory says getUsername() is available on PlayerRef, Player extends Entity.
        // Let's use simple logging.
        // System.out.println("[FishingSessionManager] Started session for " + player);
        return session;
    }

    public FishingSession getSession(Player player) {
        return sessions.get(player);
    }

    public void endSession(Player player) {
        sessions.remove(player);
        // System.out.println("[FishingSessionManager] Ended session for " + player);
    }
}
