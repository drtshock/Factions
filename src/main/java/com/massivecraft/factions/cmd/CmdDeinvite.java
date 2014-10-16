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
            msg(TL.CMD_INVITE_ALREADY_MEMBER.toString(), you.getName(), myFaction.getTag());
            msg(TL.CMD_INVITE_KICK.toString(), p.cmdBase.cmdKick.getUseageTemplate(false));
            return;
        }

        myFaction.deinvite(you);

        you.msg(TL.CMD_DEINVITE_REVOKED_PLAYER.toString(), fme.describeTo(you), myFaction.describeTo(you));

        myFaction.msg(TL.CMD_DEINVITE_REVOKED_FACTION.toString(), fme.describeTo(myFaction), you.describeTo(myFaction));
    }

}
