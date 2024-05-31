package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.utils.MessageUtils;
import ro.kmagic.klms.utils.SerializationUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandSetLocation extends AbstractCommand implements MessageUtils, SerializationUtils {
    public CommandSetLocation(AbstractCommand abstractCommand) {
        super(abstractCommand, true, new String[] { "setlocation" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        if (args.length != 2) {
            return AbstractCommand.ReturnType.SYNTAX_ERROR;
        }

        FileConfiguration settings = instance.getSettingsManager().getConfig();
        FileConfiguration language = instance.getLanguageManager().getConfig();

        if (args[1].equalsIgnoreCase("arena")) {
            settings.set("Game.Locations.Arena", serializeLocation(((Player)sender).getLocation()));
            instance.getSettingsManager().save();
            sender.sendMessage(formatAll(language.getString("Command.SetLocation.Success").replace("%location%", "arena")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("lobby")) {
            settings.set("Game.Locations.Lobby", serializeLocation(((Player)sender).getLocation()));
            instance.getSettingsManager().save();
            sender.sendMessage(formatAll(language.getString("Command.SetLocation.Success").replace("%location%", "lobby")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("spawn")) {
            settings.set("Game.Locations.Spawn", serializeLocation(((Player)sender).getLocation()));
            instance.getSettingsManager().save();
            sender.sendMessage(formatAll(language.getString("Command.SetLocation.Success").replace("%location%", "spawn")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        return AbstractCommand.ReturnType.SYNTAX_ERROR;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        if (sender.hasPermission("superlms.admin") && args.length == 2 && args[0].equalsIgnoreCase("setlocation")) {
            return Stream.of(new String[] { "arena", "lobby", "spawn" }).filter(string -> string.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
        }
        return null;
    }


    public String getPermissionNode() {
        return "superlms.admin";
    }


    public String getSyntax() {
        return "/lms setLocation <arena|lobby|spawn>";
    }


    public String getDescription() {
        return "Sets default locations.";
    }
}

