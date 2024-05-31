package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.utils.MessageUtils;

import java.util.List;

public class CommandExit extends AbstractCommand implements MessageUtils {
    public CommandExit(AbstractCommand abstractCommand) {
        super(abstractCommand, true, new String[] { "exit" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();

        Player player = (Player)sender;

        if (instance.getPlayers().contains(player)) {
            instance.getGameManager().removePlayer(player);
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (instance.getSpectators().contains(player)) {
            instance.getGameManager().removeSpectator(player);
            return AbstractCommand.ReturnType.SUCCESS;
        }

        sender.sendMessage(formatAll(language.getString("Command.Exit.Not In Game")));
        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        return null;
    }


    public String getPermissionNode() {
        return "superlms.exit";
    }


    public String getSyntax() {
        return "/lms exit";
    }


    public String getDescription() {
        return "Exit current Last Man Stand game.";
    }
}

