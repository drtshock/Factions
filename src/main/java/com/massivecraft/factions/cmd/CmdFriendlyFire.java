package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

/**
 * Created by Julio on 4/5/2016.
 */
public class CmdFriendlyFire extends FCommand {

    public CmdFriendlyFire(){
        aliases.add("friendlyfire");
        aliases.add("ff");
        setHelpShort("toggle friendly fire");

        this.permission = Permission.FRIENDLYFIRE.node;

        senderMustBeModerator = true;
        senderMustBePlayer = true;
    }

    @Override
    public void perform() {

        boolean friendlyFire = myFaction.getFriendlyFire();

        myFaction.msg(TL.COMMAND_FRIENDLYFIRE_TOGGLED, fme.describeTo(myFaction, true), (!friendlyFire ? "en" : "dis") + "abled");
        myFaction.setFriendlyFire(!friendlyFire);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FRIENDLYFIRE;
    }
}
