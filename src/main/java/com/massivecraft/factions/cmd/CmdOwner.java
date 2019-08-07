package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;


public class CmdOwner extends FCommand {

    public CmdOwner() {
        super();
        this.aliases.add("owner");

        this.optionalArgs.put("player", "you");

        this.requirements = new CommandRequirements.Builder(Permission.OWNER)
                .playerOnly()
                .build();
    }

    // TODO: Fix colors!

    @Override
    public void perform(CommandContext context) {
        boolean hasBypass = context.fPlayer.isAdminBypassing();

        if (!hasBypass && !context.assertHasFaction()) {
            return;
        }

        if (!P.p.conf().factions().ownedArea().isEnabled()) {
            context.msg(TL.COMMAND_OWNER_DISABLED);
            return;
        }

        if (!hasBypass && P.p.conf().factions().ownedArea().getLimitPerFaction() > 0 && context.faction.getCountOfClaimsWithOwners() >= P.p.conf().factions().ownedArea().getLimitPerFaction()) {
            context.msg(TL.COMMAND_OWNER_LIMIT, P.p.conf().factions().ownedArea().getLimitPerFaction());
            return;
        }

        if (!hasBypass && !context.assertMinRole(P.p.conf().factions().ownedArea().isModeratorsCanSet() ? Role.MODERATOR : Role.ADMIN)) {
            return;
        }

        FLocation flocation = new FLocation(context.fPlayer);

        Faction factionHere = Board.getInstance().getFactionAt(flocation);
        if (factionHere != context.faction) {
            if (!factionHere.isNormal()) {
                context.msg(TL.COMMAND_OWNER_NOTCLAIMED);
                return;
            }

            if (!hasBypass) {
                context.msg(TL.COMMAND_OWNER_WRONGFACTION);
                return;
            }

        }

        FPlayer target = context.argAsBestFPlayerMatch(0, context.fPlayer);
        if (target == null) {
            return;
        }

        String playerName = target.getName();

        if (target.getFaction() != context.faction) {
            context.msg(TL.COMMAND_OWNER_NOTMEMBER, playerName);
            return;
        }

        // if no player name was passed, and this claim does already have owners set, clear them
        if (context.args.isEmpty() && context.faction.doesLocationHaveOwnersSet(flocation)) {
            context.faction.clearClaimOwnership(flocation);
            context.msg(TL.COMMAND_OWNER_CLEARED);
            return;
        }

        if (context.faction.isPlayerInOwnerList(target, flocation)) {
            context.faction.removePlayerAsOwner(target, flocation);
            context.msg(TL.COMMAND_OWNER_REMOVED, playerName);
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!context.payForCommand(P.p.conf().economy().getCostOwner(), TL.COMMAND_OWNER_TOSET, TL.COMMAND_OWNER_FORSET)) {
            return;
        }

        context.faction.setPlayerAsOwner(target, flocation);

        context.msg(TL.COMMAND_OWNER_ADDED, playerName);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_OWNER_DESCRIPTION;
    }
}
