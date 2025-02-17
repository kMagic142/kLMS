package ro.kmagic.klms.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.commands.type.*;
import ro.kmagic.klms.exceptions.MissingPermissionException;
import ro.kmagic.klms.utils.MessageUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager implements CommandExecutor, MessageUtils {
    private static final List<AbstractCommand> commands = new ArrayList<>();
    private final KLMS plugin;

    public CommandManager(KLMS plugin) {
        this.plugin = plugin;
        TabManager tabManager = new TabManager(this);

        plugin.getCommand("lms").setExecutor(this);
        AbstractCommand commandSuperLMS = addCommand(new CommandLMS());

        addCommand(new CommandExit(commandSuperLMS));
        addCommand(new CommandForceStart(commandSuperLMS));
        addCommand(new CommandJoin(commandSuperLMS));
        addCommand(new CommandPrepare(commandSuperLMS));
        addCommand(new CommandReload(commandSuperLMS));
        addCommand(new CommandSetKit(commandSuperLMS));
        addCommand(new CommandSetLocation(commandSuperLMS));
        addCommand(new CommandSpectate(commandSuperLMS));
        addCommand(new CommandStop(commandSuperLMS));

        for (AbstractCommand abstractCommand : commands) {
            if (abstractCommand.getParent() != null)
                continue;  plugin.getCommand(abstractCommand.getCommand()).setTabCompleter(tabManager);
        }
    }

    private AbstractCommand addCommand(AbstractCommand abstractCommand) {
        commands.add(abstractCommand);
        return abstractCommand;
    }


    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        for (AbstractCommand abstractCommand : commands) {
            if (abstractCommand.getCommand() != null && abstractCommand.getCommand().equalsIgnoreCase(command.getName().toLowerCase())) {
                if (strings.length == 0 || abstractCommand.hasArgs()) {
                    processRequirements(abstractCommand, commandSender, strings);
                    return true;
                }  continue;
            }  if (strings.length != 0 && abstractCommand.getParent() != null && abstractCommand.getParent().getCommand().equalsIgnoreCase(command.getName())) {
                String cmd = strings[0];
                String cmd2 = (strings.length >= 2) ? String.join(" ", strings[0], strings[1]) : null;
                for (String cmds : abstractCommand.getSubCommand()) {
                    if (cmd.equalsIgnoreCase(cmds) || (cmd2 != null && cmd2.equalsIgnoreCase(cmds))) {
                        processRequirements(abstractCommand, commandSender, strings);
                        return true;
                    }
                }
            }
        }
        commandSender.sendMessage(formatAll("&8[&2kLMS&8] &cComanda pe care ai introdus-o nu exista!"));
        return true;
    }

    private void processRequirements(AbstractCommand command, CommandSender sender, String[] strings) {
        FileConfiguration language = this.plugin.getLanguageManager().getConfig();
        if (sender instanceof org.bukkit.entity.Player) {
            String permissionNode = command.getPermissionNode();
            if (permissionNode == null || sender.hasPermission(command.getPermissionNode())) {
                AbstractCommand.ReturnType returnType = null;
                try {
                    returnType = command.runCommand(this.plugin, sender, strings);
                } catch (MissingPermissionException e) {
                    sender.sendMessage(formatAll(language.getString("General.No Permission").replace("%permission%", e.getMessage())));
                }
                if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                    sender.sendMessage(formatAll(language.getString("General.Invalid Command Syntax").replace("%syntax%", command.getSyntax())));
                }
                return;
            }
            sender.sendMessage(formatAll(language.getString("General.No Permission").replace("%permission%", permissionNode)));
            return;
        }
        if (command.isNoConsole()) {
            sender.sendMessage(formatAll(language.getString("General.Must Be Player")));
            return;
        }
        if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
            AbstractCommand.ReturnType returnType = null;
            try {
                returnType = command.runCommand(this.plugin, sender, strings);
            } catch (MissingPermissionException e) {
                e.printStackTrace();
            }
            if (returnType == AbstractCommand.ReturnType.SYNTAX_ERROR) {
                sender.sendMessage(formatAll(language.getString("General.Invalid Command Syntax").replace("%syntax%", command.getSyntax())));
            }
            return;
        }
        sender.sendMessage(formatAll("&8[&2kLMS&8] &cNu ai permisiunea de a face asta!"));
    }

    public List<AbstractCommand> getCommands() {
        return Collections.unmodifiableList(commands);
    }
}

