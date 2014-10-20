package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Permission;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class CmdAnnounce extends FCommand {

    public CmdAnnounce() {
        super();
        this.aliases.add("ann");
        this.aliases.add("announce");

        this.requiredArgs.add("message");
        this.errorOnToManyArgs = false;

        this.permission = Permission.ANNOUNCE.node;
        this.disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = true;
    }

    @Override
    public void perform() {
        String prefix = p.txt.parse("<green>%s<yellow> [<gray>%s<yellow>]<reset>", myFaction.getTag(), me.getName());
        String message = StringUtils.join(args, " ");

        for (Player player : myFaction.getOnlinePlayers()) {
            player.sendMessage(prefix + message);
        }

        // Add for offline players.
        for (FPlayer fp : myFaction.getFPlayersWhereOnline(false)) {
                myFaction.addAnnouncement(fp, prefix + message);
        }
    }

}
