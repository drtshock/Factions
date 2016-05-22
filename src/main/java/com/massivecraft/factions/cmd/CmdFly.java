package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Julio on 5/22/2016.
 */
public class CmdFly extends FCommand {

    public CmdFly() {
        aliases.add("fly");
        setHelpShort("toggle fly mode");

        this.permission = Permission.FLY.node;

        senderMustBePlayer = true;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY;
    }

    @Override
    public void perform() {

        int waitTime = Conf.warmupTimeForFly;
        FLocation fLocation = fme.getLastStoodAt();
        boolean flying = fme.isFlying();

        if (!Board.getInstance().getFactionAt(fLocation).equals(myFaction)) {
            msg(TL.COMMAND_FLY_ONLYINOWNLAND);
            return;
        }

        if (!flying) {
            fme.setCanFly(true);
            msg(TL.COMMAND_FLY_WARMUP, waitTime);

            new BukkitRunnable() {
                @Override
                public void run() {

                    if (!fme.canFly()) {
                        cancel();
                        return;
                    }

                    fme.setFlying(true);
                    fme.setCanFly(false);
                    msg(TL.COMMAND_FLY_TOGGLED, "enabled");

                    me.setAllowFlight(true);
                    me.setFlying(true);
                }
            }.runTaskLater(P.p, waitTime * 20);
        } else {
            fme.setFlying(false);
            me.setFlying(false);
            me.setAllowFlight(false);
            msg(TL.COMMAND_FLY_TOGGLED, "disabled");
        }


    }

}
