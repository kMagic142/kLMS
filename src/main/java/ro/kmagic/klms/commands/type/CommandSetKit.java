package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.utils.MessageUtils;
import ro.kmagic.klms.utils.SerializationUtils;

import java.util.List;

public class CommandSetKit extends AbstractCommand implements MessageUtils, SerializationUtils {
    public CommandSetKit(AbstractCommand abstractCommand) {
        super(abstractCommand, true, new String[] { "setkit" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        FileConfiguration settings = instance.getSettingsManager().getConfig();
        FileConfiguration language = instance.getLanguageManager().getConfig();

        Player player = (Player)sender;

        settings.set("Game.Kit.Inventory", itemStackArrayToBase64(player.getInventory().getContents()));
        settings.set("Game.Kit.Armor", itemStackArrayToBase64(player.getInventory().getArmorContents()));
        instance.getSettingsManager().save();

        sender.sendMessage(formatAll(language.getString("Command.SetKit.Success")));

        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        return null;
    }


    public String getPermissionNode() {
        return "superlms.admin";
    }


    public String getSyntax() {
        return "/lms setKit";
    }


    public String getDescription() {
        return "Sets starting kit for the Last Man Standing game.";
    }
}

