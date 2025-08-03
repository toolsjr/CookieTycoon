package com.tools.cookieTycoon.utils;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.achievements.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {
    private final CookieTycoon plugin;

    public CommandManager(CookieTycoon plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("ck")) {
            if (args.length == 0) {
                sender.sendMessage("§cИспользуйте: /ck <add|set|remove|wipe> <ник> [кол-во]");
                return true;
            }

            String subCommand = args[0].toLowerCase();

            switch (subCommand) {
                case "add":
                    return handleAdd(sender, args);
                case "set":
                    return handleSet(sender, args);
                case "remove":
                    return handleRemove(sender, args);
                case "wipe":
                    return handleWipe(sender, args);
                default:
                    sender.sendMessage("§cНеизвестная команда. Доступные: add, set, remove, wipe");
                    return true;
            }
        } else if (command.getName().equalsIgnoreCase("ach")) {
            return handleAch(sender, args);
        } else if (command.getName().equalsIgnoreCase("info")) {
            return handleInfo(sender, args);
        } else if (command.getName().equalsIgnoreCase("menu")) {
            return handleMenu(sender);
        } else if (command.getName().equalsIgnoreCase("rebirth")) {
            return handleRebirth(sender);
        } else if (command.getName().equalsIgnoreCase("upgrade")) {
            if (args.length == 0) {
                sender.sendMessage("§cИспользуйте: /upgrade <add|set|remove|wipe> <ник> [кол-во]");
                return true;
            }

            String subCommand = args[0].toLowerCase();

            switch (subCommand) {
                case "add":
                    return handleAddUpgrade(sender, args);
                case "set":
                    return handleSetUpgrade(sender, args);
                case "remove":
                    return handleRemoveUpgrade(sender, args);
                case "wipe":
                    return handleWipeUpgrade(sender, args);
                default:
                    sender.sendMessage("§cНеизвестная команда. Доступные: add, set, remove, wipe");
                    return true;
            }
        }

        return false;
    }

    private boolean handleAch(CommandSender sender, String[] args) {

        if (sender instanceof Player player) {
            plugin.getGui().ach(player);
        }
        return true;
    }

    private boolean handleMenu(CommandSender sender) {
        if (sender instanceof Player player) {
            plugin.getGui().upgrades(player);
        }
        return true;
    }

    private boolean handleRebirth(CommandSender sender) {
        if (sender instanceof Player player) {
            plugin.getGui().rebirth(player);
        }
        return true;
    }

    private boolean handleInfo(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cИспользуйте: /info <ник>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }
        if (sender instanceof Player player) {
            plugin.getGui().info(player, target);
        }
        return true;
    }

    private boolean handleAdd(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользуйте: /ck add <ник> <кол-во>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        try {
            double amount = Double.parseDouble(args[2]);
            if (amount <= 0.1) {
                sender.sendMessage("§cКоличество должно быть положительным!");
                return true;
            }

            plugin.getCookieManager().add(Bukkit.getPlayer(args[1]), Double.parseDouble(args[2]));
            sender.sendMessage("§aДобавлено §6" + NumberFormatter.format(amount) + "§a печенек игроку §6" + target.getName());
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (e instanceof NumberFormatException) {
                sender.sendMessage("§cНекорректное количество печенек!");
            } else sender.sendMessage("§cВведите количество печенек!");
            return true;
        }
    }

    private boolean handleSet(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользуйте: /ck set <ник> <кол-во>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        try {
            double amount = Double.parseDouble(args[2]);
            if (amount < 0) {
                sender.sendMessage("§cКоличество не может быть отрицательным!");
                return true;
            }

            plugin.getCookieManager().set(Bukkit.getPlayer(args[1]), Double.parseDouble(args[2]));
            sender.sendMessage("§aУстановлено §6" + NumberFormatter.format(amount) + " §aпеченек игроку §6" + target.getName());
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (e instanceof NumberFormatException) {
                sender.sendMessage("§cНекорректное количество печенек!");
            } else sender.sendMessage("§cВведите количество печенек!");
            return true;
        }
    }

    private boolean handleRemove(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользуйте: /ck remove <ник> <кол-во>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        try {
            double amount = Double.parseDouble(args[2]);
            if (amount <= 0.1) {
                sender.sendMessage("§cКоличество должно быть положительным!");
                return true;
            }

            plugin.getCookieManager().remove(Bukkit.getPlayer(args[1]), Double.parseDouble(args[2]));
            sender.sendMessage("§aУдалено §6" + NumberFormatter.format(amount) + " §aпеченек у игрока §6" + target.getName());
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (e instanceof NumberFormatException) {
                sender.sendMessage("§cНекорректное количество печенек!");
            } else sender.sendMessage("§cВведите количество печенек!");
            return true;
        }
    }

    private boolean handleWipe(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользуйте: /ck wipe <ник>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        plugin.getCookieManager().wipe(Bukkit.getPlayer(args[1]));
        sender.sendMessage("§aПеченьки игрока §6" + target.getName() + "§a обнулены");
        return true;
    }

    private boolean handleSetUpgrade(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cИспользуйте: /upgrade set <ник> <апгрейд> <кол-во>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        if (!plugin.getUpgradesManager().getUpgrades().containsKey(args[2])) {
            sender.sendMessage("§cНеизвестный апгрейд. Существующие: §e" + String.join("§7, §e", plugin.getUpgradesManager().getUpgrades().keySet()));
            return true;
        }

        try {
            int amount = Integer.parseInt(args[3]);
            if (amount < 0) {
                sender.sendMessage("§cКоличество не может быть отрицательным!");
                return true;
            }

            plugin.getUpgradesManager().set(Bukkit.getPlayer(args[1]), args[2], Integer.parseInt(args[3]));
            sender.sendMessage("§aУстановлено §6" + amount + " §a" + args[2] + " игроку §6" + target.getName());
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (e instanceof NumberFormatException) {
                sender.sendMessage("§cНекорректное количество!");
            } else sender.sendMessage("§cВведите количество!");
            return true;
        }
    }

    private boolean handleAddUpgrade(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cИспользуйте: /upgrade add <ник> <апгрейд> <кол-во>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        try {
            int amount = Integer.parseInt(args[3]);
            if (amount < 0) {
                sender.sendMessage("§cКоличество не может быть отрицательным!");
                return true;
            }

            plugin.getUpgradesManager().add(Bukkit.getPlayer(args[1]), args[2], Integer.parseInt(args[3]));
            sender.sendMessage("§aДобавлено §6" + amount + " " + args[2] + "§a игроку §6" + target.getName());
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (e instanceof NumberFormatException) {
                sender.sendMessage("§cНекорректное количество!");
            } else sender.sendMessage("§cВведите количество!");
            return true;
        }
    }

    private boolean handleRemoveUpgrade(CommandSender sender, String[] args) {
        if (args.length < 3) {
            sender.sendMessage("§cИспользуйте: /upgrade remove <ник> <апгрейд> <кол-во>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        try {
            int amount = Integer.parseInt(args[3]);
            if (amount < 0) {
                sender.sendMessage("§cКоличество не может быть отрицательным!");
                return true;
            }

            plugin.getUpgradesManager().add(Bukkit.getPlayer(args[1]), args[2], Integer.parseInt(args[3]));
            sender.sendMessage("§aУдалено §6" + amount + " §a" + args[2] + " у игрока §6" + target.getName());
            return true;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            if (e instanceof NumberFormatException) {
                sender.sendMessage("§cНекорректное количество!");
            } else sender.sendMessage("§cВведите количество!");
            return true;
        }
    }

    private boolean handleWipeUpgrade(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cИспользуйте: /upgrade wipe <ник>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден!");
            return true;
        }

        plugin.getUpgradesManager().wipe(Bukkit.getPlayer(args[1]));
        sender.sendMessage("§aУлучшения игрока §6" + target.getName() + "§a обнулены");
        return true;
    }
}
