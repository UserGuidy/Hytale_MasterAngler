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
