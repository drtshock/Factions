package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdBoom extends FCommand {

    public CmdBoom() {
        super();
        this.aliases.add("noboom");

        //this.requiredArgs.add("");
        this.optionalArgs.put("on/off", "flip");

        this.permission = Permission.NO_BOOM.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = true;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        if (!myFaction.isPeaceful()) {
            fme.TLmsg(TL.CMD_BOOM_NOT_PEACEFUL, values);
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!payForCommand(Conf.econCostNoBoom, "to toggle explosions", "for toggling explosions")) {
            return;
        }

        myFaction.setPeacefulExplosionsEnabled(this.argAsBool(0, !myFaction.getPeacefulExplosionsEnabled()));

        // Inform
        values.put("player", fme.describeTo(myFaction));
        values.put("enabled", myFaction.noExplosionsInTerritory() ? TL.DISABLED.toString() : TL.ENABLED.toString());
        myFaction.TLmsg(TL.CMD_BOOM_CONFIRM, values);
    }
}
