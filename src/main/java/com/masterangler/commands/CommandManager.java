package com.masterangler.commands;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.inventory.ItemStack;
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
                    sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Set level to " + level + " (Mock Action)"));
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
                sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Gave default rod to " + player.getLegacyDisplayName()));
                // In real impl: Add item to inventory using player.getInventory().addItem(...)
                break;

            case "workbench":
                sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Opening mock workbench..."));

                // Real ItemStacks require defined ItemTypes or IDs
                ItemStack body = new ItemStack("fiberglass_body");
                ItemStack line = new ItemStack("braided_line");
                ItemStack reel = new ItemStack("high_speed_reel");
                ItemStack bait = new ItemStack("worm_bait");

                workbenchManager.validateAndCraft(body, line, reel, bait);
                break;

            default:
                sender.sendMessage(com.hypixel.hytale.server.core.Message.raw("Unknown subcommand: " + subCommand));
        }

        return CompletableFuture.completedFuture(null);
    }
}
