package ro.kmagic.klms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ro.kmagic.klms.KLMS;

import java.util.ArrayList;
import java.util.List;

public class TabManager implements TabCompleter {
    private final CommandManager commandManager;

    TabManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }


    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] strings) {
        KLMS instance = KLMS.getInstance();

        List<String> tabStrings = new ArrayList<>();
        for (AbstractCommand abstractCommand : this.commandManager.getCommands()) {
            List<String> tempStrings = abstractCommand.onTab(instance, sender, strings);
            if (tempStrings != null) {
                tabStrings.addAll(tempStrings);
            }
        }
        return tabStrings;
    }
}

