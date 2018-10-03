package com.massivecraft.factions.zcore;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.util.TextUtil;
import mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;


public abstract class MCommand<T extends MPlugin> {

    public T p;

    // The sub-commands to this command
    public List<MCommand<?>> subCommands;

    public void addSubCommand(MCommand<?> subCommand) {
        subCommand.commandChain.addAll(this.commandChain);
        subCommand.commandChain.add(this);
        this.subCommands.add(subCommand);
    }

    // The different names this commands will react to
    public List<String> aliases;
    public boolean allowNoSlashAccess;

    // Information on the args
    public List<String> requiredArgs;
    public LinkedHashMap<String, String> optionalArgs;
    public boolean errorOnToManyArgs = true;

    // FIELD: Help Short
    // This field may be left blank and will in such case be loaded from the permissions node instead.
    // Thus make sure the permissions node description is an action description like "eat hamburgers" or "do admin stuff".
    private String helpShort;

    public void setHelpShort(String val) {
        this.helpShort = val;
    }

    public String getHelpShort() {
        if (this.helpShort == null) {
            return getUsageTranslation().toString();
        }

        return this.helpShort;
    }

    public abstract TL getUsageTranslation();

    public List<String> helpLong;
    public CommandVisibility visibility;

    // Legacy stuff, will probably change later
    public List<MCommand<?>> commandChain = new ArrayList<>(); // The command chain used to execute this command

    public MCommand(T p) {
        this.p = p;

        this.allowNoSlashAccess = false;

        this.subCommands = new ArrayList<>();
        this.aliases = new ArrayList<>();

        this.requiredArgs = new ArrayList<>();
        this.optionalArgs = new LinkedHashMap<>();

        this.helpShort = null;
        this.helpLong = new ArrayList<>();
        this.visibility = CommandVisibility.VISIBLE;
    }

    public abstract void execute(CommandSender sender, List<String> args, List<MCommand<?>> commandChain);

    // Use as a bridge towards FCommand
    public void execute(CommandSender sender, List<String> args) {
        execute(sender, args, new ArrayList<MCommand<?>>());
    }

    // -------------------------------------------- //
    // Call Validation
    // -------------------------------------------- //

    /**
     * In this method we validate that all prerequisites to perform this command has been met.
     */
    // TODO: There should be a boolean for silence
    public boolean validCall(CommandSender sender, List<String> args) {
        return validArgs(args, sender);
    }

    public boolean isEnabled(CommandSender sender) {
        return true;
    }

    public boolean validArgs(List<String> args, CommandSender sender) {
        if (args.size() < this.requiredArgs.size()) {
            if (sender != null) {
                msg(sender, TL.GENERIC_ARGS_TOOFEW);
                sender.sendMessage(this.getUseageTemplate());
            }
            return false;
        }

        if (args.size() > this.requiredArgs.size() + this.optionalArgs.size() && this.errorOnToManyArgs) {
            if (sender != null) {
                // Get the to many string slice
                List<String> theToMany = args.subList(this.requiredArgs.size() + this.optionalArgs.size(), args.size());
                msg(sender, TL.GENERIC_ARGS_TOOMANY, TextUtil.implode(theToMany, " "));
                sender.sendMessage(this.getUseageTemplate());
            }
            return false;
        }
        return true;
    }

    // -------------------------------------------- //
    // Help and Usage information
    // -------------------------------------------- //

    public String getUseageTemplate(List<MCommand<?>> commandChain, boolean addShortHelp) {
        StringBuilder ret = new StringBuilder();
        ret.append(p.txt.parseTags("<c>"));
        ret.append('/');

        for (MCommand<?> mc : commandChain) {
            ret.append(TextUtil.implode(mc.aliases, ","));
            ret.append(' ');
        }

        ret.append(TextUtil.implode(this.aliases, ","));

        List<String> args = new ArrayList<>();

        for (String requiredArg : this.requiredArgs) {
            args.add("<" + requiredArg + ">");
        }

        for (Entry<String, String> optionalArg : this.optionalArgs.entrySet()) {
            String val = optionalArg.getValue();
            if (val == null) {
                val = "";
            } else {
                val = "=" + val;
            }
            args.add("[" + optionalArg.getKey() + val + "]");
        }

        if (args.size() > 0) {
            ret.append(p.txt.parseTags("<p> "));
            ret.append(TextUtil.implode(args, " "));
        }

        if (addShortHelp) {
            ret.append(p.txt.parseTags(" <i>"));
            ret.append(this.getHelpShort());
        }

        return ret.toString();
    }

    public String getUseageTemplate(boolean addShortHelp) {
        return getUseageTemplate(this.commandChain, addShortHelp);
    }

    public String getUseageTemplate() {
        return getUseageTemplate(false);
    }

    // -------------------------------------------- //
    // Message Sending Helpers
    // -------------------------------------------- //

    public void msg(CommandSender sender, String str, Object... args) {
        sender.sendMessage(p.txt.parse(str, args));
    }

    public void msg(CommandSender sender, TL translation, Object... args) {
        sender.sendMessage(p.txt.parse(translation.toString(), args));
    }

    public void sendMessage(CommandSender sender, String msg) {
        sender.sendMessage(msg);
    }

    public void sendMessage(CommandSender sender, List<String> msgs) {
        for (String msg : msgs) {
            this.sendMessage(sender, msg);
        }
    }

    public void sendFancyMessage(CommandSender sender, FancyMessage message) {
        message.send(sender);
    }

    public void sendFancyMessage(CommandSender sender, List<FancyMessage> messages) {
        for (FancyMessage m : messages) {
            sendFancyMessage(sender, m);
        }
    }

}
