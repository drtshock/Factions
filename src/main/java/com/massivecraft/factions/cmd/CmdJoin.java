package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import org.bukkit.Bukkit;

public class CmdJoin extends FCommand {

    public CmdJoin() {
        super();
        this.aliases.add("join");

        this.requiredArgs.add("faction name");
        this.optionalArgs.put("player", "you");

        this.permission = Permission.JOIN.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        Faction faction = this.argAsFaction(0);
        if (faction == null) {
            return;
        }

        FPlayer fplayer = this.argAsBestFPlayerMatch(1, fme, false);
        boolean samePlayer = fplayer == fme;
        values.put("player", fplayer.describeTo(faction, true));
        values.put("target", fplayer.describeTo(fme, false));
        values.put("targetUC", fplayer.describeTo(fme, true));
        values.put("is-are", samePlayer ? TL.ARE.toString() : TL.IS.toString());
        values.put("your-their", samePlayer ? TL.YOUR.toString() : TL.THEIR.toString());
        values.put("faction", faction.getTag(fme));
        values.put("memberlimit", String.valueOf(Conf.factionMemberLimit));

        if (!samePlayer && !Permission.JOIN_OTHERS.has(sender, false)) {
            TLmsg(TL.CMD_JOIN_OTHERS_PERM, values);
            return;
        }

        if (!faction.isNormal()) {
            TLmsg(TL.CMD_JOIN_SYSTEM, values);
            return;
        }

        if (faction == fplayer.getFaction()) {
            TLmsg(TL.CMD_JOIN_ALREADY_MEMBER, values);
            return;
        }

        if (Conf.factionMemberLimit > 0 && faction.getFPlayers().size() >= Conf.factionMemberLimit) {
            TLmsg(TL.CMD_JOIN_LIMIT, values);
            return;
        }

        if (fplayer.hasFaction()) {
            TLmsg(TL.CMD_JOIN_MUST_LEAVE, values);
            return;
        }

        if (!Conf.canLeaveWithNegativePower && fplayer.getPower() < 0) {
            TLmsg(TL.CMD_JOIN_NEG_POWER, values);
            return;
        }

        if (!(faction.getOpen() || faction.isInvited(fplayer) || fme.isAdminBypassing() || Permission.JOIN_ANY.has(sender, false))) {
            TLmsg(TL.CMD_JOIN_REQUIRES_INVITE, values);
            if (samePlayer) {
                faction.TLmsg(TL.CMD_JOIN_TRIED_NOTIFY, values);
            }
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make sure they can pay
        if (samePlayer && !canAffordCommand(Conf.econCostJoin, "to join a faction")) {
            return;
        }

        // trigger the join event (cancellable)
        FPlayerJoinEvent joinEvent = new FPlayerJoinEvent(FPlayers.i.get(me), faction, FPlayerJoinEvent.PlayerJoinReason.COMMAND);
        Bukkit.getServer().getPluginManager().callEvent(joinEvent);
        if (joinEvent.isCancelled()) {
            return;
        }

        // then make 'em pay (if applicable)
        if (samePlayer && !payForCommand(Conf.econCostJoin, "to join a faction", "for joining a faction")) {
            return;
        }

        fme.TLmsg(TL.CMD_JOIN_SUCCESS, values);

        if (!samePlayer) {
            fplayer.TLmsg(TL.CMD_JOIN_MOVED, values);
        }
        faction.TLmsg(TL.CMD_JOIN_YOUR, values);

        fplayer.resetFactionData();
        fplayer.setFaction(faction);
        faction.deinvite(fplayer);

        if (Conf.logFactionJoin) {
            if (samePlayer) {
                P.p.log("%s joined the faction %s.", fplayer.getName(), faction.getTag());
            } else {
                P.p.log("%s moved the player %s into the faction %s.", fme.getName(), fplayer.getName(), faction.getTag());
            }
        }
    }
}
