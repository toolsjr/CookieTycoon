package com.tools.cookieTycoon.upgrades;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.utils.NumberFormatter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TimeMachine extends Upgrade {

    public TimeMachine(CookieTycoon plugin) {
        super(plugin);
    }

    @Override
    public ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName());
        meta.setLore(Arrays.asList(getLore(), "", "§7(ЛКМ) Купить x1 - " + NumberFormatter.format(getPrice(player)),
                "§7(ПКМ) Купить x10 - " + NumberFormatter.format(getPrice(player) * 20.303718238),
                "§7(СКМ) Купить x100 - " + NumberFormatter.format(getPrice(player) * 7828749.671335256)));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getInfoItem(Player player) {
        ItemStack item = new ItemStack(getItem(player).getType());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName() + " §7(куплено: " + NumberFormatter.format(getSum(player)) + ")");
        meta.setLore(Arrays.asList(getLore(),
                "",
                "§7• каждый " + getName().toLowerCase() + "§7 производит §f" + NumberFormatter.format(getMultiplier(player)/getSum(player)) + " печенья §7в секунду" + " (§f" + NumberFormatter.formatPercent(getMultiplier(player)/plugin.getCookieManager().getCPS(player)) + "§7 от",
                "§7  общего значения печ/с)",
                "§7• " + NumberFormatter.format(getSum(player)) + " " + getName().toLowerCase() + "§r§7 производят §f" + NumberFormatter.format(getMultiplier(player)) + " печенья §7в секунду",
                "§7• §fпеченья N §7изготовлено на данный момент"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getName() {
        return ChatColor.BOLD + "Машина времени";
    }

    @Override
    public String getLore() {
        return ChatColor.YELLOW + "Доставляет печенье из прошлого - ещё до того, как его съели.";
    }

    @Override
    public void usage(Player player) {
        plugin.getCookieManager().add(player, getMultiplier(player));
    }

    @Override
    public double getPrice(Player player) {
        return (14 * Math.pow(10, 12) * Math.pow(1.15, getSum(player))); // 14T = 14 * 10^12 * 1,15^N
    }

    @Override
    public double getSum(Player player) {
        return plugin.getUpgradesManager().get(player, "timemachine");
    }

    @Override
    public double getMultiplier(Player player) {
        return (65_000_000 * plugin.getAchievementManager().getCombinedMultiplier(player, "timemachine") + plugin.getAchievementManager().getCombinedTerm(player, "timemachine"))* getSum(player);
    }
}
