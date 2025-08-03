package com.tools.cookieTycoon.utils;

import com.tools.cookieTycoon.CookieTycoon;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ActionBar {
    private final CookieTycoon plugin;
    private BukkitTask updateTask;

    public ActionBar(CookieTycoon plugin) {
        this.plugin = plugin;
    }

    public void start(Player p) {
        stop(p);

        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update(p);
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    public void stop(Player p) {
        if (updateTask != null) {
            updateTask.cancel();
            clear(p);
        }
    }

    public void update(Player p) {
        clear(p);
        send(p, ChatColor.GOLD + ChatColor.BOLD.toString() + NumberFormatter.format(plugin.getCookieManager().get(p)) + " ⃝");
    }

    private void clear(Player p) {
        send(p, "");
    }

    private void send(Player p, String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(msg));
    }

}
