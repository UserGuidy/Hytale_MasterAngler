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
        
        // TODO: Initialize your plugin here
        // - Load configuration
        // - Register event listeners
        // - Register commands
        // - Start services
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
