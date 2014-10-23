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
            TLmsg(TL.CMD_AUTOCLAIM_DISABLED, values);
            return;
        }
        values.put("forfaction", forFaction.describeTo(fme));

        if (!fme.canClaimForFaction(forFaction)) {
            if (myFaction == forFaction) {
                values.put("moderator", Role.MODERATOR.toString());
                TLmsg(TL.CMD_AUTOCLAIM_RANK, values);
            } else {
                TLmsg(TL.CMD_AUTOCLAIM_WRONG_FACTION, values);
            }

            return;
        }

        fme.setAutoClaimFor(forFaction);

        TLmsg(TL.CMD_AUTOCLAIM_START, values);
        fme.attemptClaim(forFaction, me.getLocation(), true);
    }

}