package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.gui.WarpGUI;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WarmUpUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdWarpOther extends FCommand {

    public CmdWarpOther() {
        super();
        this.aliases.add("warpother");
        this.requiredArgs.add("faction");
        this.optionalArgs.put("warp", "warp");
        this.optionalArgs.put("password", "password");

        this.requirements = new CommandRequirements.Builder(Permission.WARP)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        // TODO: check if in combat.
        if (context.args.size() < 1) {
            context.msg(TL.COMMAND_WARPOTHER_COMMANDFORMAT);
            return;
        }
        Faction faction = context.argAsFaction(0);
        if (faction == null) {
            context.msg(TL.GENERIC_NOFACTIONMATCH, context.argAsString(0));
            return;
        }

        if (!faction.hasAccess(context.fPlayer, PermissibleAction.WARP)) {
            context.msg(TL.COMMAND_FWARP_NOACCESS, faction.getTag(context.fPlayer));
            return;
        }

        if (context.args.size() == 1) {
            WarpGUI ui = new WarpGUI(context.fPlayer, faction);
            ui.open();
        } else {
            final String warpName = context.argAsString(1);
            final String passwordAttempt = context.argAsString(2);

            if (faction.isWarp(warpName)) {
                // Check if requires password and if so, check if valid. CASE SENSITIVE
                if (faction.hasWarpPassword(warpName) && !faction.isWarpPassword(warpName, passwordAttempt)) {
                    context.fPlayer.msg(TL.COMMAND_FWARP_INVALID_PASSWORD);
                    return;
                }

                // Check transaction AFTER password check.
                if (!transact(context.fPlayer, context)) {
                    return;
                }
                final FPlayer fPlayer = context.fPlayer;
                final UUID uuid = context.fPlayer.getPlayer().getUniqueId();
                context.doWarmUp(WarmUpUtil.Warmup.WARP, TL.WARMUPS_NOTIFY_TELEPORT, warpName, () -> {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        player.teleport(faction.getWarp(warpName).getLocation());
                        fPlayer.msg(TL.COMMAND_FWARP_WARPED, warpName);
                    }
                }, this.plugin.getConfig().getLong("warmups.f-warp", 0));
            } else {
                context.fPlayer.msg(TL.COMMAND_FWARP_INVALID_WARP, warpName);
            }
        }
    }

    private boolean transact(FPlayer player, CommandContext context) {
        return !FactionsPlugin.getInstance().getConfig().getBoolean("warp-cost.enabled", false) || player.isAdminBypassing() || context.payForCommand(FactionsPlugin.getInstance().getConfig().getDouble("warp-cost.warp", 5), TL.COMMAND_FWARP_TOWARP.toString(), TL.COMMAND_FWARP_FORWARPING.toString());
    }


    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FWARP_DESCRIPTION;
    }
}
