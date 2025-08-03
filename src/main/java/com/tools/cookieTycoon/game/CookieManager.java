package com.tools.cookieTycoon.game;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.utils.DatabaseManager;
import org.bukkit.entity.Player;

public class CookieManager {
    private CookieTycoon plugin;
    public final DatabaseManager database;

    public CookieManager(CookieTycoon plugin, DatabaseManager database) {
        this.plugin = plugin;
        this.database = database;

    }

    public double get(Player player) {
        return database.getCookies(player.getUniqueId().toString());
    }

    public double getCPS(Player player) {
        return plugin.getUpgradesManager().getCursor().getMultiplier(player) +
                plugin.getUpgradesManager().getGranny().getMultiplier(player) +
                plugin.getUpgradesManager().getFarm().getMultiplier(player) +
                plugin.getUpgradesManager().getMine().getMultiplier(player) +
                plugin.getUpgradesManager().getFactory().getMultiplier(player) +
                plugin.getUpgradesManager().getBank().getMultiplier(player) +
                plugin.getUpgradesManager().getTemple().getMultiplier(player) +
                plugin.getUpgradesManager().getTower().getMultiplier(player) +
                plugin.getUpgradesManager().getShipment().getMultiplier(player) +
                plugin.getUpgradesManager().getLab().getMultiplier(player) +
                plugin.getUpgradesManager().getPortal().getMultiplier(player) +
                plugin.getUpgradesManager().getTimeMachine().getMultiplier(player) +
                plugin.getUpgradesManager().getCondenser().getMultiplier(player) +
                plugin.getUpgradesManager().getPrism().getMultiplier(player) +
                plugin.getUpgradesManager().getChancemaker().getMultiplier(player) +
                plugin.getUpgradesManager().getEngine().getMultiplier(player) +
                plugin.getUpgradesManager().getConsole().getMultiplier(player) +
                plugin.getUpgradesManager().getIdleverse().getMultiplier(player) +
                plugin.getUpgradesManager().getCortexBaker().getMultiplier(player) +
                plugin.getUpgradesManager().getYou().getMultiplier(player);

    }

    public void click(Player player) {
        double cookies = 1;
        add(player, cookies);
    }

    public void set(Player player, double cookies) {
        database.setCookies(player.getUniqueId().toString(), cookies);
    }

    public void add(Player player, double amount) {
        double current = get(player);
        set(player, current + amount);
        addAll(player, amount);
    }

    public void remove(Player player, double amount) {
        double current = get(player);
        set(player, current - amount);
    }

    public double getAll(Player player) {
        return database.getAllCookies(player.getUniqueId().toString());
    }

    public void setAll(Player player, double cookies) {
        database.setAllCookies(player.getUniqueId().toString(), cookies);
    }

    public void addAll(Player player, double amount) {
        double current = getAll(player);
        setAll(player, current + amount);
    }

    public void removeAll(Player player, double amount) {
        double current = getAll(player);
        setAll(player, current - amount);
    }

    public void wipe(Player player) {
        set(player, 0);
    }
}

