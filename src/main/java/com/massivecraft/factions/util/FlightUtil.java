package com.massivecraft.factions.util;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.particle.ParticleProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class FlightUtil<E> {

    private static FlightUtil<?> instance;

    private EnemiesTask enemiesTask;
    private final ParticleProvider<E> particleProvider;

    private FlightUtil(ParticleProvider<E> particleProvider) {
        this.particleProvider = particleProvider;
        double enemyCheck = FactionsPlugin.getInstance().conf().commands().fly().getRadiusCheck() * 20;
        if (enemyCheck > 0) {
            enemiesTask = new EnemiesTask();
            enemiesTask.runTaskTimer(FactionsPlugin.getInstance(), 0, (long) enemyCheck);
        }

        double spawnRate = FactionsPlugin.getInstance().conf().commands().fly().particles().getSpawnRate() * 20;
        if (spawnRate > 0) {
            new ParticleTrailsTask().runTaskTimer(FactionsPlugin.getInstance(), 0, (long) spawnRate);
        }
    }

    public static <E> void start(ParticleProvider<E> particleProvider) {
        instance = new FlightUtil<>(particleProvider);
    }

    public static FlightUtil<?> instance() {
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
            Collection<FPlayer> players = FPlayers.getInstance().getOnlinePlayers();
            for (Player player : Bukkit.getOnlinePlayers()) {
                FPlayer pilot = FPlayers.getInstance().getByPlayer(player);
                if (pilot.isFlying() && !pilot.isAdminBypassing()) {
                    if (enemiesNearby(pilot, FactionsPlugin.getInstance().conf().commands().fly().getEnemyRadius(), players)) {
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
            return this.enemiesNearby(target, radius, FPlayers.getInstance().getOnlinePlayers());
        }

        public boolean enemiesNearby(FPlayer target, int radius, Collection<FPlayer> players) {
            if (!FactionsPlugin.getInstance().worldUtil().isEnabled(target.getPlayer().getWorld())) {
                return false;
            }
            int radiusSquared = radius * radius;
            Location loc = target.getPlayer().getLocation();
            Location cur = new Location(loc.getWorld(), 0, 0, 0);
            for (FPlayer player : players) {
                if (player == target || player.isAdminBypassing()) {
                    continue;
                }

                player.getPlayer().getLocation(cur);
                if (cur.getWorld().getUID().equals(loc.getWorld().getUID()) &&
                        cur.distanceSquared(loc) <= radiusSquared &&
                        player.getRelationTo(target) == Relation.ENEMY &&
                        target.getPlayer().canSee(player.getPlayer())) {
                    return true;
                }
            }
            return false;
        }
    }

    public class ParticleTrailsTask extends BukkitRunnable {

        private final int amount;
        private final float speed;

        private ParticleTrailsTask() {
            this.amount = FactionsPlugin.getInstance().conf().commands().fly().particles().getAmount();
            this.speed = (float) FactionsPlugin.getInstance().conf().commands().fly().particles().getSpeed();
        }

        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                FPlayer pilot = FPlayers.getInstance().getByPlayer(player);
                if (pilot.isFlying()) {
                    if (pilot.getFlyTrailsEffect() != null && Permission.FLY_TRAILS.has(player) && pilot.getFlyTrailsState()) {
                        E effect = particleProvider.effectFromString(pilot.getFlyTrailsEffect());
                        particleProvider.spawn(effect, player.getLocation(), amount, speed, 0, 0, 0);
                    }
                }
            }
        }

    }

}
