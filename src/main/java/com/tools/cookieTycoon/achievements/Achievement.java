package com.tools.cookieTycoon.achievements;

import com.tools.cookieTycoon.CookieTycoon;
import com.tools.cookieTycoon.utils.NumberFormatter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Achievement {
    private CookieTycoon plugin = CookieTycoon.get();
    int id;
    String upgradeType;
    String displayName;
    double multiplier;
    double price;
    boolean multiplication;

    public Achievement(int id, String upgradeType, String displayName, double price, double multiplier, boolean multiplication) {
        this.id = id;
        this.upgradeType = upgradeType;
        this.displayName = displayName;
        this.price = price;
        this.multiplier = multiplier;
        this.multiplication = multiplication;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "§r§l" + displayName;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public double getPrice() {
        return price;
    }

    public String getType() {
        return upgradeType;
    }

    public boolean isMultiplication() {
        return multiplication;
    }

    public List<String> getLore() {
        if (isMultiplication()) {
            return Arrays.asList("§7" + getRussianName() + " становятся §fв " + NumberFormatter.format(multiplier) + " раза §7эффективнее",
                    "",
                    "§6⃝ §a" + NumberFormatter.format(price));
        } else
            return Arrays.asList("§7"+getRussianName() + "§7 получают §f+" + NumberFormatter.format(multiplier) + "§7 печенек за каждое",
                "§7строение, не являющееся " + upgradeType,
                "",
                "§6⃝ §a" + NumberFormatter.format(price));
    }

    public ItemStack getItem() {
        return plugin.getAchievementManager().infoItem(id);
    }


    private String getRussianName() {
        switch (upgradeType.toLowerCase()) {
            case "cursor":
                return "Мышь и курсоры";
            case "granny":
                return "Бабули";
            case "farm":
                return "Фермы";
            case "mine":
                return "Шахты";
            case "factory":
                return "Фабрики";
            case "bank":
                return "Банки";
            case "temple":
                return "Храмы";
            case "tower":
                return "Башни мага";
            case "shipment":
                return "Космические доставки";
            case "lab":
                return "Алхимические лаборатории";
            case "portal":
                return "Порталы";
            case "timemachine":
                return "Машины времени";
            case "condenser":
                return "Конденсаторы антиматерии";
            case "prism":
                return "Призмы";
            case "chancemaker":
                return "Шансмейкеры";
            case "engine":
                return "Фрактальные двигатели";
            case "console":
                return "Консоли Java";
            case "idleverse":
                return "Пустые вселенные";
            case "cortexbaker":
                return "Мозговые пекари";
            case "you":
                return "Вы";
            default:
                return upgradeType;
        }
    }
}
