package com.masterangler.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DataLoader {
    private static final Gson gson = new Gson();
    private Map<String, FishDefinition> fishRegistry = new HashMap<>();

    public void loadFishDefinitions() {
        // Path relative to execution root or classpath.
        // In Hytale/Server environment, usually 'run/data' or inside jar.
        // For this phase, we look in src/main/resources/data/master_angler/fish for development context,
        // or the specific data folder layout.

        // Note: In a real compiled plugin, we would use ClassLoader.getResourceAsStream
        // if the data is inside the JAR, or a File path if it's external.
        // We will assume development environment reading from file system for now.

        File fishFolder = new File("src/main/resources/data/master_angler/fish");
        if (!fishFolder.exists()) {
            System.err.println("[DataLoader] Fish data folder not found: " + fishFolder.getAbsolutePath());
            return;
        }

        try (Stream<Path> paths = Files.walk(Paths.get(fishFolder.toURI()))) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".json"))
                 .forEach(this::loadSingleFish);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[DataLoader] Loaded " + fishRegistry.size() + " fish definitions.");
    }

    private void loadSingleFish(Path path) {
        try (Reader reader = new FileReader(path.toFile())) {
            FishDefinition fish = gson.fromJson(reader, FishDefinition.class);
            if (fish != null && fish.getName() != null) {
                fishRegistry.put(fish.getName().toLowerCase(), fish);
                System.out.println("  - Loaded fish: " + fish.getName());
            }
        } catch (IOException e) {
            System.err.println("Failed to load fish from " + path);
            e.printStackTrace();
        }
    }

    public List<FishDefinition> getAllFish() {
        return new ArrayList<>(fishRegistry.values());
    }

    public FishDefinition getFish(String name) {
        return fishRegistry.get(name.toLowerCase());
    }
}
