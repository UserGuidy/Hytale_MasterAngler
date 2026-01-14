package com.masterangler;

/**
 * Main plugin class for Hytale Master Angler.
 *
 * @author Jules
 * @version 1.0.0
 */
public class MasterAnglerPlugin {

    private static MasterAnglerPlugin instance;
    private com.masterangler.mechanics.AfkFishingManager afkManager;

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
        dataLoader.loadComponentDefinitions();
        dataLoader.loadArmorDefinitions();

        // Initialize Gameplay Managers
        com.masterangler.gear.AnglerArmorManager armorManager = new com.masterangler.gear.AnglerArmorManager();
        armorManager.setDataLoader(dataLoader);

        com.masterangler.mechanics.FishSpawnManager spawnManager = new com.masterangler.mechanics.FishSpawnManager(dataLoader);
        spawnManager.setArmorManager(armorManager); // Connect Armor to Spawning

        com.masterangler.data.PersistenceManager persistenceManager = new com.masterangler.data.PersistenceManager();
        this.afkManager = new com.masterangler.mechanics.AfkFishingManager(spawnManager, levelManager);

        // Repair Manager
        com.masterangler.mechanics.RepairManager repairManager = new com.masterangler.mechanics.RepairManager();

        // Update Event Handler with Managers
        eventHandler.setSpawnManager(spawnManager);
        eventHandler.setAfkManager(afkManager);

        // Inject Persistence into Level Manager (Manual wiring for Phase 2)
        levelManager.setPersistenceManager(persistenceManager);

        // Inject DataLoader into Workbench
        com.masterangler.gear.FishingWorkbenchManager workbenchManager = new com.masterangler.gear.FishingWorkbenchManager();
        workbenchManager.setDataLoader(dataLoader);

        // Update Event Handler with shared Workbench Manager
        eventHandler.setWorkbenchManager(workbenchManager);

        // Initialize Commands
        com.masterangler.commands.CommandManager commandManager = new com.masterangler.commands.CommandManager(
            levelManager,
            workbenchManager,
            repairManager,
            armorManager
        );
        // commandRegistry.register("angler", commandManager); // Mock registration

        // Start AFK Tick Loop (Mock Thread)
        new Thread(() -> {
            while (afkManager.isRunning()) {
                try {
                    Thread.sleep(1000);
                    afkManager.tick();
                } catch (InterruptedException e) {
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

        if (afkManager != null) {
            afkManager.stop();
        }
    }

    /**
     * Get plugin instance.
     */
    public static MasterAnglerPlugin getInstance() {
        return instance;
    }
}
