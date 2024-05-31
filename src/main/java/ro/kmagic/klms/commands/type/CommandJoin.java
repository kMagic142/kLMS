package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.game.GameStatus;
import ro.kmagic.klms.utils.MessageUtils;

import java.util.List;

public class CommandJoin extends AbstractCommand implements MessageUtils {
    public CommandJoin(AbstractCommand abstractCommand) {
        super(abstractCommand, true, new String[] { "join" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        Player player = (Player)sender;
        FileConfiguration language = instance.getLanguageManager().getConfig();
        GameStatus gameStatus = instance.getGameManager().getStatus();

        if (instance.getPlayers().contains(player)) {
            sender.sendMessage(formatAll(language.getString("Command.Join.Already Joined")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (gameStatus.equals(GameStatus.GRACE) || gameStatus.equals(GameStatus.STARTED)) {
            player.sendMessage(formatAll(language.getString("Command.Join.Game Already Started")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (gameStatus.equals(GameStatus.IDLE)) {
            player.sendMessage(formatAll(language.getString("Command.Join.Game Not Available")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        if (instance.getPlayers().size() >= instance.getSettingsManager().getConfig().getInt("Game.Maximum Player Count")) {
            player.sendMessage(formatAll(language.getString("Command.Join.Game Is Full")));
            return AbstractCommand.ReturnType.SUCCESS;
        }

        instance.getGameManager().addPlayer(player);
        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        return null;
    }


    public String getPermissionNode() {
        return "superlms.join";
    }


    public String getSyntax() {
        return "/lms join";
    }


    public String getDescription() {
        return "Join a Last Man Stand game.";
    }
}

