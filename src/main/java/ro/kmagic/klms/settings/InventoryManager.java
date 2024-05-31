package ro.kmagic.klms.settings;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class InventoryManager {
    private static InventoryManager instance = new InventoryManager();
    private FileConfiguration config;
    private File cfile;

    public static InventoryManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {
        this.cfile = new File(p.getDataFolder(), "inventories.yml");
        this.config = (FileConfiguration) YamlConfiguration.loadConfiguration(this.cfile);

        this.config.options().header("SuperLMS by Stefan923\n");
        this.config.options().copyDefaults(true);
        save();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void save() {
        try {
            this.config.save(this.cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("File 'inventories.yml' couldn't be saved!");
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.cfile);
    }
}

