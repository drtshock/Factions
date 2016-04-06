package com.massivecraft.factions.cmd;

import com.massivecraft.factions.zcore.util.TL;

/**
 * Created by Julio on 4/5/2016.
 */
public class CmdFriendlyFire extends FCommand {

    public CmdFriendlyFire(){
        aliases.add("friendlyfire");
        aliases.add("ff");

        senderMustBeModerator = true;
        senderMustBePlayer = true;
    }

    @Override
    public void perform() {

        boolean friendlyFire = myFaction.getFriendlyFire();
        myFaction.setFriendlyFire(!friendlyFire);
        myFaction.msg(TL.COMMAND_FRIENDLY_FIRE_TOGGLED, fme.describeTo(myFaction, true), (myFaction.getFriendlyFire() ? "en":"dis") + "abled");
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FRIENDLY_FIRE;
    }
}
