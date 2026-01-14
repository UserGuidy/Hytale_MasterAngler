package com.masterangler.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.masterangler.mock.Player;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class PersistenceManager {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String DATA_DIR = "src/main/resources/data/master_angler/players";

    public static class PlayerProfile {
        public int level = 1;
        public int currentXp = 0;
        public int totalCaught = 0;
    }

    public PersistenceManager() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerProfile loadProfile(Player player) {
        // Use Entity ID as mock UUID for now
        File file = new File(DATA_DIR, player.getEntityId() + ".json");
        if (!file.exists()) {
            return new PlayerProfile(); // New profile
        }

        try (Reader reader = new FileReader(file)) {
            return gson.fromJson(reader, PlayerProfile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new PlayerProfile();
        }
    }

    public void saveProfile(Player player, int level, int xp) {
        PlayerProfile profile = new PlayerProfile();
        profile.level = level;
        profile.currentXp = xp;

        File file = new File(DATA_DIR, player.getEntityId() + ".json");
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(profile, writer);
            System.out.println("[Persistence] Saved profile for " + player.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
