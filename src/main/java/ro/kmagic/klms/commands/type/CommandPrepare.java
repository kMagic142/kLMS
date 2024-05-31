package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.game.GameStatus;
import ro.kmagic.klms.utils.MessageUtils;
import ro.kmagic.klms.utils.SerializationUtils;

import java.util.List;

public class CommandPrepare extends AbstractCommand implements MessageUtils, SerializationUtils {
    public CommandPrepare(AbstractCommand abstractCommand) {
        super(abstractCommand, false, new String[] { "prepare" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();

        if (!instance.getGameManager().getStatus().equals(GameStatus.IDLE)) {
            sender.sendMessage(formatAll(language.getString("Command.Prepare.Game Already Started")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        sender.sendMessage(formatAll(language.getString("Command.Prepare.Success")));
        instance.getGameManager().waitForPlayers();

        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        return null;
    }


    public String getPermissionNode() {
        return "superlms.admin";
    }


    public String getSyntax() {
        return "/lms prepare";
    }


    public String getDescription() {
        return "Loads the game and prepares it for playing.";
    }
}

