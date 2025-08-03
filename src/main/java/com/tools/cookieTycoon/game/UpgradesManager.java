package com.tools.cookieTycoon.game;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.upgrades.*;
import com.tools.cookieTycoon.utils.DatabaseManager;
import com.tools.cookieTycoon.utils.NumberFormatter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

// 1 Cursors V
// 2 Grandmas (granny) V
// 3 Farms V
// 4 Mines V
// 5 Factories V
// 6 Banks V
// 7 Temples V
// 8 Wizard towers (tower) V
// 9 Shipments V
// 10 Alchemy labs V
// 11 Portals V
// 12 Time machines V
// 13 Antimatter condensers V
// 14 Prisms V
// 15 Chancemakers V
// 16 Fractal Engines V
// 17 Javascript Consoles V
// 18 Idleverses V
// 19 Cortex Bakers V
// 20 You (nick) V

public class UpgradesManager {

    private CookieTycoon plugin;
    private DatabaseManager database;
    private BukkitTask updateTask;

    private Cursor cursor;
    private Granny granny;
    private Farm farm;
    private Mine mine;
    private Factory factory;
    private Bank bank;
    private Temple temple;
    private Tower tower;
    private Shipment shipment;
    private Lab lab;
    private Portal portal;
    private TimeMachine timeMachine;
    private Condenser condenser;
    private Prism prism;
    private Chancemaker chancemaker;
    private Engine engine;
    private Console console;
    private Idleverse idleverse;
    private CortexBaker cortexBaker;
    private You you;

    private final Map<String, Upgrade> upgrades = new HashMap<>();
    Map<Integer, String> upgradeSlots = new LinkedHashMap<>() {{
        put(9, "cursor");
        put(10, "granny");
        put(11, "farm");
        put(12, "mine");
        put(13, "factory");
        put(14, "bank");
        put(15, "temple");
        put(16, "tower");
        put(17, "shipment");
        put(18, "lab");
        put(19, "portal");
        put(20, "timemachine");
        put(21, "condenser");
        put(22, "prism");
        put(23, "chancemaker");
        put(24, "engine");
        put(25, "console");
        put(26, "idleverse");
        put(27, "cortexbaker");
        put(28, "you");
    }};

    public UpgradesManager(CookieTycoon plugin, DatabaseManager database) {
        this.plugin = plugin;
        this.database = database;
        this.cursor = new Cursor(plugin);
        this.granny = new Granny(plugin);
        this.farm = new Farm(plugin);
        this.mine = new Mine(plugin);
        this.factory = new Factory(plugin);
        this.bank = new Bank(plugin);
        this.temple = new Temple(plugin);
        this.tower = new Tower(plugin);
        this.shipment = new Shipment(plugin);
        this.lab = new Lab(plugin);
        this.portal = new Portal(plugin);
        this.timeMachine = new TimeMachine(plugin);
        this.condenser = new Condenser(plugin);
        this.prism = new Prism(plugin);
        this.chancemaker = new Chancemaker(plugin);
        this.engine = new Engine(plugin);
        this.console = new Console(plugin);
        this.idleverse = new Idleverse(plugin);
        this.cortexBaker = new CortexBaker(plugin);
        this.you = new You(plugin);

        upgrades.put("cursor", cursor);
        upgrades.put("granny", granny);
        upgrades.put("farm", farm);
        upgrades.put("mine", mine);
        upgrades.put("factory", factory);
        upgrades.put("bank", bank);
        upgrades.put("temple", temple);
        upgrades.put("tower", tower);
        upgrades.put("shipment", shipment);
        upgrades.put("lab", lab);
        upgrades.put("portal", portal);
        upgrades.put("timemachine", timeMachine);
        upgrades.put("condenser", condenser);
        upgrades.put("prism", prism);
        upgrades.put("chancemaker", chancemaker);
        upgrades.put("engine", engine);
        upgrades.put("console", console);
        upgrades.put("idleverse", idleverse);
        upgrades.put("cortexbaker", cortexBaker);
        upgrades.put("you", you);
    }

