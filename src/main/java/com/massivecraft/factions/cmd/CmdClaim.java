package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.SpiralTask;
import com.massivecraft.factions.zcore.util.TL;


public class CmdClaim extends FCommand {

    public CmdClaim() {
        super();
        this.aliases.add("claim");

        //this.requiredArgs.add("");
        this.optionalArgs.put("faction", "your");
        this.optionalArgs.put("radius", "1");

        this.permission = Permission.CLAIM.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        // Read and validate input
        final Faction forFaction = this.argAsFaction(0, myFaction);
        int radius = this.argAsInt(1, 1);

        if (radius < 1) {
            msg(TL.CMD_CLAIM_RADIUS_TOO_SMALL.toString());
            return;
        }

        if (radius < 2) {
            // single chunk
            fme.attemptClaim(forFaction, me.getLocation(), true);
        } else {
            // radius claim
            if (!Permission.CLAIM_RADIUS.has(sender, false)) {
                msg(TL.CMD_CLAIM_RADIUS_PERMISSION.toString());
                return;
            }

            new SpiralTask(new FLocation(me), radius) {
                private int failCount = 0;
                private final int limit = Conf.radiusClaimFailureLimit - 1;

                @Override
                public boolean work() {
                    boolean success = fme.attemptClaim(forFaction, this.currentLocation(), true);
                    if (success) {
                        failCount = 0;
                    } else if (!success && failCount++ >= limit) {
                        this.stop();
                        return false;
                    }

                    return true;
                }
            };
        }
    }

}
