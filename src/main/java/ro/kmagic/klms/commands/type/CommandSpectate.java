package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.game.GameStatus;
import ro.kmagic.klms.utils.MessageUtils;

import java.util.List;

public class CommandSpectate extends AbstractCommand implements MessageUtils {
    public CommandSpectate(AbstractCommand abstractCommand) {
        super(abstractCommand, true, new String[] { "spectate" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        Player player = (Player)sender;
        FileConfiguration language = instance.getLanguageManager().getConfig();
        GameStatus gameStatus = instance.getGameManager().getStatus();

        if (instance.getPlayers().contains(player)) {
            sender.sendMessage(formatAll(language.getString("Command.Spectate.Already Joined")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (instance.getSpectators().contains(player)) {
            sender.sendMessage(formatAll(language.getString("Command.Spectate.Already Spectating")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (gameStatus.equals(GameStatus.IDLE)) {
            player.sendMessage(formatAll(language.getString("Command.Spectate.Game Not Available")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        player.sendMessage(formatAll(language.getString("Command.Spectate.Success")));
        instance.getGameManager().addSpectator(player);
        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        return null;
    }


    public String getPermissionNode() {
        return "superlms.spectate";
    }


    public String getSyntax() {
        return "/lms spectate";
    }


    public String getDescription() {
        return "Spectate a Last Man Stand game.";
    }
}

