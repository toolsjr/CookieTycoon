package com.tools.cookieTycoon.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    private static final List<String> UPGRADE_TYPES = Arrays.asList(
            "cursor", "granny", "farm", "mine", "factory",
            "bank", "temple", "tower", "shipment", "lab",
            "portal", "timemachine", "condenser", "prism",
            "chancemaker", "engine", "console", "idleverse",
            "cortexbaker", "you"
    );

    private static final List<String> CMD_TYPES = Arrays.asList(
            "add", "set", "remove", "wipe");


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("ck") || command.getName().equalsIgnoreCase("upgrade")) {
            if (args.length == 1) {
                StringUtil.copyPartialMatches(args[0], CMD_TYPES, completions);
            } else if (args.length == 2 && CMD_TYPES.contains(args[0].toLowerCase())) {
                return null;
            } else if (args.length == 3 && command.getName().equalsIgnoreCase("upgrade")) {
                StringUtil.copyPartialMatches(args[2], UPGRADE_TYPES, completions);
            }
        }

        return completions.isEmpty() ? Collections.emptyList() : completions;
    }
}
