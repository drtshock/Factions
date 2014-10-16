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

        if (!samePlayer && !Permission.JOIN_OTHERS.has(sender, false)) {
            msg(TL.CMD_JOIN_OTHERS_PERM.toString());
            return;
        }

        if (!faction.isNormal()) {
            msg(TL.CMD_JOIN_SYSTEM.toString());
            return;
        }

        if (faction == fplayer.getFaction()) {
            msg(TL.CMD_JOIN_ALREADY_MEMBER.toString(), fplayer.describeTo(fme, true), (samePlayer ? TL.ARE.toString() : TL.IS.toString()), faction.getTag(fme));
            return;
        }

        if (Conf.factionMemberLimit > 0 && faction.getFPlayers().size() >= Conf.factionMemberLimit) {
            msg(TL.CMD_JOIN_LIMIT.toString(), faction.getTag(fme), Conf.factionMemberLimit, fplayer.describeTo(fme, false));
            return;
        }

        if (fplayer.hasFaction()) {
            msg(TL.CMD_JOIN_MUST_LEAVE.toString(), fplayer.describeTo(fme, true), (samePlayer ? TL.YOUR.toString() : TL.THEIR.toString()));
            return;
        }

        if (!Conf.canLeaveWithNegativePower && fplayer.getPower() < 0) {
            msg(TL.CMD_JOIN_NEG_POWER.toString(), fplayer.describeTo(fme, true));
            return;
        }

        if (!(faction.getOpen() || faction.isInvited(fplayer) || fme.isAdminBypassing() || Permission.JOIN_ANY.has(sender, false))) {
            msg(TL.CMD_JOIN_REQUIRES_INVITE.toString());
            if (samePlayer) {
                faction.msg(TL.CMD_JOIN_TRIED_NOTIFY.toString(), fplayer.describeTo(faction, true));
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

        fme.msg(TL.CMD_JOIN_SUCCESS.toString(), fplayer.describeTo(fme, true), faction.getTag(fme));

        if (!samePlayer) {
            fplayer.msg(TL.CMD_JOIN_MOVED.toString(), fme.describeTo(fplayer, true), faction.getTag(fplayer));
        }
        faction.msg(TL.CMD_JOIN_YOUR.toString(), fplayer.describeTo(faction, true));

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
