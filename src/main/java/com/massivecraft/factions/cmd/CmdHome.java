package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.SmokeUtil;
import com.massivecraft.factions.zcore.util.TL;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class CmdHome extends FCommand {

    public CmdHome() {
        super();
        this.aliases.add("home");

        //this.requiredArgs.add("");
        //this.optionalArgs.put("", "");

        this.permission = Permission.HOME.node;
        this.disableOnLock = false;

        senderMustBePlayer = true;
        senderMustBeMember = true;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        // TODO: Hide this command on help also.
        if (!Conf.homesEnabled) {
            fme.TLmsg(TL.CMD_HOME_DISABLED, values);
            return;
        }

        if (!Conf.homesTeleportCommandEnabled) {
            fme.TLmsg(TL.CMD_HOME_TELEPORT_DISABLED, values);
            return;
        }

        if (!myFaction.hasHome()) {
            values.put("homenotset", TL.CMD_HOME_NOT_SET.toString());
            values.put("askleader-youshould", fme.getRole().value < Role.MODERATOR.value ? TL.CMD_HOME_ASK_LEADER.toString() : TL.CMD_HOME_YOU_SHOULD.toString());
            fme.TLmsg(TL.CMD_HOME_FMT, values);
            fme.sendMessage(p.cmdBase.cmdSethome.getUseageTemplate());
            return;
        }

        if (!Conf.homesTeleportAllowedFromEnemyTerritory && fme.isInEnemyTerritory()) {
            fme.TLmsg(TL.CMD_HOME_ENEMY_TERRITORY, values);
            return;
        }

        if (!Conf.homesTeleportAllowedFromDifferentWorld && me.getWorld().getUID() != myFaction.getHome().getWorld().getUID()) {
            fme.TLmsg(TL.CMD_HOME_WRONG_WORLD, values);
            return;
        }

        Faction faction = Board.getFactionAt(new FLocation(me.getLocation()));
        Location loc = me.getLocation().clone();

        // if player is not in a safe zone or their own faction territory, only allow teleport if no enemies are nearby
        if (Conf.homesTeleportAllowedEnemyDistance > 0 &&
                    !faction.isSafeZone() &&
                    (!fme.isInOwnTerritory() || (fme.isInOwnTerritory() && !Conf.homesTeleportIgnoreEnemiesIfInOwnTerritory))) {
            World w = loc.getWorld();
            double x = loc.getX();
            double y = loc.getY();
            double z = loc.getZ();

            for (Player p : me.getServer().getOnlinePlayers()) {
                if (p == null || !p.isOnline() || p.isDead() || p == me || p.getWorld() != w) {
                    continue;
                }

                FPlayer fp = FPlayers.i.get(p);
                if (fme.getRelationTo(fp) != Relation.ENEMY) {
                    continue;
                }

                Location l = p.getLocation();
                double dx = Math.abs(x - l.getX());
                double dy = Math.abs(y - l.getY());
                double dz = Math.abs(z - l.getZ());
                double max = Conf.homesTeleportAllowedEnemyDistance;

                // box-shaped distance check
                if (dx > max || dy > max || dz > max) {
                    continue;
                }

                values.put("proximity", String.valueOf(Conf.homesTeleportAllowedEnemyDistance));
                fme.TLmsg(TL.CMD_HOME_ENEMY_PROXIM, values);
                return;
            }
        }

        // if Essentials teleport handling is enabled and available, pass the teleport off to it (for delay and cooldown)
        if (Essentials.handleTeleport(me, myFaction.getHome())) {
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!payForCommand(Conf.econCostHome, "to teleport to your faction home", "for teleporting to your faction home")) {
            return;
        }

        // Create a smoke effect
        if (Conf.homesTeleportCommandSmokeEffectEnabled) {
            List<Location> smokeLocations = new ArrayList<Location>();
            smokeLocations.add(loc);
            smokeLocations.add(loc.add(0, 1, 0));
            smokeLocations.add(myFaction.getHome());
            smokeLocations.add(myFaction.getHome().clone().add(0, 1, 0));
            SmokeUtil.spawnCloudRandom(smokeLocations, Conf.homesTeleportCommandSmokeEffectThickness);
        }

        me.teleport(myFaction.getHome());
    }

}
