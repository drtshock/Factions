package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.tabcomplete.TabCompleteProvider;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CmdUnban extends FCommand {

    public CmdUnban() {
        super();
        this.aliases.add("unban");

        this.requiredArgs.add("target");

        this.requirements = new CommandRequirements.Builder(Permission.BAN)
                .memberOnly()
                .withAction(PermissableAction.BAN)
                .withMinRole(Role.MODERATOR)
                .build();

        this.disableOnLock = true;
    }

    @Override
    public void perform(CommandContext context) {
        FPlayer target = context.argAsFPlayer(0);
        if (target == null) {
            return; // the above method sends a message if fails to find someone.
        }

        if (!context.faction.isBanned(target)) {
            context.msg(TL.COMMAND_UNBAN_NOTBANNED, target.getName());
            return;
        }

        context.faction.unban(target);

        context.faction.msg(TL.COMMAND_UNBAN_UNBANNED,context.fPlayer.getName(), target.getName());
        target.msg(TL.COMMAND_UNBAN_TARGET, context.faction.getTag(target));
    }

    @Override
    public TabCompleteProvider onTabComplete(final CommandContext context, String[] args) {
        if (args.length == 1) {
            return new TabCompleteProvider() {
                @Override
                public List<String> get() {
                    List<String> banned = new ArrayList<>();
                    for (BanInfo info : context.faction.getBannedPlayers()) {
                        banned.add(Bukkit.getOfflinePlayer(UUID.fromString(info.getBanned())).getName());
                    }
                    return banned;
                }
            };
        }
        return super.onTabComplete(context, args);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_UNBAN_DESCRIPTION;
    }
}
