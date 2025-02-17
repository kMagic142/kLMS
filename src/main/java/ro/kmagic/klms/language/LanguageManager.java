package ro.kmagic.klms.language;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ro.kmagic.klms.KLMS;

import java.io.File;
import java.io.IOException;

public class LanguageManager {
    private static LanguageManager instance = new LanguageManager();
    private FileConfiguration config;
    private File cfile;

    public static LanguageManager getInstance() {
        return instance;
    }

    public void setup(KLMS instance) {
        this.cfile = new File(instance.getDataFolder(), "language.yml");
        this.config = YamlConfiguration.loadConfiguration(this.cfile);

        this.config.options().header("kLMS by iMagic142_\n");
        this.config.addDefault("Command.Exit.Not In Game", "&8(&3!&8) &cYou aren't in a Last Man Standing game.");
        this.config.addDefault("Command.ForceStart.Game Already Started", "&8(&3!&8) &cThe game has already started.");
        this.config.addDefault("Command.ForceStart.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        this.config.addDefault("Command.ForceStart.Not Enough Players", "&8(&3!&8) &cThere must be at least &42 players &cto start the game.");
        this.config.addDefault("Command.ForceStart.Success", "&8(&3!&8) &fThe game has been &bforced&f to start.");
        this.config.addDefault("Command.Join.Already Joined", "&8(&3!&8) &cYou have already joined the game.");
        this.config.addDefault("Command.Join.Game Already Started", "&8(&3!&8) &cThe game has already started.");
        this.config.addDefault("Command.Join.Game Is Full", "&8(&3!&8) &cThe game is full.");
        this.config.addDefault("Command.Join.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        this.config.addDefault("Command.Prepare.Game Already Started", "&8(&3!&8) &cThe game can't be prepared, because it has already started.");
        this.config.addDefault("Command.Prepare.Success", "&8(&3!&8) &fThe game has been &bsuccessfully &fprepared.");
        this.config.addDefault("Command.SetKit.Success", "&8(&3!&8) &fYou have successfully changed the game's &bstarting inventory&f.");
        this.config.addDefault("Command.SetLocation.Success", "&8(&3!&8) &fYou have successfully changed &b%location% &flocation.");
        this.config.addDefault("Command.Spectate.Already Joined", "&8(&3!&8) &cYou have already joined the game.");
        this.config.addDefault("Command.Spectate.Already Spectating", "&8(&3!&8) &cYou are already spectating the game.");
        this.config.addDefault("Command.Spectate.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        this.config.addDefault("Command.Spectate.Success", "&8(&3!&8) &fNow, you are &bspectating &fa Last Man Standing game.");
        this.config.addDefault("Command.Stop.Game Not Available", "&8(&3!&8) &cThere is no arena available.");
        this.config.addDefault("Command.Stop.Success", "&8(&3!&8) &fThe game has been &cinterrupted&f by you.");
        this.config.addDefault("Game.Finished", "&8(&3!&8) &fThe game has been won by &b%winner% &fin &3%time%&f.");
        this.config.addDefault("Game.Grace Period Ended", "&8(&3!&8) &fThe &bgrace &fperiod has expired. Now, you can fight other players.");
        this.config.addDefault("Game.Grace Period Ending", "&8(&3!&8) &fThe &bgrace &fperiod will expire in &3%time%&f.");
        this.config.addDefault("Game.Player Died", "&8(&3!&8) &b%player_name% &fhas been killed. &b%current_count% &fplayers remaining!");
        this.config.addDefault("Game.Player Killed", "&8(&3!&8) &b%player_name% &fhas been killed by &b%player_killer%&f. &b%current_count% &fplayers remaining!");
        this.config.addDefault("Game.Started", "&8(&3!&8) &fThe &bLast Man Standing &fgame has started. You have to fight all players to win the game.");
        this.config.addDefault("Game.Starting In", "&8(&3!&8) &fA &bLast Man Standing &fgame is starting in &3%time%&f. Use &3/superlms join&f. &7(&b%current_count%&7/&3%max_count%&7)");
        this.config.addDefault("Game.Waiting For Players", "&8(&3!&8) &fA &bLast Man Standing &fgame is waiting for players. Use &3/superlms join&f. &7(&b%current_count%&7/&3%max_count%&7)");
        this.config.addDefault("Game.Waiting.Player Joined", "&8(&3!&8) &a%player_name% &fjoined the game. &7(&b%current_count%&7/&3%max_count%&7)");
        this.config.addDefault("Game.Waiting.Player Quit", "&8(&3!&8) &c%player_name% &fquit the game. &7(&b%current_count%&7/&3%max_count%&7)");
        this.config.addDefault("General.Blocked Command", "&8(&3!&8) &cYou &4can't &cuse this command right now!");
        this.config.addDefault("General.Must Be Player", "&8(&3!&8) &cYou must be a player to do this!");
        this.config.addDefault("General.Cant Do Player", "&8(&3!&8) &cYou can't do that during LMS!");
        this.config.addDefault("General.Word Day", "day");
        this.config.addDefault("General.Word Days", "days");
        this.config.addDefault("General.Word Hour", "hour");
        this.config.addDefault("General.Word Hours", "hours");
        this.config.addDefault("General.Word Minute", "minute");
        this.config.addDefault("General.Word Minutes", "minutes");
        this.config.addDefault("General.Word Month", "month");
        this.config.addDefault("General.Word Months", "months");
        this.config.addDefault("General.Word Second", "second");
        this.config.addDefault("General.Word Seconds", "seconds");
        this.config.addDefault("General.Word Year", "year");
        this.config.addDefault("General.Word Years", "years");
        this.config.addDefault("General.Invalid Command Syntax", "&8(&3!&8) &cInvalid Syntax or you have no permission!\n&8(&3!&8) &fThe valid syntax is: &b%syntax%");
        this.config.addDefault("General.No Permission", "&8(&3!&8) &cYou need the &4%permission% &cpermission to do that!");
        this.config.options().copyDefaults(true);
        save();
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void reset() {
        this.config.set("General.Invalid Command Syntax", "&8(&3!&8) &cInvalid Syntax or you have no permission!\n&8(&3!&8) &fThe valid syntax is: &b%syntax%");
        this.config.set("General.No Permission", "&8(&3!&8) &cYou need the &4%permission% &cpermission to do that!");
        save();
    }

    private void save() {
        try {
            this.config.save(this.cfile);
        } catch (IOException e) {
            Bukkit.getLogger().severe("File 'language.yml' couldn't be saved!");
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.cfile);
    }
}

