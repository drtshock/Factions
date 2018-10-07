package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FlightUtil extends BukkitRunnable {
    private int amount;
    private float speed;

    public FlightUtil() {
        this.amount = P.p.getConfig().getInt("f-fly.trails.amount", 20);
        this.speed = (float) P.p.getConfig().getDouble("f-fly.trails.speed", 0.05);
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            FPlayer pilot = FPlayers.getInstance().getByPlayer(player);
            if (pilot.isFlying()) {
                if (pilot.getFlyTrailsEffect() != null && Permission.FLY_TRAILS.has(player) && pilot.getFlyTrailsState()) {
                    pilot.getFlyTrailsEffect().display(0, 0, 0, speed, amount, player.getLocation(), new ArrayList<>(Bukkit.getOnlinePlayers()));
                }

                if (!pilot.isAdminBypassing()) {
                    if (enemiesNearby(pilot, 5)) {
                        pilot.msg(TL.COMMAND_FLY_ENEMY_DISABLE);
                        pilot.setFlying(false);
                        if (pilot.isAutoFlying()) {
                            pilot.setAutoFlying(false);
                        }
                    }
                }
            }
        }
    }

    public static boolean enemiesNearby(FPlayer target, int radius) {
        List<Entity> nearbyEntities = target.getPlayer().getNearbyEntities(radius, radius, radius);
        for (Entity entity : nearbyEntities) {
            if (entity instanceof Player) {
                FPlayer playerNearby = FPlayers.getInstance().getByPlayer((Player) entity);
                if (playerNearby.isAdminBypassing()) {
                    continue;
                }
                if (playerNearby.getRelationTo(target) == Relation.ENEMY) {
                    return true;
                }
            }
        }
        return false;
    }

}
