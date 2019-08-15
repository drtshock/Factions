package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class FlightUtil {

    private static FlightUtil instance;

    private EnemiesTask enemiesTask;

    private FlightUtil() {
        double enemyCheck = FactionsPlugin.getInstance().getConfig().getDouble("f-fly.radius-check", 1) * 20;
        if (enemyCheck > 0) {
            enemiesTask = new EnemiesTask();
            enemiesTask.runTaskTimer(FactionsPlugin.getInstance(), 0, (long) enemyCheck);
        }

        double spawnRate = FactionsPlugin.getInstance().getConfig().getDouble("f-fly.trails.spawn-rate", 0) * 20;
        if (spawnRate > 0) {
            new ParticleTrailsTask().runTaskTimer(FactionsPlugin.getInstance(), 0, (long) spawnRate);
        }
    }

    public static void start() {
        instance = new FlightUtil();
    }

    public static FlightUtil instance() {
        return instance;
    }

    public boolean enemiesNearby(FPlayer target, int radius) {
        if (this.enemiesTask == null) {
            return false;
        } else {
            return this.enemiesTask.enemiesNearby(target, radius);
        }
    }

    public class EnemiesTask extends BukkitRunnable {

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                FPlayer pilot = FPlayers.getInstance().getByPlayer(player);
                if (pilot.isFlying() && !pilot.isAdminBypassing()) {
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

        public boolean enemiesNearby(FPlayer target, int radius) {
            List<Entity> nearbyEntities = target.getPlayer().getNearbyEntities(radius, radius, radius);
            for (Entity entity : nearbyEntities) {
                if (entity instanceof Player) {
                    FPlayer playerNearby = FPlayers.getInstance().getByPlayer((Player) entity);
                    if (playerNearby.isAdminBypassing()) {
                        continue;
                    }
                    if (playerNearby.getRelationTo(target) == Relation.ENEMY) {
                        if (playerNearby.isFlying()) {
                            playerNearby.msg(TL.COMMAND_FLY_ENEMY_DISABLE);
                            playerNearby.setFlying(false);
                            if (playerNearby.isAutoFlying()) {
                                playerNearby.setAutoFlying(false);
                            }
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public class ParticleTrailsTask extends BukkitRunnable {

        private int amount;
        private float speed;

        private ParticleTrailsTask() {
            this.amount = FactionsPlugin.getInstance().getConfig().getInt("f-fly.trails.amount", 20);
            this.speed = (float) FactionsPlugin.getInstance().getConfig().getDouble("f-fly.trails.speed", 0.02);
        }

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                FPlayer pilot = FPlayers.getInstance().getByPlayer(player);
                if (pilot.isFlying()) {
                    if (pilot.getFlyTrailsEffect() != null && Permission.FLY_TRAILS.has(player) && pilot.getFlyTrailsState()) {
                        Object effect = FactionsPlugin.getInstance().getParticleProvider().effectFromString(pilot.getFlyTrailsEffect());
                        FactionsPlugin.getInstance().getParticleProvider().spawn(effect, player.getLocation(), amount, speed, 0, 0, 0);
                    }
                }
            }
        }

    }

}
