package ro.kmagic.klms.settings;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class SettingsManager {
    private static SettingsManager instance = new SettingsManager();
    private FileConfiguration config;
    private File cfile;

    public static SettingsManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        this.cfile = new File(p.getDataFolder(), "settings.yml");
        this.config = YamlConfiguration.loadConfiguration(this.cfile);

        this.config.options().header("SuperLMS by Stefan923\n");
        this.config.addDefault("Game.Starting Counter", Integer.valueOf(60));
        this.config.addDefault("Game.Maximum Player Count", Integer.valueOf(30));
        this.config.addDefault("Game.Minimum Player Count", Integer.valueOf(15));
        this.config.addDefault("Game.Grace Period.Enabled", Boolean.valueOf(true));
        this.config.addDefault("Game.Grace Period.Time In Seconds", Integer.valueOf(10));
        this.config.addDefault("Game.Winner Rewards", Arrays.asList(new String[] { "give %player_name% minecraft:diamond 16", "give %player_name% minecraft:iron_ingot 32" }));
        this.config.addDefault("Game.Command Whitelist", Arrays.asList(new String[] { "ac", "helpop", "report" }));
        this.config.addDefault("Arena Auto-Prepare.Enable", Boolean.valueOf(true));
        this.config.addDefault("Arena Auto-Prepare.Hours", Arrays.asList(new String[] { "9:00", "12:00", "15:00", "18:00", "21:00", "0:00" }));
        this.config.addDefault("Arena Auto-Stop.Enable", Boolean.valueOf(true));
        this.config.addDefault("Arena Auto-Stop.After X Seconds", Integer.valueOf(600));
        this.config.options().copyDefaults(true);
        save();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void resetConfig() {
        save();
    }

    public void save() {
        try {
            this.config.save(this.cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("File 'settings.yml' couldn't be saved!");
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.cfile);
    }
}

