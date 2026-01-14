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
    private Map<String, RodComponentDefinition> componentRegistry = new HashMap<>();
    private Map<String, com.masterangler.gear.AnglerArmor> armorRegistry = new HashMap<>();
    private Map<String, LootDefinition> lootRegistry = new HashMap<>();

    public void loadFishDefinitions() {
        loadData("src/main/resources/data/master_angler/fish", FishDefinition.class, fishRegistry);
    }

    public void loadComponentDefinitions() {
        loadData("src/main/resources/data/master_angler/components", RodComponentDefinition.class, componentRegistry);
    }

    public void loadArmorDefinitions() {
        // AnglerArmor currently doesn't have an ID field in the class, assuming JSON has one or we use filename/type.
        // For simplicity, we'll assume the loaded object is put into the registry if we can identify it.
        // However, AnglerArmor class matches JSON fields. We need a way to key it.
        // Let's assume the filename is the key for now in loadSingleFile logic tweak or we add ID to AnglerArmor.
        loadData("src/main/resources/data/master_angler/armor", com.masterangler.gear.AnglerArmor.class, armorRegistry);
    }

    public void loadLootDefinitions() {
        loadData("src/main/resources/data/master_angler/loot", LootDefinition.class, lootRegistry);
    }

    private <T> void loadData(String pathStr, Class<T> clazz, Map<String, T> registry) {
        File folder = new File(pathStr);
        if (!folder.exists()) {
            System.err.println("[DataLoader] Folder not found: " + folder.getAbsolutePath());
            return;
        }

        try (Stream<Path> paths = Files.walk(Paths.get(folder.toURI()))) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".json"))
                 .forEach(p -> loadSingleFile(p, clazz, registry));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[DataLoader] Loaded " + registry.size() + " items from " + pathStr);
    }

    private <T> void loadSingleFile(Path path, Class<T> clazz, Map<String, T> registry) {
        try (Reader reader = new FileReader(path.toFile())) {
            T item = gson.fromJson(reader, clazz);
            // Assuming both FishDefinition and RodComponentDefinition have a getName() via reflection or common interface would be better,
            // but for this phase we'll cast or check.
            String name = null;
            if (item instanceof FishDefinition) name = ((FishDefinition) item).getName();
            if (item instanceof RodComponentDefinition) name = ((RodComponentDefinition) item).getId();
            if (item instanceof LootDefinition) name = ((LootDefinition) item).getId();
            if (item instanceof com.masterangler.gear.AnglerArmor) {
                // Use filename as ID since AnglerArmor doesn't have an ID field yet
                String filename = path.getFileName().toString();
                name = filename.substring(0, filename.lastIndexOf('.'));
            }

            if (name != null) {
                registry.put(name.toLowerCase(), item);
                System.out.println("  - Loaded: " + name);
            }
        } catch (IOException e) {
            System.err.println("Failed to load from " + path);
            e.printStackTrace();
        }
    }

    public List<FishDefinition> getAllFish() {
        return new ArrayList<>(fishRegistry.values());
    }

    public FishDefinition getFish(String name) {
        return fishRegistry.get(name.toLowerCase());
    }

    public RodComponentDefinition getComponent(String id) {
        return componentRegistry.get(id.toLowerCase());
    }

    public Map<String, com.masterangler.gear.AnglerArmor> getAllArmor() {
        return armorRegistry;
    }

    public List<LootDefinition> getAllLoot() {
        return new ArrayList<>(lootRegistry.values());
    }
}
