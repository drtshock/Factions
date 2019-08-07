package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;

public class CmdJoin extends FCommand {

    public CmdJoin() {
        super();
        this.aliases.add("join");

        this.requiredArgs.add("faction");
        this.optionalArgs.put("player", "you");

        this.requirements = new CommandRequirements.Builder(Permission.JOIN)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction faction = context.argAsFaction(0);
        if (faction == null) {
            return;
        }

        FPlayer fplayer = context.argAsBestFPlayerMatch(1, context.fPlayer, false);
        boolean samePlayer = fplayer == context.fPlayer;

        if (!samePlayer && !Permission.JOIN_OTHERS.has(context.sender, false)) {
            context.msg(TL.COMMAND_JOIN_CANNOTFORCE);
            return;
        }

        if (!faction.isNormal()) {
            context.msg(TL.COMMAND_JOIN_SYSTEMFACTION);
            return;
        }

        if (faction == fplayer.getFaction()) {
            //TODO:TL
            context.msg(TL.COMMAND_JOIN_ALREADYMEMBER, fplayer.describeTo(context.fPlayer, true), (samePlayer ? "are" : "is"), faction.getTag(context.fPlayer));
            return;
        }

        if (P.p.conf().factions().getFactionMemberLimit() > 0 && faction.getFPlayers().size() >= P.p.conf().factions().getFactionMemberLimit()) {
            context.msg(TL.COMMAND_JOIN_ATLIMIT, faction.getTag(context.fPlayer), P.p.conf().factions().getFactionMemberLimit(), fplayer.describeTo(context.fPlayer, false));
            return;
        }

        if (fplayer.hasFaction()) {
            //TODO:TL
            context.msg(TL.COMMAND_JOIN_INOTHERFACTION, fplayer.describeTo(context.fPlayer, true), (samePlayer ? "your" : "their"));
            return;
        }

        if (!P.p.conf().factions().isCanLeaveWithNegativePower() && fplayer.getPower() < 0) {
            context.msg(TL.COMMAND_JOIN_NEGATIVEPOWER, fplayer.describeTo(context.fPlayer, true));
            return;
        }

        if (!(faction.getOpen() || faction.isInvited(fplayer) || context.fPlayer.isAdminBypassing() || Permission.JOIN_ANY.has(context.sender, false))) {
            context.msg(TL.COMMAND_JOIN_REQUIRESINVITATION);
            if (samePlayer) {
                faction.msg(TL.COMMAND_JOIN_ATTEMPTEDJOIN, fplayer.describeTo(faction, true));
            }
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make sure they can pay
        if (samePlayer && !context.canAffordCommand(P.p.conf().economy().getCostJoin(), TL.COMMAND_JOIN_TOJOIN.toString())) {
            return;
        }

        // Check for ban
        if (!context.fPlayer.isAdminBypassing() && faction.isBanned(context.fPlayer)) {
            context.fPlayer.msg(TL.COMMAND_JOIN_BANNED, faction.getTag(context.fPlayer));
            return;
        }

        // trigger the join event (cancellable)
        FPlayerJoinEvent joinEvent = new FPlayerJoinEvent(FPlayers.getInstance().getByPlayer(context.player), faction, FPlayerJoinEvent.PlayerJoinReason.COMMAND);
        Bukkit.getServer().getPluginManager().callEvent(joinEvent);
        if (joinEvent.isCancelled()) {
            return;
        }

        // then make 'em pay (if applicable)
        if (samePlayer && !context.payForCommand(P.p.conf().economy().getCostJoin(), TL.COMMAND_JOIN_TOJOIN.toString(), TL.COMMAND_JOIN_FORJOIN.toString())) {
            return;
        }

        context.fPlayer.msg(TL.COMMAND_JOIN_SUCCESS, fplayer.describeTo(context.fPlayer, true), faction.getTag(context.fPlayer));

        if (!samePlayer) {
            fplayer.msg(TL.COMMAND_JOIN_MOVED, context.fPlayer.describeTo(fplayer, true), faction.getTag(fplayer));
        }
        faction.msg(TL.COMMAND_JOIN_JOINED, fplayer.describeTo(faction, true));

        fplayer.resetFactionData();
        fplayer.setFaction(faction);
        faction.deinvite(fplayer);
        fplayer.setRole(faction.getDefaultRole());

        if (P.p.conf().logging().isFactionJoin()) {
            if (samePlayer) {
                P.p.log(TL.COMMAND_JOIN_JOINEDLOG.toString(), fplayer.getName(), faction.getTag());
            } else {
                P.p.log(TL.COMMAND_JOIN_MOVEDLOG.toString(), context.fPlayer.getName(), fplayer.getName(), faction.getTag());
            }
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_JOIN_DESCRIPTION;
    }
}
