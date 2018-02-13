package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

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
        if (!P.p.getConfig().getBoolean("enable-faction-flight", false) || !P.p.getServer().getAllowFlight()) {
            fme.msg(TL.COMMAND_FLY_DISABLED);
            return;
        }

        // Called if auto flight is enabled
        if (args.size() == 0 && P.p.getConfig().getBoolean("use-auto-faction-flight", false)) {
            fme.setAutoFFlying(!fme.isAutoFFlying());

            if (fme.canFlyAtLocation()) {
                fme.setFFlying(fme.isAutoFFlying());
            } else {
                fme.setFFlying(false);
            }

            return;
        } else if (args.size() == 1 && P.p.getConfig().getBoolean("use-auto-faction-flight", false)) {
            fme.setAutoFFlying(argAsBool(0));

            if (fme.canFlyAtLocation()) {
                fme.setFFlying(fme.isAutoFFlying());
            } else {
                fme.setFFlying(false);
            }

            return;
        }

        // Called if auto flight is disabled
        if (args.size() == 0) {
            if (fme.canFlyAtLocation() && !fme.isFFlying()) {
                Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                fme.msg(TL.COMMAND_CANNOT_FLY_HERE, factionAtLocation.getTag(fme));
                return;
            }

            fme.setFFlying(!fme.isFFlying());
        } else if (args.size() == 1) {
            if (fme.canFlyAtLocation() && argAsBool(0)) {
                Faction factionAtLocation = Board.getInstance().getFactionAt(fme.getLastStoodAt());
                fme.msg(TL.COMMAND_CANNOT_FLY_HERE, factionAtLocation.getTag(fme));
                return;
            }

            fme.setFFlying(argAsBool(0));
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY_DESCRIPTION;
    }

}
