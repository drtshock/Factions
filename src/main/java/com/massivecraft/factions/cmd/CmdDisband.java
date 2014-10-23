package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FactionDisbandEvent;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.zcore.util.TL;

import org.bukkit.Bukkit;


public class CmdDisband extends FCommand {

    public CmdDisband() {
        super();
        this.aliases.add("disband");

        //this.requiredArgs.add("");
        this.optionalArgs.put("faction tag", "yours");

        this.permission = Permission.DISBAND.node;
        this.disableOnLock = true;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        // The faction, default to your own.. but null if console sender.
        Faction faction = this.argAsFaction(0, fme == null ? null : myFaction);
        if (faction == null) {
            return;
        }

        boolean isMyFaction = fme == null ? false : faction == myFaction;

        if (isMyFaction) {
            if (!assertMinRole(Role.ADMIN)) {
                return;
            }
        } else {
            if (!Permission.DISBAND_ANY.has(sender, true)) {
                return;
            }
        }

        if (!faction.isNormal()) {
            TLmsg(TL.CMD_DISBAND_NOT_NORMAL, values);
            return;
        }
        if (faction.isPermanent()) {
            TLmsg(TL.CMD_DISBAND_PERMANENT, values);
            return;
        }

        FactionDisbandEvent disbandEvent = new FactionDisbandEvent(me, faction.getId());
        Bukkit.getServer().getPluginManager().callEvent(disbandEvent);
        if (disbandEvent.isCancelled()) {
            return;
        }

        // Send FPlayerLeaveEvent for each player in the faction
        for (FPlayer fplayer : faction.getFPlayers()) {
            Bukkit.getServer().getPluginManager().callEvent(new FPlayerLeaveEvent(fplayer, faction, FPlayerLeaveEvent.PlayerLeaveReason.DISBAND));
        }

        // Inform all players
        for (FPlayer fplayer : FPlayers.i.getOnline()) {
            values.put("who", senderIsConsole ? MiscUtil.capitalizeFirstLetter(TL.A_SERVER_ADMIN.toString()) : fme.describeTo(fplayer));
            if (fplayer.getFaction() == faction) {
                fplayer.TLmsg(TL.CMD_DISBAND_YOUR, values);
            } else {
                values.put("faction", faction.getTag(fplayer));
                fplayer.TLmsg(TL.CMD_DISBAND_OTHER, values);
            }
        }
        if (Conf.logFactionDisband) {
            P.p.log("The faction " + faction.getTag() + " (" + faction.getId() + ") was disbanded by " + (senderIsConsole ? "console command" : fme.getName()) + ".");
        }

        if (Econ.shouldBeUsed() && !senderIsConsole) {
            //Give all the faction's money to the disbander
            double amount = Econ.getBalance(faction.getAccountId());
            Econ.transferMoney(fme, faction, fme, amount, false);

            if (amount > 0.0) {
                String amountString = Econ.moneyString(amount);
                values.put("total", amountString);
                TLmsg(TL.CMD_DISBAND_BANK, values);
                P.p.log(fme.getName() + " has been given bank holdings of " + amountString + " from disbanding " + faction.getTag() + ".");
            }
        }

        faction.detach();
    }
}
