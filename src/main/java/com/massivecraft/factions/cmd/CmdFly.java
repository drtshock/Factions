package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.util.TL;

public class CmdFly extends FCommand {

    public CmdFly() {
        super();
        this.aliases.add("fly");

        this.optionalArgs.put("on/off", "flip");

        this.permission = Permission.FLY.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        // Disabled by default.
        if (!P.p.getConfig().getBoolean("faction-flight.enable", false)) {
            fme.msg(TL.COMMAND_FLY_DISABLED);
            return;
        }

        // Use normal faction flight is AutoFly is not used
        if (!P.p.getConfig().getBoolean("faction-flight.auto-flight", false)) {
            if (args.size() == 0) {
                if (!fme.canFlyAtLocation() && !fme.isFlying()) {
                    Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                    fme.msg(TL.COMMAND_FLY_NO_ACCESS, factionAtLocation.getTag(fme));
                    return;
                }

                toggleFlight(!fme.isFlying());
                return;
            } else if (args.size() == 1) {
                if (!fme.canFlyAtLocation() && argAsBool(0)) {
                    Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                    fme.msg(TL.COMMAND_FLY_NO_ACCESS, factionAtLocation.getTag(fme));
                    return;
                }

                toggleFlight(argAsBool(0));
                return;
            }
        } else {
            // AutoFly is enabled lets do this!
            if (args.size() == 0) {
                fme.setAutoFlying(!fme.isAutoFlying());
                fme.msg(TL.COMMAND_FLY_AUTO_CHANGE, fme.isAutoFlying() ? "enabled" : "disabled");

                if (fme.canFlyAtLocation() && fme.isAutoFlying()) {
                    toggleFlight(fme.isAutoFlying());
                }
                return;
            } else if (args.size() == 1) {
                boolean toggle = argAsBool(0);
                fme.setAutoFlying(toggle);
                fme.msg(TL.COMMAND_FLY_AUTO_CHANGE, toggle ? "Enabled" : "Disabled");

                if (toggle && fme.canFlyAtLocation()) {
                    toggleFlight(true);
                }
                return;
            }
        }

        fme.msg(getUsageTranslation());
    }

    private void toggleFlight(final boolean toggle) {
        if (!toggle) {
            fme.setFlying(false);
            return;
        }
        
        this.doWarmUp(WarmUpUtil.Warmup.FLIGHT, TL.WARMUPS_NOTIFY_FLIGHT, "Fly", new Runnable() {
            @Override
            public void run() {
                fme.setFlying(true);
            }
        }, this.p.getConfig().getLong("warmups.f-fly", 0));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY_DESCRIPTION;
    }

}
