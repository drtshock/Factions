package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdChatSpy extends FCommand {

    public CmdChatSpy() {
        super();
        this.aliases.add("chatspy");

        this.optionalArgs.put("on/off", "flip");

        this.permission = Permission.CHATSPY.node;
        this.disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        fme.setSpyingChat(this.argAsBool(0, !fme.isSpyingChat()));

        if (fme.isSpyingChat()) {
            fme.msg(TL.CMD_CHATSPY_ENABLED.toString());
            P.p.log(fme.getName() + " has ENABLED chat spying mode.");
        } else {
            fme.msg(TL.CMD_CHATSPY_DISABLED.toString());
            P.p.log(fme.getName() + " DISABLED chat spying mode.");
        }
    }
}