package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.game.GameStatus;
import ro.kmagic.klms.utils.MessageUtils;
import ro.kmagic.klms.utils.SerializationUtils;

import java.util.List;

public class CommandForceStart extends AbstractCommand implements MessageUtils, SerializationUtils {
    public CommandForceStart(AbstractCommand abstractCommand) {
        super(abstractCommand, false, new String[] { "forcestart" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        FileConfiguration language = instance.getLanguageManager().getConfig();
        GameStatus gameStatus = instance.getGameManager().getStatus();

        if (instance.getGameManager().getStatus().equals(GameStatus.IDLE)) {
            sender.sendMessage(formatAll(language.getString("Command.ForceStart.Game Not Available")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (gameStatus.equals(GameStatus.GRACE) || gameStatus.equals(GameStatus.STARTED)) {
            sender.sendMessage(formatAll(language.getString("Command.ForceStart.Game Already Started")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (instance.getPlayers().size() < 2) {
            sender.sendMessage(formatAll(language.getString("Command.ForceStart.Not Enough Players")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        instance.getGameManager().startGame();
        sender.sendMessage(formatAll(language.getString("Command.ForceStart.Success")));

        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        return null;
    }


    public String getPermissionNode() {
        return "superlms.admin";
    }


    public String getSyntax() {
        return "/lms forceStart";
    }


    public String getDescription() {
        return "Forces a game to start.";
    }
}

