package com.tools.cookieTycoon.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tools.cookieTycoon.CookieTycoon;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TimeTracker {
    private BukkitTask updateTask;
    private CookieTycoon plugin;
    private final File dataFile;
    private final Gson gson;
    private Map<UUID, PlayerPlayTime> playTimeData;

    private static class PlayerPlayTime {
        long totalPlayTime;
        long currentRebirthPlayTime;
        long lastLoginTime;

        public PlayerPlayTime() {
            this.totalPlayTime = 0;
            this.currentRebirthPlayTime = 0;
            this.lastLoginTime = System.currentTimeMillis();
        }
    }

    public TimeTracker(CookieTycoon plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "time.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.playTimeData = new HashMap<>();
        loadData();
    }

    public void start() {
        stop();

        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                saveData();
                loadData();
            }
        }.runTaskTimer(plugin, 0, 100); // сохранение каждые 5 сек
    }

    public void stop() {
        if (updateTask != null) {
            updateTask.cancel();
        }
    }

    public void onPlayerLogin(Player player) {
        PlayerPlayTime stats = playTimeData.computeIfAbsent(player.getUniqueId(), k -> new PlayerPlayTime());
        stats.lastLoginTime = System.currentTimeMillis();
        saveData();
    }

    public void onPlayerLogout(Player player) {
        PlayerPlayTime stats = playTimeData.get(player.getUniqueId());
        if (stats != null) {
            long sessionTime = System.currentTimeMillis() - stats.lastLoginTime;
            stats.totalPlayTime += sessionTime;
            stats.currentRebirthPlayTime += sessionTime;
            saveData();
        }
    }

    public void onRebirth(Player player) {
        PlayerPlayTime stats = playTimeData.get(player.getUniqueId());
        if (stats != null) {
            stats.currentRebirthPlayTime = 0;
            saveData();
        }
    }

    public double getTotalPlayTimeHours(Player player) {
        PlayerPlayTime stats = playTimeData.get(player.getUniqueId());
        if (stats == null) return 0;
        return (stats.totalPlayTime / 1000.0) / 3600.0;
    }

    public double getCurrentRebirthPlayTimeHours(Player player) {
        PlayerPlayTime stats = playTimeData.get(player.getUniqueId());
        if (stats == null) return 0;
        return (stats.currentRebirthPlayTime / 1000.0) / 3600.0;
    }

    // Загрузка данных из файла
    private void loadData() {
        if (!dataFile.exists()) {
            playTimeData = new HashMap<>();
            return;
        }

        try (Reader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<UUID, PlayerPlayTime>>(){}.getType();
            playTimeData = gson.fromJson(reader, type);
            if (playTimeData == null) {
                playTimeData = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            playTimeData = new HashMap<>();
        }
    }

    private void saveData() {
        try (Writer writer = new FileWriter(dataFile)) {
            gson.toJson(playTimeData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String format(double totalHours) {
        long totalMinutes = (long)(totalHours * 60);

        int years = (int)(totalMinutes / (60 * 24 * 365));
        int remaining = (int)(totalMinutes % (60 * 24 * 365));

        int months = remaining / (60 * 24 * 30);
        remaining %= (60 * 24 * 30);

        int weeks = remaining / (60 * 24 * 7);
        remaining %= (60 * 24 * 7);

        int days = remaining / (60 * 24);
        remaining %= (60 * 24);

        int hours = remaining / 60;
        int minutes = remaining % 60;

        StringBuilder result = new StringBuilder();

        if (years > 0) {
            result.append(years).append("г ");
        }
        if (months > 0) {
            result.append(months).append("мес ");
        }
        if (weeks > 0) {
            result.append(weeks).append("нед ");
        }
        if (days > 0) {
            result.append(days).append("д ");
        }
        if (hours > 0) {
            result.append(hours).append("ч ");
        }
        if (minutes > 0 || result.length() == 0) {
            result.append(minutes).append("мин");
        }

        return result.toString().trim();
    }
}