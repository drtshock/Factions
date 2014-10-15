package com.massivecraft.factions.cmd;

import java.util.ArrayList;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.FPlayer;

public class CmdStatus extends FCommand {

    public CmdStatus() {
        super();
        this.aliases.add("status");
        this.aliases.add("s");

        this.permission = Permission.STATUS.node;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        ArrayList<String> ret = new ArrayList<String>();
        for (FPlayer fp : myFaction.getFPlayers()) {
            String last;
            ChatColor online;
            if (fp.isOnline()) {
                last = "now";
                online = ChatColor.GREEN;
            } else {
                String humanized = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - fp.getLastLoginTime(), true, true);
                last = humanized + " ago";
                online = (System.currentTimeMillis() - fp.getLastLoginTime()) < 432000000 ? ChatColor.YELLOW : ChatColor.RED;
            }
            ret.add(String.format("%s Power: %s Last Seen: %s",
                    online + fp.getRole().getPrefix() + fp.getName() + ChatColor.RESET,
                    ChatColor.YELLOW + String.valueOf(fp.getPowerRounded()) +
                        " / " + String.valueOf(fp.getPowerMaxRounded()) + ChatColor.RESET,
                    online + last
                    ).trim());
        }
        fme.sendMessage(ret);
    }

}
