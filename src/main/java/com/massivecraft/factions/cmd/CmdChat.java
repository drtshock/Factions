package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdChat extends FCommand {

    public CmdChat() {
        super();
        this.aliases.add("c");
        this.aliases.add("chat");

        //this.requiredArgs.add("");
        this.optionalArgs.put("mode", "next");

        this.permission = Permission.CHAT.node;
        this.disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        if (!Conf.factionOnlyChat) {
            TLmsg(TL.CMD_CHAT_DISABLED, values);
            return;
        }

        String modeString = this.argAsString(0);
        ChatMode modeTarget = fme.getChatMode().getNext();

        if (modeString != null) {
            modeString.toLowerCase();
            if (modeString.startsWith("p")) {
                modeTarget = ChatMode.PUBLIC;
            } else if (modeString.startsWith("a")) {
                modeTarget = ChatMode.ALLIANCE;
            } else if (modeString.startsWith("f")) {
                modeTarget = ChatMode.FACTION;
            } else {
                TLmsg(TL.CMD_CHAT_UNRECOGNIZED, values);
                return;
            }
        }

        fme.setChatMode(modeTarget);

        if (fme.getChatMode() == ChatMode.PUBLIC) {
            TLmsg(TL.CMD_CHAT_PUBLIC, values);
        } else if (fme.getChatMode() == ChatMode.ALLIANCE) {
            TLmsg(TL.CMD_CHAT_ALLIANCE, values);
        } else {
            TLmsg(TL.CMD_CHAT_FACTION, values);
        }
    }
}
