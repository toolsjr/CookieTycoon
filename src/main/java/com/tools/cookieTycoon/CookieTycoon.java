package com.tools.cookieTycoon;


import com.tools.cookieTycoon.achievements.AchievementManager;
import com.tools.cookieTycoon.game.CookieManager;
import com.tools.cookieTycoon.game.GUI;
import com.tools.cookieTycoon.game.RebirthManager;
import com.tools.cookieTycoon.game.UpgradesManager;
import com.tools.cookieTycoon.listeners.ClickListener;
import com.tools.cookieTycoon.listeners.GUIListener;
import com.tools.cookieTycoon.listeners.JoinListener;
import com.tools.cookieTycoon.utils.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class CookieTycoon extends JavaPlugin {

    private static CookieTycoon instance;

    private ConfigManager configManager;
    private DatabaseManager database;
    private TimeTracker timeTracker;
    private AchievementManager achievementManager;
    private CookieManager cookieManager;
    private RebirthManager rebirthManager;
    private UpgradesManager upgradesManager;
    private ActionBar actionBar;
    private GUI gui;
    private ScoreboardManager scoreboard;

    private BukkitTask updateTask;

    @Override
    public void onEnable() {

        String connectionString =  configManager.getConfig().getString("connection-string", "mongodb://localhost:27017/");
        String databaseName =  configManager.getConfig().getString("db-name", "cookieTycoon");

        instance = this;

        configManager = new ConfigManager(this);
        this.database = new DatabaseManager(this, connectionString, databaseName);
        this.timeTracker = new TimeTracker(this);
        this.achievementManager = new AchievementManager(this);
        this.cookieManager = new CookieManager(this, database);
        this.rebirthManager = new RebirthManager(this, database);
        this.upgradesManager = new UpgradesManager(this, database);
        this.actionBar = new ActionBar(this);
        this.gui = new GUI(this);
        this.scoreboard = new ScoreboardManager(this);
        getCommand("ck").setExecutor(new CommandManager(this));
        getCommand("info").setExecutor(new CommandManager(this));
        getCommand("menu").setExecutor(new CommandManager(this));
        getCommand("upgrade").setExecutor(new CommandManager(this));
        getCommand("rebirth").setExecutor(new CommandManager(this));
        getCommand("ach").setExecutor(new CommandManager(this));
        getCommand("ck").setTabCompleter(new TabCompleter());
        getCommand("upgrade").setTabCompleter(new TabCompleter());
        getServer().getPluginManager().registerEvents(new ClickListener(this), this);
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this, gui), this);

        timeTracker.start();
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.close();
        }
    }

    public static CookieTycoon get() {
        return instance;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public TimeTracker getTimeTracker() {
        return timeTracker;
    }

    public AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public ActionBar getActionBar() {
        return actionBar;
    }

    public UpgradesManager getUpgradesManager() {
        return upgradesManager;
    }

    public RebirthManager getRebirthManager() {
        return rebirthManager;
    }

    public GUI getGui() {
        return gui;
    }

    public ScoreboardManager getScoreboard() {
        return scoreboard;
    }
}
