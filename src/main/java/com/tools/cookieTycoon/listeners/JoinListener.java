package com.tools.cookieTycoon.listeners;

import com.tools.cookieTycoon.CookieTycoon;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {
    private final CookieTycoon plugin;

    public JoinListener(CookieTycoon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void join(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        plugin.getActionBar().start(p);
        p.setGameMode(GameMode.ADVENTURE);
        e.setJoinMessage(null);
        plugin.getScoreboard().start(p);
        plugin.getUpgradesManager().start(p);
        plugin.getTimeTracker().onPlayerLogin(p);
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        plugin.getTimeTracker().onPlayerLogout(p);
        e.setQuitMessage(null);
    }
}
