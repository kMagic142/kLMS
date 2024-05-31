package ro.kmagic.klms.commands.type;

import org.bukkit.command.CommandSender;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.AbstractCommand;
import ro.kmagic.klms.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandLMS extends AbstractCommand implements MessageUtils {
    public CommandLMS() {
        super(null, false, new String[] { "lms" });
    }


    protected AbstractCommand.ReturnType runCommand(KLMS instance, CommandSender sender, String... args) {
        sender.sendMessage(formatAll(" "));
        sendCenteredMessage(sender, formatAll("&8&m--+----------------------------------------+--&r"));
        sendCenteredMessage(sender, formatAll("&2&lkLMS &f&lv" + instance.getDescription().getVersion()));
        sendCenteredMessage(sender, formatAll("&8&l&fPlugin by: &akMagic"));
        sendCenteredMessage(sender, formatAll(" "));
        sendCenteredMessage(sender, formatAll("&8&l&fAdds the Last Man Standing game to Minecraft."));
        sendCenteredMessage(sender, formatAll("&8&m--+----------------------------------------+--&r"));
        sender.sendMessage(formatAll(" "));

        return AbstractCommand.ReturnType.SUCCESS;
    }


    protected List<String> onTab(KLMS instance, CommandSender sender, String... args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (sender.hasPermission("superlms.exit") && "exit".startsWith(args[0].toLowerCase())) {
                list.add("exit");
            }
            if (sender.hasPermission("superlms.join") && "join".startsWith(args[0].toLowerCase())) {
                list.add("join");
            }
            if (sender.hasPermission("superlms.spectate") && "spectate".startsWith(args[0].toLowerCase())) {
                list.add("spectate");
            }
            if (sender.hasPermission("superlms.admin")) {
                list.addAll(Stream.of(new String[] { "forcestart", "prepare", "reload", "setkit", "setlocation", "stop" }).filter(string -> string.startsWith(args[0].toLowerCase())).collect(Collectors.toList()));
            }
            return list.isEmpty() ? null : list;
        }

        return null;
    }


    public String getPermissionNode() {
        return null;
    }


    public String getSyntax() {
        return "/lms";
    }


    public String getDescription() {
        return "Displays plugin info";
    }
}

