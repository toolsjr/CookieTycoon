package com.tools.cookieTycoon.upgrades;

import com.tools.cookieTycoon.CookieTycoon;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class Upgrade {
    protected final CookieTycoon plugin;

    public Upgrade(CookieTycoon plugin) {
        this.plugin = plugin;
    }

    public abstract ItemStack getItem(Player player);

    public abstract ItemStack getInfoItem(Player player);

    public abstract String getName();

    public abstract String getLore();

    public abstract void usage(Player player);

    public abstract double getPrice(Player player);

    public abstract double getSum(Player player);

    public abstract double getMultiplier(Player player);

}