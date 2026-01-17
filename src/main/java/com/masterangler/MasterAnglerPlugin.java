package com.masterangler;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.masterangler.mechanics.AfkFishingManager;
import com.masterangler.mechanics.FishingSessionManager;
import com.masterangler.mechanics.TensionManager;
import com.masterangler.progression.FishingPlayerLevelManager;
import com.masterangler.events.FishingEventHandler;
import com.masterangler.data.DataLoader;
import com.masterangler.data.PersistenceManager;
import com.masterangler.gear.AnglerArmorManager;
import com.masterangler.gear.FishingWorkbenchManager;
import com.masterangler.mechanics.LootTableManager;
import com.masterangler.mechanics.CrateLootManager;
import com.masterangler.mechanics.FishSpawnManager;
import com.masterangler.mechanics.RepairManager;
import com.masterangler.commands.CommandManager;

/**
 * Main plugin class for Hytale Master Angler.
 *
 * @author Jules
 * @version 1.0.0
 */
public class MasterAnglerPlugin extends JavaPlugin {

    private static MasterAnglerPlugin instance;
    private AfkFishingManager afkManager;

    public MasterAnglerPlugin(JavaPluginInit init) {
        super(init);
        instance = this;
    }

    @Override
    public void start() {
        System.out.println("[MasterAnglerPlugin] Plugin enabled!");

        // Initialize Managers
        FishingSessionManager sessionManager = new FishingSessionManager();
        TensionManager tensionManager = new TensionManager();
        FishingPlayerLevelManager levelManager = new FishingPlayerLevelManager();

        // Initialize Data
        DataLoader dataLoader = new DataLoader();
        dataLoader.loadFishDefinitions();
        dataLoader.loadComponentDefinitions();
        dataLoader.loadArmorDefinitions();
        dataLoader.loadLootDefinitions();
        dataLoader.loadCrateDefinitions();

        // Initialize Gameplay Managers
        AnglerArmorManager armorManager = new AnglerArmorManager();
        armorManager.setDataLoader(dataLoader);

        LootTableManager lootManager = new LootTableManager(dataLoader);
        CrateLootManager crateManager = new CrateLootManager(dataLoader);

        FishSpawnManager spawnManager = new FishSpawnManager(dataLoader);
        spawnManager.setArmorManager(armorManager);
        spawnManager.setLootManager(lootManager);
        spawnManager.setCrateManager(crateManager);

        PersistenceManager persistenceManager = new PersistenceManager();
        persistenceManager.setBaseDir(getDataDirectory());

        this.afkManager = new AfkFishingManager(spawnManager, levelManager);

        RepairManager repairManager = new RepairManager();
        FishingWorkbenchManager workbenchManager = new FishingWorkbenchManager();
        workbenchManager.setDataLoader(dataLoader);

        levelManager.setPersistenceManager(persistenceManager);

        // Register Event Handler
        FishingEventHandler eventHandler = new FishingEventHandler(
            sessionManager, tensionManager, levelManager
        );
        eventHandler.setSpawnManager(spawnManager);
        eventHandler.setAfkManager(afkManager);
        eventHandler.setWorkbenchManager(workbenchManager);
        eventHandler.setArmorManager(armorManager);

        // Trying registerGlobal for generic event handling logic based on error hint
        // Note: If PlayerInteractEvent is keyed, we might need a specific key, but "Global" usually covers all keys.
        getEventRegistry().registerGlobal(com.hypixel.hytale.server.core.event.events.player.PlayerInteractEvent.class, eventHandler::onPlayerInteract);

        // Initialize Commands
        CommandManager commandManager = new CommandManager(
            levelManager,
            workbenchManager,
            repairManager,
            armorManager
        );
        // Inject managers for simulation
        commandManager.setSessionManager(sessionManager);
        commandManager.setSpawnManager(spawnManager);

        getCommandRegistry().registerCommand(commandManager);

        // Start AFK Tick Loop
        // Using Hytale Scheduler instead of raw thread
        com.hypixel.hytale.server.core.HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(() -> {
             if (afkManager.isRunning()) {
                 afkManager.tick();
             }
        }, 0, 1, java.util.concurrent.TimeUnit.SECONDS);

        System.out.println("[MasterAnglerPlugin] Systems initialized.");
    }

    @Override
    public void shutdown() {
        System.out.println("[MasterAnglerPlugin] Plugin disabled!");

        if (afkManager != null) {
            afkManager.stop();
        }
    }

    public static MasterAnglerPlugin getInstance() {
        return instance;
    }
}
