package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.utils.MessageUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandReload extends AbstractCommand implements MessageUtils {
    public CommandReload(AbstractCommand abstractCommand) {
        super(abstractCommand, false, new String[] { "reload" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        if (args.length != 2) {
            return AbstractCommand.ReturnType.SYNTAX_ERROR;
        }
        if (args[1].equalsIgnoreCase("all")) {
            instance.reloadSettingManager();
            instance.reloadLanguageManager();
            sender.sendMessage(formatAll("&8[&2kLMS&8] &fYou have successfully reloaded &aall &fmodules!"));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("settings")) {
            instance.reloadSettingManager();
            sender.sendMessage(formatAll("&8[&2kLMS&8] &fYou have successfully reloaded &asettings &fmodule!"));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (args[1].equalsIgnoreCase("languages")) {
            instance.reloadLanguageManager();
            sender.sendMessage(formatAll("&8[&2kLMS&8] &fYou have successfully reloaded &alanguages &fmodule!"));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        return AbstractCommand.ReturnType.SYNTAX_ERROR;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        if (sender.hasPermission("superlms.admin") && args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            return Stream.of(new String[] { "all", "languages", "settings" }).filter(string -> string.startsWith(args[1].toLowerCase())).collect(Collectors.toList());
        }
        return null;
    }


    public String getPermissionNode() {
        return "superlms.admin";
    }


    public String getSyntax() {
        return "/lms reload <all|settings|language>";
    }


    public String getDescription() {
        return "Reloads plugin settings.";
    }
}

