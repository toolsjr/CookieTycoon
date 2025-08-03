package com.tools.cookieTycoon.listeners;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.achievements.Achievement;
import com.tools.cookieTycoon.game.GUI;
import com.tools.cookieTycoon.game.UpgradesManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class GUIListener implements Listener {
    private final CookieTycoon plugin;
    private final UpgradesManager upgradesManager;
    private GUI gui;
    private final Map<Material, String> upgradeMaterials = new HashMap<>();


    public GUIListener(CookieTycoon plugin, GUI gui) {
        this.plugin = plugin;
        this.upgradesManager = plugin.getUpgradesManager();
        this.gui = gui;
    }

    @EventHandler
    public void click(InventoryClickEvent e) {

        e.setCancelled(true);

        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        String name = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        Player p = (Player) e.getWhoClicked();
        Material type = e.getCurrentItem().getType();

        upgradeMaterials.put(upgradesManager.getCursor().getItem(p).getType(), "cursor");
        upgradeMaterials.put(upgradesManager.getGranny().getItem(p).getType(), "granny");
        upgradeMaterials.put(upgradesManager.getFarm().getItem(p).getType(), "farm");
        upgradeMaterials.put(upgradesManager.getMine().getItem(p).getType(), "mine");
        upgradeMaterials.put(upgradesManager.getFactory().getItem(p).getType(), "factory");
        upgradeMaterials.put(upgradesManager.getBank().getItem(p).getType(), "bank");
        upgradeMaterials.put(upgradesManager.getTemple().getItem(p).getType(), "temple");
        upgradeMaterials.put(upgradesManager.getTower().getItem(p).getType(), "tower");
        upgradeMaterials.put(upgradesManager.getShipment().getItem(p).getType(), "shipment");
        upgradeMaterials.put(upgradesManager.getLab().getItem(p).getType(), "lab");
        upgradeMaterials.put(upgradesManager.getPortal().getItem(p).getType(), "portal");
        upgradeMaterials.put(upgradesManager.getTimeMachine().getItem(p).getType(), "timemachine");
        upgradeMaterials.put(upgradesManager.getCondenser().getItem(p).getType(), "condenser");
        upgradeMaterials.put(upgradesManager.getPrism().getItem(p).getType(), "prism");
        upgradeMaterials.put(upgradesManager.getChancemaker().getItem(p).getType(), "chancemaker");
        upgradeMaterials.put(upgradesManager.getEngine().getItem(p).getType(), "engine");
        upgradeMaterials.put(upgradesManager.getConsole().getItem(p).getType(), "console");
        upgradeMaterials.put(upgradesManager.getIdleverse().getItem(p).getType(), "idleverse");
        upgradeMaterials.put(upgradesManager.getCortexBaker().getItem(p).getType(), "cortexbaker");
        upgradeMaterials.put(upgradesManager.getYou().getItem(p).getType(), "you");

        if (e.getInventory() == gui.getUpgrades()) {
            if (e.getClick().equals(ClickType.LEFT)) {
                String upgradeName = upgradeMaterials.get(type);
                if (upgradeName != null) {
                    upgradesManager.buy(p, upgradeName);
                }
            }

            if (e.getClick().equals(ClickType.RIGHT)) {
                String upgradeName = upgradeMaterials.get(type);
                if (upgradeName != null) {
                    upgradesManager.buy(p, upgradeName, 10);
                }
            }

            if (e.getClick().equals(ClickType.MIDDLE)) {
                String upgradeName = upgradeMaterials.get(type);
                if (upgradeName != null) {
                    upgradesManager.buy(p, upgradeName, 100);
                }
            }

            if (e.getClick().equals(ClickType.SHIFT_LEFT)) {
                String upgradeName = upgradeMaterials.get(type);
                if (upgradeName != null) {
                    upgradesManager.sell(p, upgradeName);
                }
            }

            if (e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                String upgradeName = upgradeMaterials.get(type);
                if (upgradeName != null) {
                    upgradesManager.sell(p, upgradeName, 10);
                }
            }
        }

        if (e.getInventory() == gui.getRebirthInventory()) {
            if (type == Material.GOLDEN_APPLE || type == Material.ENCHANTED_GOLDEN_APPLE) {
                plugin.getRebirthManager().buy(p);
                p.closeInventory();
            }
        }

        if (e.getInventory() == gui.getAch()) {
            for (int id = 0; id < 1000; id++) {
                Achievement achievement = plugin.getAchievementManager().get(id);
                if (achievement == null) continue;

                if (name.equalsIgnoreCase(ChatColor.stripColor(achievement.getName()))) {
                    plugin.getAchievementManager().buy(p, id);
                    break;
                }
            }

        }
    }
}
