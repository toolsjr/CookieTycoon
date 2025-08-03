package com.tools.cookieTycoon.listeners;

import com.tools.cookieTycoon.CookieTycoon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickListener implements Listener {
    private CookieTycoon plugin;

    public ClickListener(CookieTycoon plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void click(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            plugin.getCookieManager().add(p, 1 * plugin.getAchievementManager().getCombinedMultiplier(p, "cursor") + plugin.getAchievementManager().getCombinedTerm(p, "cursor"));
            plugin.getActionBar().update(p);
        }
    }

}
