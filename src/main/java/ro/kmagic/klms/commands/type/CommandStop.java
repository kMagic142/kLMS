package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.game.GameStatus;
import ro.kmagic.klms.utils.MessageUtils;
import ro.kmagic.klms.utils.SerializationUtils;

import java.util.List;

public class CommandStop extends AbstractCommand implements MessageUtils, SerializationUtils {
    public CommandStop(AbstractCommand abstractCommand) {
        super(abstractCommand, false, new String[] { "stop" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();

        if (instance.getGameManager().getStatus().equals(GameStatus.IDLE)) {
            sender.sendMessage(formatAll(language.getString("Command.Stop.Game Not Available")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        instance.getGameManager().forceEndGame();
        sender.sendMessage(formatAll(language.getString("Command.Stop.Success")));

        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        return null;
    }


    public String getPermissionNode() {
        return "superlms.admin";
    }


    public String getSyntax() {
        return "/lms stop";
    }


    public String getDescription() {
        return "Stops a starting or already started game.";
    }
}

