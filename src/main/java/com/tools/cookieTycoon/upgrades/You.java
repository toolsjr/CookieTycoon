package com.tools.cookieTycoon.upgrades;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.utils.NumberFormatter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class You extends Upgrade {

    public You(CookieTycoon plugin) {
        super(plugin);
    }

    @Override
    public ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName() + player.getDisplayName());
        meta.setLore(Arrays.asList(getLore(), getLore2(), getLore3(), "", "§7(ЛКМ) Купить x1 - " + NumberFormatter.format(getPrice(player)),
                "§7(ПКМ) Купить x10 - " + NumberFormatter.format(getPrice(player) * 20.303718238),
                "§7(СКМ) Купить x100 - " + NumberFormatter.format(getPrice(player) * 7828749.671335256)));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public ItemStack getInfoItem(Player player) {
        ItemStack item = new ItemStack(getItem(player).getType());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getName() + player.getDisplayName() + " §7(куплено: " + NumberFormatter.format(getSum(player)) + ")");
        meta.setLore(Arrays.asList(getLore(),
                "",
                "§7• каждый " + getName().toLowerCase() + player.getDisplayName() + "§7 производит §f" + NumberFormatter.format(getMultiplier(player)/getSum(player)) + " печенья §7в секунду" ,
                "§7• " + NumberFormatter.format(getSum(player)) + " " + getName().toLowerCase() + player.getDisplayName() + "§r§7 производят §f" + NumberFormatter.format(getMultiplier(player)) + " печенья §7в секунду",
                "§7• §fпеченья N §7изготовлено на данный момент"));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getName() {
        return ChatColor.BOLD + "";
    }

    @Override
    public String getLore() {
        return ChatColor.YELLOW + "Всё это печенье существует только благодаря вам. ";
    }

    public String getLore2() {
        return ChatColor.YELLOW + "Вы подумали, что, будь вас больше, вы";
    }

    public String getLore3() {
        return ChatColor.YELLOW + "смогли бы и заработать больше.";
    }

    @Override
    public void usage(Player player) {
        plugin.getCookieManager().add(player, getMultiplier(player));
    }

    @Override
    public double getPrice(Player player) {
        return (540 * Math.pow(10, 24) * Math.pow(1.15, getSum(player))); // 540Spt == 540 * 10^24 * 1,15^N
    }

    @Override
    public double getSum(Player player) {
        return plugin.getUpgradesManager().get(player, "you");
    }

    @Override
    public double getMultiplier(Player player) {
        return (510 * Math.pow(10, 12) * plugin.getAchievementManager().getCombinedMultiplier(player, "you") + plugin.getAchievementManager().getCombinedTerm(player, "you"))* getSum(player); // 510Т
    }
}
