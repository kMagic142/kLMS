package ro.kmagic.klms;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ro.kmagic.klms.commands.CommandManager;
import ro.kmagic.klms.game.GameManager;
import ro.kmagic.klms.game.GameStatus;
import ro.kmagic.klms.language.LanguageManager;
import ro.kmagic.klms.language.PlaceholderAPI;
import ro.kmagic.klms.listeners.*;
import ro.kmagic.klms.settings.InventoryManager;
import ro.kmagic.klms.settings.SettingsManager;
import ro.kmagic.klms.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class KLMS extends JavaPlugin implements MessageUtils {

    private static KLMS instance;
    private SettingsManager settingsManager;
    private LanguageManager languageManager;

    public void onEnable() {
        instance = this;

        this.settingsManager = SettingsManager.getInstance();
        this.settingsManager.setup(this);

        this.languageManager = LanguageManager.getInstance();
        this.languageManager.setup(this);

        this.inventoryManager = InventoryManager.getInstance();
        this.inventoryManager.setup(this);

        this.gameManager = new GameManager(this);

        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPI(this).register();
        }

        players = new ArrayList<>();
        spectators = new ArrayList<>();

        sendLogger("&8&l> &7&m------- &8&l( &3&lkLMS &b&lby kMagic &8&l) &7&m------- &8&l<");
        sendLogger("&b   Plugin has been initialized.");
        sendLogger("&b   Version: &3v" + getDescription().getVersion());
        sendLogger("&b   Enabled listeners: &3" + enableListeners());
        sendLogger("&b   Enabled commands: &3" + enableCommands());
        sendLogger("&8&l> &7&m------- &8&l( &3&lkLMS &b&lby kMagic &8&l) &7&m------- &8&l<");

        if (this.settingsManager.getConfig().getBoolean("Arena Auto-Prepare.Enable"))
            timeTask();
    }
    private InventoryManager inventoryManager; private GameManager gameManager; public static ArrayList<Player> players; public static ArrayList<Player> spectators;

    private Integer enableListeners() {
        Integer i = 9;
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new EntityDamageByEntityListener(this), this);
        pluginManager.registerEvents(new EntityDamageListener(this), this);
        pluginManager.registerEvents(new FoodLevelChangeListener(this), this);
        pluginManager.registerEvents(new PlayerCommandPreprocessListener(this), this);
        pluginManager.registerEvents(new PlayerDeathListener(this), this);
        pluginManager.registerEvents(new PlayerDropItemListener(this), this);
        pluginManager.registerEvents(new PlayerPickupItemListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerRespawnListener(this), this);
        pluginManager.registerEvents(new PlayerInteractListener(this), this);
        return i;
    }

    private Integer enableCommands() {
        CommandManager commandManager = new CommandManager(this);
        return commandManager.getCommands().size();
    }

    public static KLMS getInstance() {
        return instance;
    }

    public SettingsManager getSettingsManager() {
        return this.settingsManager;
    }

    public void reloadSettingManager() {
        this.settingsManager.reload();
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public void reloadLanguageManager() {
        this.languageManager.reload();
    }

    public InventoryManager getInventoryManager() {
        return this.inventoryManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public void timeTask() {
        long nextGame = 0L;
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
        calendar.set(Calendar.SECOND, 0);

        while (nextGame == 0L) {
            for (String time : this.settingsManager.getConfig().getStringList("Arena Auto-Prepare.Hours")) {
                String[] tempTime = time.split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tempTime[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(tempTime[1]));

                long duration = calendar.getTimeInMillis() - System.currentTimeMillis();
                if (duration > 0L && (nextGame == 0L || duration < nextGame)) {
                    nextGame = duration;
                    System.out.println(nextGame);
                }
            }
            calendar.add(Calendar.MILLISECOND, 86400000);
        }

        nextGame = (nextGame / 1000L + 1L) * 20L;

        Bukkit.getScheduler().scheduleAsyncDelayedTask(this, new BukkitRunnable() {

            @Override
            public void run() {
                if (gameManager.getStatus().equals(GameStatus.IDLE)) {
                    gameManager.waitForPlayers();
                }

                timeTask();
            }
        },  nextGame);
    }


    public void onDisable() {
        super.onDisable();
    }
}
