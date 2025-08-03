package com.tools.cookieTycoon.utils;

import com.tools.cookieTycoon.CookieTycoon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

public class ConfigManager {

    private CookieTycoon plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager (CookieTycoon plugin) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        this.plugin = plugin;
    }

    private void createConfig() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);

        }

        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Не удалось загрузить config.yml", e);
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
