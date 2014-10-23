package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdDeinvite extends FCommand {

    public CmdDeinvite() {
        super();
        this.aliases.add("deinvite");
        this.aliases.add("deinv");

        this.requiredArgs.add("player name");
        //this.optionalArgs.put("", "");

        this.permission = Permission.DEINVITE.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = true;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        FPlayer you = this.argAsBestFPlayerMatch(0);
        if (you == null) {
            return;
        }

        if (you.getFaction() == myFaction) {
            values.put("target", you.getName());
            values.put("faction", myFaction.getTag());
            values.put("kickdesc", p.cmdBase.cmdKick.getUseageTemplate(false));
            TLmsg(TL.CMD_INVITE_ALREADY_MEMBER, values);
            TLmsg(TL.CMD_INVITE_KICK, values);
            return;
        }

        myFaction.deinvite(you);

        values.put("player", fme.describeTo(you));
        values.put("faction", myFaction.describeTo(you));
        you.TLmsg(TL.CMD_DEINVITE_REVOKED_PLAYER, values);

        values.put("player", fme.describeTo(myFaction));
        values.put("target", you.describeTo(myFaction));
        myFaction.TLmsg(TL.CMD_DEINVITE_REVOKED_FACTION, values);
    }

}
