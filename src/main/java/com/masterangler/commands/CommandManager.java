package com.masterangler.commands;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.masterangler.progression.FishingPlayerLevelManager;
import com.masterangler.gear.FishingWorkbenchManager;
import java.util.concurrent.CompletableFuture;

public class CommandManager extends AbstractCommand {

    private FishingPlayerLevelManager levelManager;
    private FishingWorkbenchManager workbenchManager;
    private com.masterangler.mechanics.RepairManager repairManager;
    private com.masterangler.gear.AnglerArmorManager armorManager;

    public CommandManager(FishingPlayerLevelManager levelManager,
                          FishingWorkbenchManager workbenchManager,
                          com.masterangler.mechanics.RepairManager repairManager,
                          com.masterangler.gear.AnglerArmorManager armorManager) {
        super("angler", "Master Angler Commands");
        setAllowsExtraArguments(true); // Allow subcommands/arguments
        this.levelManager = levelManager;
        this.workbenchManager = workbenchManager;
        this.repairManager = repairManager;
        this.armorManager = armorManager;
    }

    @Override
    public CompletableFuture<Void> execute(CommandContext context) {
        CommandSender sender = context.sender();
        // Parse arguments from input string.
        // Input string usually contains the full command "angler sub arg1 arg2" or just args depending on impl.
        // Assuming we need to split manually. If args are not parsed, we do it here.
        // If getInputString returns "angler sub arg", splitting by space is safe.
        String input = context.getInputString();
        String[] parts = input.split(" ");
        // If parts[0] is "angler", then args start at 1.

        String[] args;
        if (parts.length > 1) {
            args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, parts.length - 1);
        } else {
            args = new String[0];
        }

        if (args.length == 0) {
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§e--- Master Angler Commands ---"));
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§b/angler level <int> §7- Set fishing level (Debug)"));
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§b/angler xp <int> §7- Add fishing XP"));
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§b/angler rod §7- Get a debug fishing rod"));
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§b/angler equip <id> §7- Equip angler armor"));
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§b/angler workbench §7- Open fishing workbench"));
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§b/angler repair §7- Repair held rod"));
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§cNote: Most commands require OP permission."));
            return CompletableFuture.completedFuture(null);
        }

        String subCommand = args[0];
        Player player = null;

        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("This command is for players only."));
            return CompletableFuture.completedFuture(null);
        }

        switch (subCommand.toLowerCase()) {
            case "level":
                if (args.length < 2) break;
                try {
                    int level = Integer.parseInt(args[1]);
                    levelManager.setLevel(player, level);
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Set fishing level to " + level));
                } catch (NumberFormatException e) {
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Invalid number"));
                }
                break;

            case "equip":
                if (args.length < 2) break;
                String armorId = args[1];
                if (armorManager != null) {
                    armorManager.equipArmorById(player, armorId);
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Equipping armor: " + armorId));
                }
                break;

            case "repair":
                if (repairManager != null) {
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Attempting to repair rod..."));
                     com.masterangler.gear.DynamicRod rod = workbenchManager.assembleRod(
                         new com.masterangler.gear.RodBody(0.0f, 1f, 50f, 1.0f, "#FFF"),
                         new com.masterangler.gear.RodLine(20f, 0.5f, 10f),
                         new com.masterangler.gear.RodReel(5f, 1f, "#FFF"),
                         new com.masterangler.gear.RodBait("worm", 1f, 1f)
                    );
                    repairManager.repairRod(rod, player);
                }
                break;

            case "xp":
                if (args.length < 2) break;
                try {
                    int xp = Integer.parseInt(args[1]);
                    levelManager.addXp(player, xp);
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Added " + xp + " XP."));
                } catch (NumberFormatException e) {
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Invalid number"));
                }
                break;

            case "rod":
                sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Gave rod and components to " + player.getDisplayName()));

                // Give basic components to allow testing crafting.
                // Using namespaced IDs to ensure Hytale recognizes them from the asset pack.
                player.getInventory().getHotbar().addItemStack(new ItemStack("master_angler:fishing_rod", 1));
                player.getInventory().getHotbar().addItemStack(new ItemStack("master_angler:fiberglass_body", 1));
                player.getInventory().getHotbar().addItemStack(new ItemStack("master_angler:braided_line", 1));
                player.getInventory().getHotbar().addItemStack(new ItemStack("master_angler:high_speed_reel", 1));
                player.getInventory().getHotbar().addItemStack(new ItemStack("master_angler:worm_bait", 5));
                break;

            case "workbench":
                sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Opening workbench logic... checking inventory..."));

                // Basic workbench simulation: check for components in inventory
                // Components needed: Body, Line, Reel.
                // We'll search for specific items for this demo: fiberglass_body, braided_line, high_speed_reel

                String bodyId = "master_angler:fiberglass_body";
                String lineId = "master_angler:braided_line";
                String reelId = "master_angler:high_speed_reel";

                Inventory inv = player.getInventory();
                ItemStack bodyStack = null;
                ItemStack lineStack = null;
                ItemStack reelStack = null;

                // Helper to find item in inventory
                // Searching hotbar
                for (int i = 0; i < inv.getHotbar().getSize(); i++) {
                     ItemStack s = inv.getHotbar().getItem(i);
                     if (s != null && !s.isEmpty()) {
                         if (s.getItemId().equals(bodyId) && bodyStack == null) bodyStack = s;
                         if (s.getItemId().equals(lineId) && lineStack == null) lineStack = s;
                         if (s.getItemId().equals(reelId) && reelStack == null) reelStack = s;
                     }
                }

                // Searching storage if not found
                if (bodyStack == null || lineStack == null || reelStack == null) {
                     for (int i = 0; i < inv.getStorage().getSize(); i++) {
                         ItemStack s = inv.getStorage().getItem(i);
                         if (s != null && !s.isEmpty()) {
                             if (s.getItemId().equals(bodyId) && bodyStack == null) bodyStack = s;
                             if (s.getItemId().equals(lineId) && lineStack == null) lineStack = s;
                             if (s.getItemId().equals(reelId) && reelStack == null) reelStack = s;
                         }
                     }
                }

                if (bodyStack != null && lineStack != null && reelStack != null) {
                    // Consume 1 of each
                    bodyStack.setAmount(bodyStack.getAmount() - 1);
                    lineStack.setAmount(lineStack.getAmount() - 1);
                    reelStack.setAmount(reelStack.getAmount() - 1);

                    // Explicitly clear empty stacks if the API requires it (Safe practice)
                    if (bodyStack.getAmount() <= 0) inv.removeItem(bodyStack);
                    if (lineStack.getAmount() <= 0) inv.removeItem(lineStack);
                    if (reelStack.getAmount() <= 0) inv.removeItem(reelStack);

                    // Craft Rod
                    com.masterangler.gear.DynamicRod dynamicRod = workbenchManager.validateAndCraft(
                         new ItemStack(bodyId),
                         new ItemStack(lineId),
                         new ItemStack(reelId),
                         null // No bait needed for craft
                    );

                    if (dynamicRod != null) {
                         ItemStack rodItem = new ItemStack("master_angler:fishing_rod", 1);
                         inv.getHotbar().addItemStack(rodItem);
                         sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§aCrafting successful! Received Fishing Rod."));
                    } else {
                         sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§cCrafting logic failed validation."));
                    }
                } else {
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("§cMissing components! You need: Fiberglass Body, Braided Line, High Speed Reel."));
                }
                break;

            default:
                sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Unknown subcommand: " + subCommand));
        }

        return CompletableFuture.completedFuture(null);
    }
}
