package ro.kmagic.klms.commands;

import org.bukkit.command.CommandSender;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.exceptions.MissingPermissionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractCommand {
    private final boolean noConsole;
    private AbstractCommand parent = null;

    private boolean hasArgs = false;
    private String command;
    private List<String> subCommand = new ArrayList<>();

    protected AbstractCommand(AbstractCommand parent, boolean noConsole, String... command) {
        if (parent != null) {
            this.subCommand = Arrays.asList(command);
        } else {
            this.command = Arrays.asList(command).get(0);
        }
        this.parent = parent;
        this.noConsole = noConsole;
    }

    protected AbstractCommand(boolean noConsole, boolean hasArgs, String... command) {
        this.command = Arrays.asList(command).get(0);

        this.hasArgs = hasArgs;
        this.noConsole = noConsole;
    }

    public AbstractCommand getParent() {
        return this.parent;
    }

    public String getCommand() {
        return this.command;
    }

    public List<String> getSubCommand() {
        return this.subCommand;
    }

    protected abstract ReturnType runCommand(KLMS paramSuperLMS, CommandSender paramCommandSender, String... paramVarArgs) throws MissingPermissionException;

    protected abstract List<String> onTab(KLMS paramSuperLMS, CommandSender paramCommandSender, String... paramVarArgs);

    public abstract String getPermissionNode();

    public abstract String getSyntax();

    public abstract String getDescription();

    public boolean hasArgs() {
        return this.hasArgs;
    }

    public boolean isNoConsole() {
        return this.noConsole;
    }

    public enum ReturnType { SUCCESS, SYNTAX_ERROR; }

}

