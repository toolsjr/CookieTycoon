package com.tools.cookieTycoon.achievements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tools.cookieTycoon.CookieTycoon;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import static java.util.Map.entry;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class AchievementManager {
    private CookieTycoon plugin;
    private final Gson gson;
    private final File dataFile;
    private Map<UUID, PlayerAchievements> achievementsData;
    private final Map<Integer, Achievement> achievements = new HashMap<>();
    private final Map<String, Map<Integer, Integer>> requirements = new HashMap<>() {{
        put("cursor", Map.ofEntries(
                entry(0, 1),    // ID 0: 1 cursor
                entry(1, 1),    // ID 1: 1 cursor
                entry(2, 10),   // ID 2: 10 cursors
                entry(3, 25),
                entry(4, 50),
                entry(5, 100),
                entry(6, 150),
                entry(43, 200),
                entry(82, 250),
                entry(109, 300),
                entry(188, 350),
                entry(189, 400),
                entry(660, 450),
                entry(764, 500),
                entry(873, 550)
        ));
        put("granny", Map.ofEntries(
                entry(7, 1),
                entry(8, 5),
                entry(9, 25),
                entry(44, 50),
                entry(110, 100),
                entry(192, 150),
                entry(294, 200),
                entry(307, 250),
                entry(428, 350),
                entry(480, 350),
                entry(506, 400),
                entry(662, 450),
                entry(700, 500),
                entry(743, 550),
                entry(840, 600)
        ));
        put("farm", Map.ofEntries(
                entry(10, 1),
                entry(11, 5),
                entry(12, 25),
                entry(45, 50),
                entry(111, 100),
                entry(193, 150),
                entry(295, 200),
                entry(308, 250),
                entry(429, 350),
                entry(481, 350),
                entry(507, 400),
                entry(663, 450),
                entry(701, 500),
                entry(744, 550),
                entry(841, 600)
        ));
        put("mine", Map.ofEntries(
                entry(16, 1),
                entry(17, 5),
                entry(18, 25),
                entry(47, 50),
                entry(113, 100),
                entry(195, 150),
                entry(296, 200),
                entry(309, 250),
                entry(430, 350),
                entry(482, 350),
                entry(508, 400),
                entry(664, 450),
                entry(702, 500),
                entry(745, 550),
                entry(842, 600)
        ));
    }};

    public AchievementManager(CookieTycoon plugin) {
        this.plugin = plugin;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.dataFile = new File(plugin.getDataFolder(), "achievements.json");

        // Создаем папку плагина, если ее нет
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        // # TRUE - УМНОЖЕНИЕ
        // # FALSE - СЛОЖЕНИЕ

        // Cursors (улучшения курсора дают +к клику тоже)
        register(new Achievement(0, "cursor", "Усиленный указательный палец", 100, 2, true));
        register(new Achievement(1, "cursor", "Крем от синдрома запястного канала", 500, 2, true));
        register(new Achievement(2, "cursor", "Амбидекстр", 10000, 2, true));
        register(new Achievement(3, "cursor", "Тысяча пальцев", 100000, 0.1, false));
        register(new Achievement(4, "cursor", "Миллион пальцев", 10000000, 0.5, false));
        register(new Achievement(5, "cursor", "Миллиард пальцев", 100000000, 5, false));
        register(new Achievement(6, "cursor", "Триллион пальцев", 1_000_000_000, 50, false));
        register(new Achievement(43, "cursor", "Квадриллион пальцев", 10 * Math.pow(10, 9), 500, false)); // 10B
        register(new Achievement(82, "cursor", "Квинтиллион пальцев", 10 * Math.pow(10, 12), 5000, false)); // 10T = 10*10^12
        register(new Achievement(109, "cursor", "Секстиллион пальцев", 10 * Math.pow(10, 15), 50000, false)); // 10Quad
        register(new Achievement(188, "cursor", "Септиллион пальцев", 10*Math.pow(10, 18), 500000, false)); // 10Qi
        register(new Achievement(189, "cursor", "Октиллион пальцев", 10*Math.pow(10, 21), 5000000, false)); // 10 Sx
        register(new Achievement(660, "cursor", "Нониллион пальцев", 10*Math.pow(10, 24), 50000000, false)); // 10 Sp
        register(new Achievement(764, "cursor", "Дециллион пальцев", 10*Math.pow(10, 27), 500000000, false)); // 10 Oc
        register(new Achievement(873, "cursor", "Ундециллион пальцев", 10*Math.pow(10, 30), 500000000*10, false)); // 10 Non

        // Grannies
        register(new Achievement(7, "granny", "Помощь от бабули", 1000, 2, true));
        register(new Achievement(8, "granny", "Стальные скалки", 5000, 2, true));
        register(new Achievement(9, "granny", "Вставные зубы", 50000, 2, true));
        register(new Achievement(44, "granny", "Сливовый сок", 5000000, 2, true));
        register(new Achievement(110, "granny", "Очки с двойным стеклом", 500000000, 2, true));
        register(new Achievement(192, "granny", "Агенты старения", 50 * Math.pow(10, 9), 2, true));
        register(new Achievement(294, "granny", "Экстремальные ходунки", 50 * Math.pow(10, 12), 2, true));
        register(new Achievement(307, "granny", "Безудержный", 50 * Math.pow(10, 15), 2, true));
        register(new Achievement(428, "granny", "Обратная деменция", 50 * Math.pow(10, 18), 2, true));
        register(new Achievement(480, "granny", "Стойкие краски для волос", 50 * Math.pow(10, 21), 2, true));
        register(new Achievement(506, "granny", "Хорошие манеры", 500 * Math.pow(10, 24), 2, true));
        register(new Achievement(662, "granny", "Генерационная дегенерация", 5 * Math.pow(10, 30), 2, true));
        register(new Achievement(700, "granny", "Визиты", 50 * Math.pow(10, 33), 2, true));
        register(new Achievement(743, "granny", "Кухонные кабинеты", 500 * Math.pow(10, 36), 2, true));
        register(new Achievement(840, "granny", "Трости с пенопластовыми наконечниками", 5 * Math.pow(10, 42), 2, true));


        // Farms
        register(new Achievement(10, "farm", "Дешёвые мотыги", 11000, 2, true));
        register(new Achievement(11, "farm", "Удобрение", 55000, 2, true));
        register(new Achievement(12, "farm", "Печеньковые деревья", 550000, 2, true));
        register(new Achievement(45, "farm", "Генетически-модифицированные печеньки", 55000000, 2, true));
        register(new Achievement(111, "farm", "Пряничные чучела", 10 * 550000000, 2, true));
        register(new Achievement(193, "farm", "Посыпка", 550 * Math.pow(10, 9), 2, true));
        register(new Achievement(295, "farm", "Помадковые грибы", 550 * Math.pow(10, 12), 2, true));
        register(new Achievement(308, "farm", "Пшеничные триффиды", 550 * Math.pow(10, 15), 2, true));
        register(new Achievement(429, "farm", "Гуманные пестициды", 550 * Math.pow(10, 18), 2, true));
        register(new Achievement(481, "farm", "Сараи", 550 * Math.pow(10, 21), 2, true));
        register(new Achievement(507, "farm", "Линдвормы", 5.5 * Math.pow(10, 27), 2, true));
        register(new Achievement(663, "farm", "Глобальное хранилище семян", 55 * Math.pow(10, 30), 2, true));
        register(new Achievement(701, "farm", "Обратное веганство", 550 * Math.pow(10, 33), 2, true));
        register(new Achievement(744, "farm", "Мульча из печенья", 5.5 * Math.pow(10, 39), 2, true));
        register(new Achievement(841, "farm", "Самоходные тракторы", 55 * Math.pow(10, 42), 2, true));


        // Mine
        register(new Achievement(16, "mine", "Сахарный газ", 120000, 2, true));
        register(new Achievement(17, "mine", "Мега-дрель", 600000, 2, true));
        register(new Achievement(18, "mine", "Ультра-дрель", 6000000, 2, true));
        register(new Achievement(47, "mine", "Ультима-дрель", 600000000, 2, true));
        register(new Achievement(113, "mine", "Минирование водородной бомбой", 600000000, 2, true));
        register(new Achievement(195, "mine", "Главная кузница", 6 * Math.pow(10, 12), 2, true));
        register(new Achievement(296, "mine", "Планетоделители", 6 * Math.pow(10, 15), 2, true));
        register(new Achievement(309, "mine", "Скважины рапсового масла", 6 * Math.pow(10, 18), 2, true));
        register(new Achievement(430, "mine", "Люди-кроты", 6 * Math.pow(10, 21), 2, true));
        register(new Achievement(482, "mine", "Подземные канарейки", 6 * Math.pow(10, 24), 2, true));
        register(new Achievement(508, "mine", "Снова скучно", 60 * Math.pow(10, 27), 2, true));
        register(new Achievement(664, "mine", "Добыча воздуха", 600 * Math.pow(10, 30), 2, true));
        register(new Achievement(702, "mine", "Карамельные сплавы", 6 * Math.pow(10, 36), 2, true));
        register(new Achievement(745, "mine", "Вкусная минералогия", 60 * Math.pow(10, 39), 2, true));
        register(new Achievement(842, "mine", "Опоры шахтовых стволов", 600 * Math.pow(10, 42), 2, true));


        loadData();
    }

    private void register(Achievement achievement) {
        achievements.put(achievement.id, achievement);
    }

    // Класс для хранения данных об достижениях игрока
    public static class PlayerAchievements {
        private List<Integer> IDs;

        public PlayerAchievements() {
            this.IDs = new ArrayList<>();
        }

        public PlayerAchievements(List<Integer> IDs) {
            this.IDs = IDs;
        }

        public List<Integer> getIDs() {
            return IDs;
        }

        public void setIDs(List<Integer> IDs) {
            this.IDs = IDs;
        }

        public void addAchievement(int id) {
            if (!IDs.contains(id)) {
                IDs.add(id);
            }
        }

        public boolean hasAchievement(int id) {
            return IDs.contains(id);
        }
    }

    // Загрузка данных из файла
    private void loadData() {
        if (!dataFile.exists()) {
            achievementsData = new HashMap<>();
            return;
        }

        try (Reader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<UUID, PlayerAchievements>>(){}.getType();
            achievementsData = gson.fromJson(reader, type);
            if (achievementsData == null) {
                achievementsData = new HashMap<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            achievementsData = new HashMap<>();
        }
    }

    // Сохранение данных в файл
    public void saveData() {
        try (Writer writer = new FileWriter(dataFile)) {
            gson.toJson(achievementsData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Получить достижения игрока
    public PlayerAchievements getPlayerAchievements(UUID playerId) {
        return achievementsData.computeIfAbsent(playerId, k -> new PlayerAchievements());
    }

    // Добавить достижение игроку
    public void add(Player player, int achievementId) {
        getPlayerAchievements(player.getUniqueId()).addAchievement(achievementId);
        saveData();
    }

    public void buy(Player player, int id) {
        if (!has(player, id)) {
            if (plugin.getCookieManager().get(player) >= get(id).getPrice()) {
                add(player, id);
                plugin.getCookieManager().remove(player, get(id).getPrice());
                player.sendMessage("§aВы успешно купили " + get(id).getName());
                plugin.getGui().ach(player);
            } else
                player.sendMessage("§cВам не хватает");
        } else player.sendMessage("У вас уже есть");
    }

    // Проверить есть ли у игрока достижение
    public boolean has(Player player, int achievementId) {
        PlayerAchievements playerAchievements = getPlayerAchievements(player.getUniqueId());
        return playerAchievements.hasAchievement(achievementId);
    }

    // Получить список всех достижений игрока
    public List<Integer> getList(Player player) {
        return getPlayerAchievements(player.getUniqueId()).getIDs();
    }

    // Установить достижения игроку (перезаписать)
    public void set(Player player, List<Integer> achievementIds) {
        achievementsData.put(player.getUniqueId(), new PlayerAchievements(achievementIds));
        saveData();
    }

    public void wipe(Player player, List<Integer> achievementIds) {
        set(player, Collections.singletonList(null));
    }

    public Achievement get(int id) {
        return achievements.get(id);
    }

    public ItemStack infoItem(int id) {
        if (get(id) == null) {
            ItemStack item = new ItemStack(Material.DIRT);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_RED + "Ошибка!");
            meta.setLore(Arrays.asList(ChatColor.DARK_RED + "ID " +  id + " не существует!"));
            item.setItemMeta(meta);
            return item;
        }
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(get(id).getName());
        meta.setLore(get(id).getLore());
        item.setItemMeta(meta);
        return item;
    }

    public double getCombinedMultiplier(Player player, String upgradeType) {

        double multiplier = 1.0;
        for (Achievement achievement : achievements.values()) {
            if (upgradeType.equals(achievement.getType()) &&
                    has(player, achievement.id) &&
                    achievement.isMultiplication()) {
                multiplier *= achievement.getMultiplier();
            }
        }
        return multiplier;
    }

    public double getCombinedTerm(Player player, String upgradeType) {
        double totalTerm = 0.0;
        double sumExcept = plugin.getUpgradesManager().getSumExcept(player, upgradeType);

        for (Achievement achievement : achievements.values()) {
            if (upgradeType.equals(achievement.getType()) &&
                    has(player, achievement.id) &&
                    !achievement.isMultiplication()) {
                totalTerm += achievement.getMultiplier() * sumExcept;
            }
        }
        return totalTerm;
    }

    public Map<String, Map<Integer, Integer>> getRequirements() {
        return requirements;
    }
}