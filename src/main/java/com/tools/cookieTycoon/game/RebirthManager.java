package com.tools.cookieTycoon.game;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.utils.DatabaseManager;
import com.tools.cookieTycoon.utils.NumberFormatter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RebirthManager {
    private CookieTycoon plugin;
    private DatabaseManager database;

    public RebirthManager(CookieTycoon plugin, DatabaseManager database) {
        this.plugin = plugin;
        this.database = database;
    }

    public void add(Player player, int num) {
        int current = get(player);
        database.setRebirth(player, current + num);
    }

    public void remove(Player player, int num) {
        int current = get(player);
        database.setRebirth(player, current - num);
    }

    public void set(Player player, int num) {
        database.setRebirth(player, num);
    }

    public int get(Player player) {
        return database.getRebirth(player);
    }

    public void buy(Player player) {
        if (isAvailable(player)) {
            plugin.getCookieManager().wipe(player);
            plugin.getUpgradesManager().wipe(player);
            plugin.getTimeTracker().onRebirth(player);
            add(player, 1);
            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            player.sendTitle("§6Престиж повышен!", "§eВаш уровень §f- §a" + plugin.getRebirthManager().get(player));
        } else
            player.sendMessage("§cВам не хватает " + (NumberFormatter.format(getPrice(player) - plugin.getCookieManager().getAll(player))) + " до следующего уровня.");
    }

    public double getPrice(Player player) {
        return Math.pow(10, 12) * Math.pow(1 + get(player), 3);
    }

    public boolean isAvailable(Player player) {
        return plugin.getCookieManager().getAll(player) >= getPrice(player);
    }

}
