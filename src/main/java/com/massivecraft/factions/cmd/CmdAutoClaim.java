package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;

public class CmdAutoClaim extends FCommand {

    public CmdAutoClaim() {
        super();
        this.aliases.add("autoclaim");

        //this.requiredArgs.add("");
        this.optionalArgs.put("faction", "your");

        this.permission = Permission.AUTOCLAIM.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        Faction forFaction = this.argAsFaction(0, myFaction);
        if (forFaction == null || forFaction == fme.getAutoClaimFor()) {
            fme.setAutoClaimFor(null);
            msg(TL.COMMAND_AUTOCLAIM_DISABLED.toString());
            return;
        }

        if (!fme.canClaimForFaction(forFaction)) {
            if (myFaction == forFaction) {
                msg(TL.COMMAND_AUTOCLAIM_REQUIREDRANK.toString(), Role.MODERATOR.toString());
            } else {
                msg(TL.COMMAND_AUTOCLAIM_OTHERFACTION.toString(), forFaction.describeTo(fme));
            }

            return;
        }

        fme.setAutoClaimFor(forFaction);

        msg(TL.COMMAND_AUTOCLAIM_ENABLED.toString(), forFaction.describeTo(fme));
        fme.attemptClaim(forFaction, me.getLocation(), true);
    }

}