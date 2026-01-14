package com.masterangler.commands;

import com.masterangler.mock.Player;
import com.masterangler.progression.FishingPlayerLevelManager;

public class CommandManager {

    private FishingPlayerLevelManager levelManager;

    public CommandManager(FishingPlayerLevelManager levelManager) {
        this.levelManager = levelManager;
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

            default:
                System.out.println("Unknown subcommand: " + subCommand);
        }
    }
}