    public void start(Player p) {
        stop();

        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                update(p);
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void update(Player p) {
        for (Upgrade upgrade : upgrades.values()) {
            upgrade.usage(p);
        }

        for (Map.Entry<Integer, String> entry : upgradeSlots.entrySet()) {
            int slot = entry.getKey();
            String upgradeKey = entry.getValue();
            Upgrade upgrade = upgrades.get(upgradeKey);

            if (upgrade != null) {
                if (get(p, upgradeKey) != 0) {
                    p.getInventory().setItem(slot, upgrade.getInfoItem(p));
                } else {
                    p.getInventory().setItem(slot, null);
                }
            }
        }
    }

    public void stop() {
        if (updateTask != null) {
            updateTask.cancel();
        }
    }

    public void buy(Player player, String upgrade) {
        if (plugin.getCookieManager().get(player) >= getUpgrade(upgrade).getPrice(player)) {
            plugin.getCookieManager().remove(player, getUpgrade(upgrade).getPrice(player));
            add(player, upgrade, 1);
            plugin.getGui().upgrades(player);
        } else {
            player.sendMessage("У вас недостаточно средств");
            player.closeInventory();
        }
    }

    public void buy(Player player, String upgrade, int amount) {
        if (amount == 10) {
            if (plugin.getCookieManager().get(player) >= getUpgrade(upgrade).getPrice(player) * 20.3037182381) {
                plugin.getCookieManager().remove(player, getUpgrade(upgrade).getPrice(player) * 20.3037182381);
                add(player, upgrade, amount);
                plugin.getGui().upgrades(player);
            } else {
                player.sendMessage("У вас недостаточно средств");
                player.closeInventory();
            }
        } else if (amount == 100) {
            if (plugin.getCookieManager().get(player) >= getUpgrade(upgrade).getPrice(player) * 7828749.671335256) {
                plugin.getCookieManager().remove(player, getUpgrade(upgrade).getPrice(player) * 7828749.671335256);
                add(player, upgrade, amount);
                plugin.getGui().upgrades(player);
            } else {
                player.sendMessage("У вас недостаточно средств");
                player.closeInventory();
            }
        }
    }

    public void sell(Player player, String upgrade) {
        if (get(player, upgrade) >= 1) {
            plugin.getCookieManager().add(player, (getUpgrade(upgrade).getPrice(player) / 4));
            int current = get(player, upgrade);
            set(player, upgrade, current - 1);
            plugin.getGui().upgrades(player);
        } else {
            player.sendMessage("У вас нет улучшения");
            player.closeInventory();
        }
    }

    public void sell(Player player, String upgrade, int amount) {
        if (amount == 10) {
            if (get(player, upgrade) >= 10) {
                plugin.getCookieManager().set(player, plugin.getCookieManager().get(player)+getUpgrade(upgrade).getPrice(player) * 1.254);
                int current = get(player, upgrade);
                player.sendMessage("+" + NumberFormatter.format(getUpgrade(upgrade).getPrice(player) * 1.254));
                set(player, upgrade, current - 10);
                plugin.getGui().upgrades(player);
            } else {
                player.sendMessage("У вас нет столько улучшений");
                player.closeInventory();
            }
        } else if (amount == 100) {
            if (get(player, upgrade) >= 100) {
                plugin.getCookieManager().set(player, plugin.getCookieManager().get(player)+getUpgrade(upgrade).getPrice(player) * 1.198);
                int current = get(player, upgrade);
                player.sendMessage("+" + NumberFormatter.format(getUpgrade(upgrade).getPrice(player) * 1.198));
                set(player, upgrade, current - 100);
                plugin.getGui().upgrades(player);
            } else {
                player.sendMessage("У вас нет столько улучшений");
                player.closeInventory();
            }
        }
        player.sendMessage("да");
    }

    public Upgrade getUpgrade(String name) {
        return upgrades.get(name.toLowerCase());
    }

    public Map<String, Upgrade> getUpgrades() {
        return upgrades;
    }

    public int getSumExcept(Player player, String upgrade) {
        int count = 0;
        for (String upgradeName : upgrades.keySet()) {
            count += get(player, upgradeName);
        }
        return count-get(player, upgrade);
    }

    public int get(Player player, String upgradeName) {
        return database.getUpgrade(player, upgradeName);
    }

    public void set(Player player, String upgradeName, int amount) {
        database.setUpgrade(player, upgradeName, amount);
    }

    public void add(Player player, String upgradeName, int amount) {
        int current = database.getUpgrade(player, upgradeName);
        database.setUpgrade(player, upgradeName, current + amount);
    }

    public void wipe(Player player) {
        database.setUpgrade(player, "cursor", 0);
        database.setUpgrade(player, "granny", 0);
        database.setUpgrade(player, "farm", 0);
        database.setUpgrade(player, "mine", 0);
        database.setUpgrade(player, "factory", 0);
        database.setUpgrade(player, "bank", 0);
        database.setUpgrade(player, "temple", 0);
        database.setUpgrade(player, "tower", 0);
        database.setUpgrade(player, "shipment", 0);
        database.setUpgrade(player, "lab", 0);
        database.setUpgrade(player, "portal", 0);
        database.setUpgrade(player, "timemachine", 0);
        database.setUpgrade(player, "condenser", 0);
        database.setUpgrade(player, "prism", 0);
        database.setUpgrade(player, "chancemaker", 0);
        database.setUpgrade(player, "engine", 0);
        database.setUpgrade(player, "console", 0);
        database.setUpgrade(player, "idleverse", 0);
        database.setUpgrade(player, "cortexbaker", 0);
        database.setUpgrade(player, "you", 0);
    }

    public Cursor getCursor() {
        return cursor;
    }

    public Granny getGranny() {
        return granny;
    }

    public Farm getFarm() {
        return farm;
    }

    public Mine getMine() {
        return mine;
    }

    public Factory getFactory() {
        return factory;
    }

    public Bank getBank() {
        return bank;
    }

    public Temple getTemple() {
        return temple;
    }

    public Tower getTower() {
        return tower;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Lab getLab() {
        return lab;
    }

    public Portal getPortal() {
        return portal;
    }

    public TimeMachine getTimeMachine() {
        return timeMachine;
    }

    public Condenser getCondenser() {
        return condenser;
    }

    public Prism getPrism() {
        return prism;
    }

    public Chancemaker getChancemaker() {
        return chancemaker;
    }

    public Engine getEngine() {
        return engine;
    }

    public Console getConsole() {
        return console;
    }

    public Idleverse getIdleverse() {
        return idleverse;
    }

    public CortexBaker getCortexBaker() {
        return cortexBaker;
    }

    public You getYou() {
        return you;
    }
}
