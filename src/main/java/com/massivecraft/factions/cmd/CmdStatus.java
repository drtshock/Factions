package com.massivecraft.factions.cmd;

import java.util.ArrayList;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.FPlayer;

public class CmdStatus extends FCommand {
    
    public CmdStatus() {
        super();
        this.aliases.add("status");
        this.aliases.add("s");

        //this.requiredArgs.add("");
        this.optionalArgs.put("page", "1");

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
            String rank;
            if (fp.isOnline()) {
                last = "now";
                online = ChatColor.GREEN;
            } else {
                String humanized = DurationFormatUtils.formatDurationWords(System.currentTimeMillis() - fp.getLastLoginTime(), true, true);
                if ((System.currentTimeMillis() - fp.getLastLoginTime()) < 432000000) { // if time offline less than five days
                    last = humanized + " ago";
                    online = ChatColor.YELLOW;
                } else {
                    last = humanized + " ago";
                    online = ChatColor.RED;
                }
            }
            if (fp.getRole() == Role.ADMIN) {
                rank = "**";
            } else if (fp.getRole() == Role.MODERATOR) {
                rank = " *";
            } else {
                rank = "   ";
            }
            ret.add(String.format("%s Power: %s Last Seen: %s",
                    online + rank + fp.getName() + ChatColor.RESET,
                    ChatColor.YELLOW + String.valueOf(fp.getPowerRounded()) + 
                        " / " + String.valueOf(fp.getPowerMaxRounded()) + ChatColor.RESET,
                    online + last
                    ).trim());
        }
        fme.sendMessage(ret);
    }

}
