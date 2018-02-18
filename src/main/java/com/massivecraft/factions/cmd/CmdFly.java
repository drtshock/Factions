package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.WarmUpUtil;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.SmokeUtil;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class CmdFly extends FCommand {

    public CmdFly() {
        super();
        this.aliases.add("fly");

        this.optionalArgs.put("on/off", "flip");

        this.permission = Permission.FLIGHT.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        if (!P.p.getConfig().getBoolean("enable-faction-flight", false)) {
            fme.msg(TL.COMMAND_FLY_DISABLED);
            return;
        }

        if (args.size() == 0) {
            if (!fme.canFlyAtLocation() && !fme.isFFlying()) {
                Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                fme.msg(TL.COMMAND_CANNOT_FLY_HERE, factionAtLocation.getTag(fme));
                return;
            }

            enableFlight(!fme.isFFlying());
        } else if (args.size() == 1) {
            if (!fme.canFlyAtLocation() && argAsBool(0)) {
                Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                fme.msg(TL.COMMAND_CANNOT_FLY_HERE, factionAtLocation.getTag(fme));
                return;
            }

            enableFlight(argAsBool(0));
        }
    }

    private void enableFlight(final boolean toggle) {
        this.doWarmUp(WarmUpUtil.Warmup.FLIGHT, TL.COMMAND_FLIGHT_WARMUP, "Fly", new Runnable() {
            @Override
            public void run() {
                fme.setFFlying(toggle);
            }
        }, this.p.getConfig().getLong("warmups.f-fly", 0));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY_DESCRIPTION;
    }

}