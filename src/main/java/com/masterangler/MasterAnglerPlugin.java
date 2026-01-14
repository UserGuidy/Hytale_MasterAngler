package com.masterangler;

/**
 * Main plugin class for Hytale Master Angler.
 *
 * @author Jules
 * @version 1.0.0
 */
public class MasterAnglerPlugin {

    private static MasterAnglerPlugin instance;

    /**
     * Constructor - Called when plugin is loaded.
     */
    public MasterAnglerPlugin() {
        instance = this;
        System.out.println("[MasterAnglerPlugin] Plugin loaded!");
    }

    /**
     * Called when plugin is enabled.
     */
    public void onEnable() {
        System.out.println("[MasterAnglerPlugin] Plugin enabled!");

        // Initialize managers
        com.masterangler.mock.ServerContext serverContext = new com.masterangler.mock.ServerContext();
        com.masterangler.mechanics.FishingSessionManager sessionManager = new com.masterangler.mechanics.FishingSessionManager();
        com.masterangler.mechanics.TensionManager tensionManager = new com.masterangler.mechanics.TensionManager();
        com.masterangler.progression.FishingPlayerLevelManager levelManager = new com.masterangler.progression.FishingPlayerLevelManager();

        // Register Event Handler
        com.masterangler.events.FishingEventHandler eventHandler = new com.masterangler.events.FishingEventHandler(
            sessionManager, tensionManager, levelManager, serverContext
        );

        com.masterangler.mock.EventBus eventBus = new com.masterangler.mock.EventBus();
        eventBus.register(eventHandler);

        // Initialize Data
        com.masterangler.data.DataLoader dataLoader = new com.masterangler.data.DataLoader();
        dataLoader.loadFishDefinitions();

        // Initialize Gameplay Managers
        com.masterangler.mechanics.FishSpawnManager spawnManager = new com.masterangler.mechanics.FishSpawnManager(dataLoader);
        com.masterangler.data.PersistenceManager persistenceManager = new com.masterangler.data.PersistenceManager();
        com.masterangler.mechanics.AfkFishingManager afkManager = new com.masterangler.mechanics.AfkFishingManager(spawnManager, levelManager);

        // Update Event Handler with Managers
        eventHandler.setSpawnManager(spawnManager);
        eventHandler.setAfkManager(afkManager);

        // Inject Persistence into Level Manager (Manual wiring for Phase 2)
        levelManager.setPersistenceManager(persistenceManager);

        // Initialize Commands
        com.masterangler.commands.CommandManager commandManager = new com.masterangler.commands.CommandManager(levelManager);
        // commandRegistry.register("angler", commandManager); // Mock registration

        // Start AFK Tick Loop (Mock Thread)
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    afkManager.tick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();

        System.out.println("[MasterAnglerPlugin] Systems initialized.");
    }

    /**
     * Called when plugin is disabled.
     */
    public void onDisable() {
        System.out.println("[MasterAnglerPlugin] Plugin disabled!");

        // TODO: Cleanup your plugin here
        // - Save data
        // - Stop services
        // - Close connections
    }

    /**
     * Get plugin instance.
     */
    public static MasterAnglerPlugin getInstance() {
        return instance;
    }
}
