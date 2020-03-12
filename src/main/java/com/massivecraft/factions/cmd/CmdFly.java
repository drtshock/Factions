package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.FlightUtil;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.WarmUpUtil;

public class CmdFly extends FCommand {

    public CmdFly() {
        super();
        this.aliases.add("fly");

        this.optionalArgs.put("on/off/auto", "flip");

        this.requirements = new CommandRequirements.Builder(Permission.FLY)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (context.args.size() == 0) {
            toggleFlight(context, !context.fPlayer.isFlying(), true);
        } else if (context.args.size() == 1) {
            if (context.argAsString(0).equalsIgnoreCase("auto")) {
                // Player Wants to AutoFly
                if (Permission.FLY_AUTO.has(context.player, true)) {
                    context.fPlayer.setAutoFlying(!context.fPlayer.isAutoFlying());
                    toggleFlight(context, context.fPlayer.isAutoFlying(), false);
                }
            } else {
                toggleFlight(context, context.argAsBool(0), true);
            }
        }
    }

    private void toggleFlight(final CommandContext context, final boolean toggle, boolean notify) {
        // If false do nothing besides set
        if (!toggle) {
            context.fPlayer.setFlying(false);
            return;
        }
        // Do checks if true
        if (!flyTest(context, notify)) {
            return;
        }

        context.doWarmUp(WarmUpUtil.Warmup.FLIGHT, TL.WARMUPS_NOTIFY_FLIGHT, "Fly", () -> {
            if (flyTest(context, notify)) {
                context.fPlayer.setFlying(true);
            }
        }, this.plugin.conf().commands().fly().getDelay());
    }

    private boolean flyTest(final CommandContext context, boolean notify) {
        if (!context.fPlayer.canFlyAtLocation()) {
            if (notify) {
                Faction factionAtLocation = Board.getInstance().getFactionAt(context.fPlayer.getLastStoodAt());
                context.msg(TL.COMMAND_FLY_NO_ACCESS, factionAtLocation.getTag(context.fPlayer));
            }
            return false;
        } else if (FlightUtil.instance().enemiesNearby(context.fPlayer, FactionsPlugin.getInstance().conf().commands().fly().getEnemyRadius())) {
            if (notify) {
                context.msg(TL.COMMAND_FLY_ENEMY_NEARBY);
            }
            return false;
        }
        return true;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY_DESCRIPTION;
    }

}
