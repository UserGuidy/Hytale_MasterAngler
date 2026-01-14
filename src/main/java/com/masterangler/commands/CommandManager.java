package com.masterangler.commands;

import com.masterangler.mock.Player;
import com.masterangler.progression.FishingPlayerLevelManager;

import com.masterangler.gear.FishingWorkbenchManager;
import com.masterangler.mock.Item;
import com.masterangler.mock.ItemStack;

public class CommandManager {

    private FishingPlayerLevelManager levelManager;
    private FishingWorkbenchManager workbenchManager;
    private com.masterangler.mechanics.RepairManager repairManager;
    private com.masterangler.gear.AnglerArmorManager armorManager;

    public CommandManager(FishingPlayerLevelManager levelManager,
                          FishingWorkbenchManager workbenchManager,
                          com.masterangler.mechanics.RepairManager repairManager,
                          com.masterangler.gear.AnglerArmorManager armorManager) {
        this.levelManager = levelManager;
        this.workbenchManager = workbenchManager;
        this.repairManager = repairManager;
        this.armorManager = armorManager;
    }

    public void onCommand(Player sender, String command, String[] args) {
        if (!command.equalsIgnoreCase("angler")) return;

        if (args.length == 0) {
            System.out.println("Usage: /angler <level|xp|rod>");
            return;
        }

        String subCommand = args[0];

        switch (subCommand.toLowerCase()) {
            case "level":
                if (args.length < 2) return;
                try {
                    int level = Integer.parseInt(args[1]);
                    sender.setLevel(level);
                    System.out.println("Set level to " + level);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
                break;

            case "equip":
                if (args.length < 2) return;
                String armorId = args[1];
                if (armorManager != null) {
                    armorManager.equipArmorById(sender, armorId);
                }
                break;

            case "repair":
                // Simulating repair of currently held rod
                // In a real scenario, retrieve rod from Item in hand metadata
                // For mock, we'll try to find an active session or just print mock message
                // However, repair manager needs a DynamicRod instance.
                // We'll mock a default rod repair for now to prove connection.
                if (repairManager != null) {
                    System.out.println("Attempting to repair rod (Mock)...");
                    // Mock rod for repair command
                     com.masterangler.gear.DynamicRod rod = workbenchManager.assembleRod(
                         new com.masterangler.gear.RodBody(0.0f, 1f, 50f), // Broken rod
                         new com.masterangler.gear.RodLine(20f, 0.5f, 10f),
                         new com.masterangler.gear.RodReel(5f, 1f),
                         new com.masterangler.gear.RodBait(1f, 1f)
                    );
                    repairManager.repairRod(rod, sender);
                }
                break;

            case "xp":
                if (args.length < 2) return;
                try {
                    int xp = Integer.parseInt(args[1]);
                    levelManager.addXp(sender, xp);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
                break;

            case "rod":
                System.out.println("Gave default rod to " + sender.getName());
                // In real impl: Add item to inventory
                break;

            case "workbench":
                // Simulate crafting with mock items for now
                System.out.println("Opening mock workbench...");

                // These names must match the IDs in the JSON files we created
                ItemStack body = new ItemStack(new Item("fiberglass_body"), 1);
                ItemStack line = new ItemStack(new Item("braided_line"), 1);
                ItemStack reel = new ItemStack(new Item("high_speed_reel"), 1);
                ItemStack bait = new ItemStack(new Item("worm_bait"), 1);

                workbenchManager.validateAndCraft(body, line, reel, bait);
                break;

            default:
                System.out.println("Unknown subcommand: " + subCommand);
        }
    }
}
