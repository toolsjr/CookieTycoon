package com.tools.cookieTycoon.utils;

import com.tools.cookieTycoon.CookieTycoon;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class ScoreboardManager {
    private CookieTycoon plugin;
    private BukkitTask updateTask;

    public ScoreboardManager (CookieTycoon plugin) {
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

    public void update(Player player) {

        Scoreboard board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective obj = board.registerNewObjective("cookietycoon", "dummy", "§6§lCOOKIE TYCOON!");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        obj.getScore(ChatColor.GRAY + "").setScore(0);
        obj.getScore("§fПеченья в секунду: §6" + NumberFormatter.format(plugin.getCookieManager().getCPS(player))).setScore(-1);
        obj.getScore("§fПеченье за всё время: §6" + NumberFormatter.format(plugin.getCookieManager().getAll(player))).setScore(-2);
        obj.getScore("§f").setScore(-3);
        obj.getScore("§fУровень престижа: §6" + NumberFormatter.format(plugin.getRebirthManager().get(player))).setScore(-4);
        if (plugin.getRebirthManager().isAvailable(player)) {
        obj.getScore("§aДоступно возвышение!").setScore(-5);
        } else obj.getScore("§fДо следующего: §6" + NumberFormatter.format((plugin.getRebirthManager().getPrice(player))-plugin.getCookieManager().getAll(player))).setScore(-5);
        obj.getScore("").setScore(-6);
        obj.getScore("§7t.me/tools_development").setScore(-7);

        player.setScoreboard(board);

    }

    public void clear(Player player) {
        Scoreboard board = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        player.setScoreboard(board);

    }

}
