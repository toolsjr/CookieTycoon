package com.tools.cookieTycoon.game;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.achievements.Achievement;
import com.tools.cookieTycoon.utils.NumberFormatter;
import com.tools.cookieTycoon.utils.TimeTracker;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GUI {
    private CookieTycoon plugin;
    private Inventory upgradeInventory;
    public Inventory rebirthInventory;
    public Inventory ach;

    public GUI(CookieTycoon plugin) {
        this.plugin = plugin;
    }

    private static class UpgradeInfo {
        String upgradeKey;
        String requiredUpgrade;

        public UpgradeInfo(String upgradeKey, String requiredUpgrade) {
            this.upgradeKey = upgradeKey;
            this.requiredUpgrade = requiredUpgrade;
        }
    }

    public void upgrades(Player player) {
        upgradeInventory = Bukkit.createInventory(null, 54, "Улучшения");

        Map<Integer, UpgradeInfo> upgradeSlots = new LinkedHashMap<>() {{
            put(1, new UpgradeInfo("cursor", null));
            put(10, new UpgradeInfo("granny", null));
            put(19, new UpgradeInfo("farm", "cursor"));
            put(28, new UpgradeInfo("mine", "granny"));
            put(37, new UpgradeInfo("factory", "farm"));
            put(46, new UpgradeInfo("bank", "mine"));
            put(3, new UpgradeInfo("temple", "factory"));
            put(12, new UpgradeInfo("tower", "bank"));
            put(21, new UpgradeInfo("shipment", "temple"));
            put(30, new UpgradeInfo("lab", "tower"));
            put(39, new UpgradeInfo("portal", "shipment"));
            put(48, new UpgradeInfo("timemachine", "lab"));
            put(5, new UpgradeInfo("condenser", "portal"));
            put(14, new UpgradeInfo("prism", "timemachine"));
            put(23, new UpgradeInfo("chancemaker", "condenser"));
            put(32, new UpgradeInfo("engine", "prism"));
            put(41, new UpgradeInfo("console", "chancemaker"));
            put(50, new UpgradeInfo("idleverse", "engine"));
            put(25, new UpgradeInfo("cortexbaker", "console"));
            put(34, new UpgradeInfo("you", "idleverse"));
        }};

        // Заполняем инвентарь
        for (Map.Entry<Integer, UpgradeInfo> entry : upgradeSlots.entrySet()) {
            int slot = entry.getKey();
            UpgradeInfo info = entry.getValue();

            if (info.requiredUpgrade == null ||
                    plugin.getUpgradesManager().get(player, info.requiredUpgrade) > 0 ||
                    plugin.getCookieManager().get(player) >= plugin.getUpgradesManager().getUpgrade(info.requiredUpgrade).getPrice(player)) {

                upgradeInventory.setItem(slot, plugin.getUpgradesManager().getUpgrade(info.upgradeKey).getItem(player));
            } else {
                upgradeInventory.setItem(slot, upgradeNotAvailableItem(player, info.upgradeKey));
            }
        }

        player.openInventory(upgradeInventory);
    }

    public void info(Player sender, Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + player.getDisplayName() + "§f - " + getOnline(player));
        gui.setItem(14, new ItemStack(Material.BARRIER));
        gui.setItem(13, infoItem(player));
        gui.setItem(12, upgradesItem(player));
        sender.openInventory(gui);
    }

    public void ach(Player player) {
        ach = Bukkit.createInventory(null, 27, "Ачивменты");

        // Создаем список для хранения ачивментов с их ценами
        List<Map.Entry<Integer, Double>> achievementsWithPrices = new ArrayList<>();

        // 1. Собираем все доступные ачивменты с их ценами
        for (Map.Entry<String, Map<Integer, Integer>> entry : plugin.getAchievementManager().getRequirements().entrySet()) {
            String upgradeType = entry.getKey();
            int count = plugin.getUpgradesManager().get(player, upgradeType);

            entry.getValue().forEach((id, required) -> {
                if (count >= required && !plugin.getAchievementManager().has(player, id)) {
                    Achievement achievement = plugin.getAchievementManager().get(id);
                    if (achievement != null) {
                        achievementsWithPrices.add(new AbstractMap.SimpleEntry<>(id, achievement.getPrice()));
                    }
                }
            });
        }

        // 2. Сортируем ачивменты по цене (от дешевых к дорогим)
        achievementsWithPrices.sort(Comparator.comparingDouble(Map.Entry::getValue));

        // 3. Заполняем инвентарь в отсортированном порядке
        for (int i = 0; i < Math.min(achievementsWithPrices.size(), 27); i++) {
            int achievementId = achievementsWithPrices.get(i).getKey();
            ach.setItem(i, plugin.getAchievementManager().infoItem(achievementId));
        }

        player.openInventory(ach);
    }

    public void rebirth(Player player) {
        rebirthInventory = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "§lПерерождение");
        if (plugin.getRebirthManager().isAvailable(player))
            rebirthInventory.setItem(4, rebirthAvailableItem(player));
        else rebirthInventory.setItem(4, rebirthNotAvailableItem(player));
        player.openInventory(rebirthInventory);
    }

    private ItemStack upgradeNotAvailableItem(Player player, String upgradeName) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GRAY + "???");
        meta.setLore(Arrays.asList(
                "§8???",
                "",
                "§7(ЛКМ) Купить x1 - " + NumberFormatter.format(plugin.getUpgradesManager().getUpgrade(upgradeName).getPrice(player)),
                "§7(ПКМ) Купить x10 - " + NumberFormatter.format(plugin.getUpgradesManager().getUpgrade(upgradeName).getPrice(player) * 20.303718238),
                "§7(СКМ) Купить x100 - " + NumberFormatter.format(plugin.getUpgradesManager().getUpgrade(upgradeName).getPrice(player) * 7828749.671335256)));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack infoItem(Player player) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Информация о " + ChatColor.GOLD + player.getDisplayName());
        meta.setLore(Arrays.asList(
                "§6" + plugin.getRebirthManager().get(player) + "§f уровень престижа",
                "§fПеченья: §6" + NumberFormatter.format(plugin.getCookieManager().get(player)),
                "§fВ секунду: §6" + NumberFormatter.format(plugin.getCookieManager().getCPS(player)),
                "",
                "§fПеченья за всё время: §6" + NumberFormatter.format(plugin.getCookieManager().getAll(player)),
                "§fНаиграно всего: §6" + plugin.getTimeTracker().format(plugin.getTimeTracker().getTotalPlayTimeHours(player)),
                "§fНаиграно с текущим рб: §6" + plugin.getTimeTracker().format(plugin.getTimeTracker().getCurrentRebirthPlayTimeHours(player))));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack upgradesItem(Player player) {
        ItemStack item = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Улучшения");
        meta.setLore(Arrays.asList(
                "§fКурсоры: §6" + plugin.getUpgradesManager().get(player, "cursor"),
                "§fБабули: §6" + plugin.getUpgradesManager().get(player, "granny"),
                "§fФермы: §6" + plugin.getUpgradesManager().get(player, "farm"),
                "§fШахты: §6" + plugin.getUpgradesManager().get(player, "mine"),
                "§fФабрики: §6" + plugin.getUpgradesManager().get(player, "factory"),
                "§fБанки: §6" + plugin.getUpgradesManager().get(player, "bank"),
                "§fХрамы: §6" + plugin.getUpgradesManager().get(player, "temple"),
                "§fБашни: §6" + plugin.getUpgradesManager().get(player, "tower"),
                "§fКосм.доставка: §6" + plugin.getUpgradesManager().get(player, "shipment"),
                "§fАлхим.лаб.: §6" + plugin.getUpgradesManager().get(player, "lab"),
                "§fПорталы: §6" + plugin.getUpgradesManager().get(player, "portal"),
                "§fМашины времени: §6" + plugin.getUpgradesManager().get(player, "timemachine"),
                "§fКонденсаторы: §6" + plugin.getUpgradesManager().get(player, "condenser"),
                "§fПризмы: §6" + plugin.getUpgradesManager().get(player, "prism"),
                "§fШансмейкеры: §6" + plugin.getUpgradesManager().get(player, "chancemaker"),
                "§fФракт.двигатели: §6" + plugin.getUpgradesManager().get(player, "engine"),
                "§fКонсоли: §6" + plugin.getUpgradesManager().get(player, "console"),
                "§fВселенные: §6" + plugin.getUpgradesManager().get(player, "idleverse"),
                "§fПекари: §6" + plugin.getUpgradesManager().get(player, "cortexbaker"),
                "§fYou: §6" + plugin.getUpgradesManager().get(player, "you")));

        item.setItemMeta(meta);
        return item;
    }

    private ItemStack rebirthNotAvailableItem(Player player) {
        ItemStack item = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Совершить перерождение");
        meta.setLore(Arrays.asList(
                "§e" + plugin.getRebirthManager().get(player) + " §fуровень престижа",
                "§fВы пробыли в этой реальности: §e" + plugin.getTimeTracker().format(plugin.getTimeTracker().getCurrentRebirthPlayTimeHours(player)),
                "",
                "§cВам недоступно перерождение",
                "§7" + NumberFormatter.format(plugin.getCookieManager().getAll(player)) + "/" + NumberFormatter.format(plugin.getRebirthManager().getPrice(player))));
        item.setItemMeta(meta);
        return item;
    }

    private @NotNull ItemStack rebirthAvailableItem(Player player) {
        ItemStack item = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Совершить перерождение");
        meta.setLore(Arrays.asList(
                "§e" + plugin.getRebirthManager().get(player) + " §fуровень престижа",
                "§fВы пробыли в этой реальности: §e" + plugin.getTimeTracker().format(plugin.getTimeTracker().getCurrentRebirthPlayTimeHours(player)),
                "",
                "§aВам доступно перерождение!",
                "§7" + NumberFormatter.format(plugin.getCookieManager().getAll(player)) + "/" + NumberFormatter.format(plugin.getRebirthManager().getPrice(player))));
        item.setItemMeta(meta);
        return item;
    }

    public String getOnline(Player player) {
        if (player.isOnline()) return "§2Онлайн";
        return "§4Оффлайн";
    }

    public Inventory getUpgrades() {
        return upgradeInventory;
    }

    public Inventory getRebirthInventory() {
        return rebirthInventory;
    }

    public Inventory getAch() {
        return ach;
    }
}
