package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.tabcomplete.TabCompleteProvider;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdUnban extends FCommand {

    public CmdUnban() {
        super();
        this.aliases.add("unban");

        this.requiredArgs.add("target");

        this.permission = Permission.BAN.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        Access access = myFaction.getAccess(fme, PermissableAction.BAN);
        if (access == Access.DENY) {
            fme.msg(TL.GENERIC_NOPERMISSION, "ban");
            return;
        }

        // Can the player set the home for this faction?
        // Check for ALLOW access as well before we check for role.
        // TODO: no more duplicate code :(
        if (access != Access.ALLOW) {
            if (!Permission.BAN.has(sender) && !(assertMinRole(Role.MODERATOR))) {
                return;
            }
        } else {
            if (!Permission.BAN.has(sender, true)) {
                return;
            }
        }

        // Good on permission checks. Now lets just ban the player.
        FPlayer target = argAsFPlayer(0);
        if (target == null) {
            return; // the above method sends a message if fails to find someone.
        }

        if (!myFaction.isBanned(target)) {
            fme.msg(TL.COMMAND_UNBAN_NOTBANNED, target.getName());
            return;
        }

        myFaction.unban(target);

        myFaction.msg(TL.COMMAND_UNBAN_UNBANNED, fme.getName(), target.getName());
        target.msg(TL.COMMAND_UNBAN_TARGET, myFaction.getTag(target));
    }

    @Override
    public TabCompleteProvider onTabComplete(final Player player, String[] args) {
        if (args.length == 0) {
            return new TabCompleteProvider() {
                @Override
                public List<String> get() {
                    Faction faction = FPlayers.getInstance().getByPlayer(player).getFaction();
                    List<String> banned = new ArrayList<>();
                    for (BanInfo info : faction.getBannedPlayers()) {
                        banned.add(info.getBanned());
                    }
                    return banned;
                }
            };
        }
        return super.onTabComplete(player, args);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_UNBAN_DESCRIPTION;
    }
}
