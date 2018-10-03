package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.tabcomplete.FactionTabCompleter;
import com.massivecraft.factions.cmd.tabcomplete.TabCompleteProvider;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.MCommand;
import com.massivecraft.factions.zcore.util.TL;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public abstract class FCommand extends MCommand<P> implements FactionTabCompleter {

    public SimpleDateFormat sdf = new SimpleDateFormat(TL.DATE_FORMAT.toString());

    public CommandRequirements requirements;

    // Legacy, To be moved to requirements SoonTM
    public boolean disableOnLock;
    public boolean isMoneyCommand;

    public FCommand() {
        super(P.p);

        requirements = new CommandRequirements.Builder(null).build();

        // Due to safety reasons it defaults to disable on lock.
        disableOnLock = true;
        // The money commands must be disabled if money should not be used.
        isMoneyCommand = false;
    }

    public abstract void perform(CommandContext context);

    @Override
    public void execute(CommandSender sender, List<String> args, List<MCommand<?>> commandChain) {
        // Is there a matching sub command?
        if (args.size() > 0) {
            for (MCommand<?> subCommand : this.subCommands) {
                if (subCommand.aliases.contains(args.get(0).toLowerCase())) {
                    args.remove(0);
                    commandChain.add(this);
                    subCommand.execute(sender, args, commandChain);
                    return;
                }
            }
        }

        if (!validCall(sender, args)) {
            return;
        }

        if (!this.isEnabled(sender)) {
            return;
        }

        perform(new CommandContext(sender, args, aliases.get(0)));
    }

    @Override
    public TabCompleteProvider onTabComplete(CommandContext context, String[] args) {
        return new TabCompleteProvider() {
            @Override
            public List<String> get() {
                return Collections.emptyList();
            }
        };
    }

    @Override
    public boolean validCall(CommandSender sender, List<String> args) {
        return requirements.computeRequirements(new CommandContext(sender, args, aliases.get(0)), true) && validArgs(args, sender);
    }

    @Override
    public boolean isEnabled(CommandSender sender) {
        if (p.getLocked() && this.disableOnLock) {
            msg(sender, "<b>Factions was locked by an admin. Please try again later.");
            return false;
        }

        if (this.isMoneyCommand && !Conf.econEnabled) {
            msg(sender, "<b>Faction economy features are disabled on this server.");
            return false;
        }

        if (this.isMoneyCommand && !Conf.bankEnabled) {
            msg(sender, "<b>The faction bank system is disabled on this server.");
            return false;
        }

        return true;
    }

    /*
    // -------------------------------------------- //
    // Assertions
    // -------------------------------------------- //

    public boolean assertHasFaction() {
        if (me == null) {
            return true;
        }

        if (!fme.hasFaction()) {
            sendMessage("You are not member of any faction.");
            return false;
        }
        return true;
    }

    public boolean assertMinRole(Role role) {
        if (me == null) {
            return true;
        }

        if (fme.getRole().value < role.value) {
            msg("<b>You <h>must be " + role + "<b> to " + this.getHelpShort() + ".");
            return false;
        }
        return true;
    }

    // -------------------------------------------- //
    // Commonly used logic
    // -------------------------------------------- //

    public boolean canIAdministerYou(FPlayer i, FPlayer you) {
        if (!i.getFaction().equals(you.getFaction())) {
            i.sendMessage(p.txt.parse("%s <b>is not in the same faction as you.", you.describeTo(i, true)));
            return false;
        }

        if (i.getRole().value > you.getRole().value || i.getRole().equals(Role.ADMIN)) {
            return true;
        }

        if (you.getRole().equals(Role.ADMIN)) {
            i.sendMessage(p.txt.parse("<b>Only the faction admin can do that."));
        } else if (i.getRole().equals(Role.MODERATOR)) {
            if (i == you) {
                return true; //Moderators can control themselves
            } else {
                i.sendMessage(p.txt.parse("<b>Moderators can't control each other..."));
            }
        } else {
            i.sendMessage(p.txt.parse("<b>You must be a faction moderator to do that."));
        }

        return false;
    }

    // if economy is enabled and they're not on the bypass list, make 'em pay; returns true unless person can't afford the cost
    public boolean payForCommand(double cost, String toDoThis, String forDoingThis) {
        if (!Econ.shouldBeUsed() || this.fme == null || cost == 0.0 || fme.isAdminBypassing()) {
            return true;
        }

        if (Conf.bankEnabled && Conf.bankFactionPaysCosts && fme.hasFaction()) {
            return Econ.modifyMoney(myFaction, -cost, toDoThis, forDoingThis);
        } else {
            return Econ.modifyMoney(fme, -cost, toDoThis, forDoingThis);
        }
    }

    public boolean payForCommand(double cost, TL toDoThis, TL forDoingThis) {
        return payForCommand(cost, toDoThis.toString(), forDoingThis.toString());
    }

    // like above, but just make sure they can pay; returns true unless person can't afford the cost
    public boolean canAffordCommand(double cost, String toDoThis) {
        if (!Econ.shouldBeUsed() || this.fme == null || cost == 0.0 || fme.isAdminBypassing()) {
            return true;
        }

        if (Conf.bankEnabled && Conf.bankFactionPaysCosts && fme.hasFaction()) {
            return Econ.hasAtLeast(myFaction, cost, toDoThis);
        } else {
            return Econ.hasAtLeast(fme, cost, toDoThis);
        }
    }

    public void doWarmUp(WarmUpUtil.Warmup warmup, TL translationKey, String action, Runnable runnable, long delay) {
        this.doWarmUp(this.fme, warmup, translationKey, action, runnable, delay);
    }

    public void doWarmUp(FPlayer player, WarmUpUtil.Warmup warmup, TL translationKey, String action, Runnable runnable, long delay) {
        WarmUpUtil.process(player, warmup, translationKey, action, runnable, delay);
    }*/


    public List<String> getToolTips(FPlayer player) {
        List<String> lines = new ArrayList<>();
        for (String s : p.getConfig().getStringList("tooltips.show")) {
            lines.add(ChatColor.translateAlternateColorCodes('&', replaceFPlayerTags(s, player)));
        }
        return lines;
    }

    public List<String> getToolTips(Faction faction) {
        List<String> lines = new ArrayList<>();
        for (String s : p.getConfig().getStringList("tooltips.list")) {
            lines.add(ChatColor.translateAlternateColorCodes('&', replaceFactionTags(s, faction)));
        }
        return lines;
    }

    public String replaceFPlayerTags(String s, FPlayer player) {
        if (s.contains("{balance}")) {
            String balance = Econ.isSetup() ? Econ.getFriendlyBalance(player) : "no balance";
            s = s.replace("{balance}", balance);
        }
        if (s.contains("{lastSeen}")) {
            String humanized = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - player.getLastLoginTime(), true, true) + " ago";
            String lastSeen = player.isOnline() ? ChatColor.GREEN + "Online" : (System.currentTimeMillis() - player.getLastLoginTime() < 432000000 ? ChatColor.YELLOW + humanized : ChatColor.RED + humanized);
            s = s.replace("{lastSeen}", lastSeen);
        }
        if (s.contains("{power}")) {
            String power = player.getPowerRounded() + "/" + player.getPowerMaxRounded();
            s = s.replace("{power}", power);
        }
        if (s.contains("{group}")) {
            String group = P.p.getPrimaryGroup(Bukkit.getOfflinePlayer(UUID.fromString(player.getId())));
            s = s.replace("{group}", group);
        }
        return s;
    }

    public String replaceFactionTags(String s, Faction faction) {
        if (s.contains("{power}")) {
            s = s.replace("{power}", String.valueOf(faction.getPowerRounded()));
        }
        if (s.contains("{maxPower}")) {
            s = s.replace("{maxPower}", String.valueOf(faction.getPowerMaxRounded()));
        }
        if (s.contains("{leader}")) {
            FPlayer fLeader = faction.getFPlayerAdmin();
            String leader = fLeader == null ? "Server" : fLeader.getName().substring(0, fLeader.getName().length() > 14 ? 13 : fLeader.getName().length());
            s = s.replace("{leader}", leader);
        }
        if (s.contains("{chunks}")) {
            s = s.replace("{chunks}", String.valueOf(faction.getLandRounded()));
        }
        if (s.contains("{members}")) {
            s = s.replace("{members}", String.valueOf(faction.getSize()));

        }
        if (s.contains("{online}")) {
            s = s.replace("{online}", String.valueOf(faction.getOnlinePlayers().size()));
        }
        return s;
    }


}
