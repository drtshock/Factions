package com.massivecraft.factions.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.util.TL;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;


public class FactionsEntityListener extends AbstractListener {

    public FactionsPlugin plugin;

    public FactionsEntityListener(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            FactionsPlugin.getInstance().getLandRaidControl().onDeath((Player) entity);
        }
    }

    /**
     * Who can I hurt? I can never hurt members or allies. I can always hurt enemies. I can hurt neutrals as long as
     * they are outside their own territory.
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent) event;
            if (!this.canDamagerHurtDamagee(sub, true)) {
                event.setCancelled(true);
            }
        } else if (FactionsPlugin.getInstance().conf().factions().protection().isSafeZonePreventAllDamageToPlayers() && isPlayerInSafeZone(event.getEntity())) {
            // Players can not take any damage in a Safe Zone
            event.setCancelled(true);
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            if (fPlayer != null && !fPlayer.shouldTakeFallDamage()) {
                event.setCancelled(true); // Falling after /f fly
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageMonitor(EntityDamageEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        Entity damagee = event.getEntity();
        boolean playerHurt = damagee instanceof Player;

        if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();

            if (damager instanceof Projectile) {
                Projectile projectile = (Projectile) damager;

                if (projectile.getShooter() instanceof Entity) {
                    damager = (Entity) projectile.getShooter();
                }
            }

            if (playerHurt) {
                cancelFStuckTeleport((Player) damagee);
                cancelFFly((Player) damagee);
            }
            if (damager instanceof Player) {
                cancelFStuckTeleport((Player) damager);
                if ((playerHurt && plugin.conf().commands().fly().isDisableOnHurtingPlayers()) ||
                        (!playerHurt && plugin.conf().commands().fly().isDisableOnHurtingMobs())) {
                    cancelFFly((Player) damager);
                }
            }
        }

        // entity took generic damage?
        if (playerHurt) {
            Player player = (Player) damagee;
            FPlayer me = FPlayers.getInstance().getByPlayer(player);
            cancelFStuckTeleport(player);
            if (plugin.conf().commands().fly().isDisableOnGenericDamage()) {
                cancelFFly(player);
            }
            if (me.isWarmingUp()) {
                me.clearWarmup();
                me.msg(TL.WARMUPS_CANCELLED);
            }
        }
    }

    private void cancelFFly(Player player) {
        if (player == null) {
            return;
        }

        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if (fPlayer.isFlying()) {
            fPlayer.setFlying(false, true);
            if (fPlayer.isAutoFlying()) {
                fPlayer.setAutoFlying(false);
            }
        }
    }

    public void cancelFStuckTeleport(Player player) {
        if (player == null) {
            return;
        }
        UUID uuid = player.getUniqueId();
        if (FactionsPlugin.getInstance().getStuckMap().containsKey(uuid)) {
            FPlayers.getInstance().getByPlayer(player).msg(TL.COMMAND_STUCK_CANCELLED);
            FactionsPlugin.getInstance().getStuckMap().remove(uuid);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        this.handleExplosion(event.getLocation(), event.getEntity(), event, event.blockList());
    }

    // mainly for flaming arrows; don't want allies or people in safe zones to be ignited even after damage event is cancelled
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent(event.getCombuster(), event.getEntity(), EntityDamageEvent.DamageCause.FIRE, 0d);
        if (!this.canDamagerHurtDamagee(sub, false)) {
            event.setCancelled(true);
        }
    }

    private static final Set<PotionEffectType> badPotionEffects = new LinkedHashSet<>(Arrays.asList(PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HARM, PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.SLOW, PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.WITHER));

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPotionSplashEvent(PotionSplashEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        // see if the potion has a harmful effect
        boolean badjuju = false;
        for (PotionEffect effect : event.getPotion().getEffects()) {
            if (badPotionEffects.contains(effect.getType())) {
                badjuju = true;
                break;
            }
        }
        if (!badjuju) {
            return;
        }

        ProjectileSource thrower = event.getPotion().getShooter();
        if (!(thrower instanceof Entity)) {
            return;
        }

        if (thrower instanceof Player) {
            Player player = (Player) thrower;
            FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
            if (fPlayer.getFaction().isPeaceful()) {
                for (PotionEffect effect : event.getPotion().getEffects()) {
                    if (effect.getType().equals(PotionEffectType.WEAKNESS)) {
                        // make sure this weakness potion only affects zombie villagers
                        for (LivingEntity target : event.getAffectedEntities()) {
                            if (target.getType() != EntityType.ZOMBIE_VILLAGER) {
                                event.setIntensity(target, 0);
                            }
                        }
                        return;
                    }
                }

                event.setCancelled(true);
                return;
            }
        }

        // scan through affected entities to make sure they're all valid targets
        for (LivingEntity target : event.getAffectedEntities()) {
            EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent((Entity) thrower, target, EntityDamageEvent.DamageCause.CUSTOM, 0);
            if (!this.canDamagerHurtDamagee(sub, true)) {
                event.setIntensity(target, 0.0);  // affected entity list doesn't accept modification (so no iter.remove()), but this works
            }
        }
    }

    public boolean isPlayerInSafeZone(Entity damagee) {
        if (!(damagee instanceof Player)) {
            return false;
        }
        return Board.getInstance().getFactionAt(new FLocation(damagee.getLocation())).isSafeZone();
    }

    public boolean canDamagerHurtDamagee(EntityDamageByEntityEvent sub) {
        return canDamagerHurtDamagee(sub, true);
    }

    public boolean canDamagerHurtDamagee(EntityDamageByEntityEvent sub, boolean notify) {
        Entity damager = sub.getDamager();
        Entity damagee = sub.getEntity();

        // for damage caused by projectiles, getDamager() returns the projectile... what we need to know is the source
        if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;

            if (!(projectile.getShooter() instanceof Entity)) {
                return true;
            }

            damager = (Entity) projectile.getShooter();
        }

        if (damager instanceof Player) {
            Player player = (Player) damager;
            Material material = null;
            switch (sub.getEntity().getType()) {
                case ITEM_FRAME:
                    material = Material.ITEM_FRAME;
                    break;
                case ARMOR_STAND:
                    material = Material.ARMOR_STAND;
                    break;
            }
            if (material != null && !canPlayerUseBlock(player, material, damagee.getLocation(), false)) {
                return false;
            }
        }

        if (!(damagee instanceof Player)) {
            return true;
        }

        FPlayer defender = FPlayers.getInstance().getByPlayer((Player) damagee);

        if (defender == null || defender.getPlayer() == null) {
            return true;
        }

        Location defenderLoc = defender.getPlayer().getLocation();
        Faction defLocFaction = Board.getInstance().getFactionAt(new FLocation(defenderLoc));

        if (damager == damagee) {  // ender pearl usage and other self-inflicted damage
            return true;
        }

        // Players can not take attack damage in a SafeZone, or possibly peaceful territory
        if (defLocFaction.noPvPInTerritory()) {
            if (damager instanceof Player) {
                if (notify) {
                    FPlayer attacker = FPlayers.getInstance().getByPlayer((Player) damager);
                    attacker.msg(TL.PLAYER_CANTHURT, (defLocFaction.isSafeZone() ? TL.REGION_SAFEZONE.toString() : TL.REGION_PEACEFUL.toString()));
                }
                return false;
            }
            return !defLocFaction.noMonstersInTerritory();
        }

        if (!(damager instanceof Player)) {
            return true;
        }

        FPlayer attacker = FPlayers.getInstance().getByPlayer((Player) damager);

        if (attacker == null || attacker.getPlayer() == null) {
            return true;
        }

        MainConfig.Factions facConf = FactionsPlugin.getInstance().conf().factions();
        if (facConf.protection().getPlayersWhoBypassAllProtection().contains(attacker.getName())) {
            return true;
        }

        if (attacker.hasLoginPvpDisabled()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_LOGIN, facConf.pvp().getNoPVPDamageToOthersForXSecondsAfterLogin());
            }
            return false;
        }

        Faction locFaction = Board.getInstance().getFactionAt(new FLocation(attacker));

        // so we know from above that the defender isn't in a safezone... what about the attacker, sneaky dog that he might be?
        if (locFaction.noPvPInTerritory()) {
            if (notify) {
                attacker.msg(TL.PLAYER_CANTHURT, (locFaction.isSafeZone() ? TL.REGION_SAFEZONE.toString() : TL.REGION_PEACEFUL.toString()));
            }
            return false;
        }

        if (locFaction.isWarZone() && facConf.protection().isWarZoneFriendlyFire()) {
            return true;
        }

        if (facConf.pvp().getWorldsIgnorePvP().contains(defenderLoc.getWorld().getName())) {
            return true;
        }

        Faction defendFaction = defender.getFaction();
        Faction attackFaction = attacker.getFaction();

        if (attackFaction.isWilderness() && facConf.pvp().isDisablePVPForFactionlessPlayers()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_REQUIREFACTION);
            }
            return false;
        } else if (defendFaction.isWilderness()) {
            if (defLocFaction == attackFaction && facConf.pvp().isEnablePVPAgainstFactionlessInAttackersLand()) {
                // Allow PVP vs. Factionless in attacker's faction territory
                return true;
            } else if (facConf.pvp().isDisablePVPForFactionlessPlayers()) {
                if (notify) {
                    attacker.msg(TL.PLAYER_PVP_FACTIONLESS);
                }
                return false;
            }
        }

        if (!defLocFaction.isWarZone() || facConf.pvp().isDisablePeacefulPVPInWarzone()) {
            if (defendFaction.isPeaceful()) {
                if (notify) {
                    attacker.msg(TL.PLAYER_PVP_PEACEFUL);
                }
                return false;
            } else if (attackFaction.isPeaceful()) {
                if (notify) {
                    attacker.msg(TL.PLAYER_PVP_PEACEFUL);
                }
                return false;
            }
        }

        Relation relation = defendFaction.getRelationTo(attackFaction);

        // You can not hurt neutral factions
        if (facConf.pvp().isDisablePVPBetweenNeutralFactions() && relation.isNeutral()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_NEUTRAL);
            }
            return false;
        }

        // Players without faction may be hurt anywhere
        if (!defender.hasFaction()) {
            return true;
        }

        // You can never hurt faction members or allies
        if (relation.isMember() || relation.isAlly() || relation.isTruce()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_CANTHURT, defender.describeTo(attacker));
            }
            return false;
        }

        boolean ownTerritory = defender.isInOwnTerritory();

        // You can not hurt neutrals in their own territory.
        if (ownTerritory && relation.isNeutral()) {
            if (notify) {
                attacker.msg(TL.PLAYER_PVP_NEUTRALFAIL, defender.describeTo(attacker));
                defender.msg(TL.PLAYER_PVP_TRIED, attacker.describeTo(defender, true));
            }
            return false;
        }

        // Damage will be dealt. However check if the damage should be reduced.
        /*
        if (damage > 0.0 && ownTerritory && Conf.territoryShieldFactor > 0) {
            double newDamage = Math.ceil(damage * (1D - Conf.territoryShieldFactor));
            sub.setDamage(newDamage);

            // Send message
            if (notify) {
                String perc = MessageFormat.format("{0,number,#%}", (Conf.territoryShieldFactor)); // TODO does this display correctly??
                defender.msg("<i>Enemy damage reduced by <rose>%s<i>.", perc);
            }
        } */

        return true;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        if (event.getLocation() == null) { // Just in case
            return;
        }

        Faction faction = Board.getInstance().getFactionAt(new FLocation(event.getLocation()));
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        EntityType type = event.getEntityType();
        MainConfig.Factions.Spawning spawning = FactionsPlugin.getInstance().conf().factions().spawning();

        if (faction.isNormal()) {
            if (faction.isPeaceful() && FactionsPlugin.getInstance().conf().factions().specialCase().isPeacefulTerritoryDisableMonsters()) {
                if (event.getEntity() instanceof Monster) {
                    event.setCancelled(true);
                }
            }
            if (spawning.getPreventInTerritory().contains(reason) && !spawning.getPreventInTerritoryExceptions().contains(type)) {
                event.setCancelled(true);
            }
        } else if (faction.isSafeZone()) {
            if (spawning.getPreventInSafezone().contains(reason) && !spawning.getPreventInSafezoneExceptions().contains(type)) {
                event.setCancelled(true);
            }
        } else if (faction.isWarZone()) {
            if (spawning.getPreventInWarzone().contains(reason) && !spawning.getPreventInWarzoneExceptions().contains(type)) {
                event.setCancelled(true);
            }
        } else if (faction.isWilderness()) {
            if (spawning.getPreventInWilderness().contains(reason) && !spawning.getPreventInWildernessExceptions().contains(type)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityTarget(EntityTargetEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        // if there is a target
        Entity target = event.getTarget();
        if (target == null) {
            return;
        }

        if (event.getEntity() instanceof Monster && Board.getInstance().getFactionAt(new FLocation(target.getLocation())).noMonstersInTerritory()) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPaintingBreak(HangingBreakEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        if (event.getCause() == RemoveCause.EXPLOSION) {
            Location loc = event.getEntity().getLocation();
            Faction faction = Board.getInstance().getFactionAt(new FLocation(loc));
            if (faction.noExplosionsInTerritory()) {
                // faction is peaceful and has explosions set to disabled
                event.setCancelled(true);
                return;
            }

            boolean online = faction.hasPlayersOnline();
            MainConfig.Factions.Protection protection = FactionsPlugin.getInstance().conf().factions().protection();

            if ((faction.isWilderness() && !protection.getWorldsNoWildernessProtection().contains(loc.getWorld().getName()) && (protection.isWildernessBlockCreepers() || protection.isWildernessBlockFireballs() || protection.isWildernessBlockTNT())) ||
                    (faction.isNormal() && (online ? (protection.isTerritoryBlockCreepers() || protection.isTerritoryBlockFireballs() || protection.isTerritoryBlockTNT()) : (protection.isTerritoryBlockCreepersWhenOffline() || protection.isTerritoryBlockFireballsWhenOffline() || protection.isTerritoryBlockTNTWhenOffline()))) ||
                    (faction.isWarZone() && (protection.isWarZoneBlockCreepers() || protection.isWarZoneBlockFireballs() || protection.isWarZoneBlockTNT())) ||
                    faction.isSafeZone()) {
                // explosion which needs prevention
                event.setCancelled(true);
            }
        }

        if (!(event instanceof HangingBreakByEntityEvent)) {
            return;
        }

        Entity breaker = ((HangingBreakByEntityEvent) event).getRemover();
        if (!(breaker instanceof Player)) {
            return;
        }

        if (!FactionsBlockListener.playerCanBuildDestroyBlock((Player) breaker, event.getEntity().getLocation(), PermissibleAction.DESTROY, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPaintingPlace(HangingPlaceEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        if (!FactionsBlockListener.playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getRelative(event.getBlockFace()).getLocation(), PermissibleAction.BUILD, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (!plugin.worldUtil().isEnabled(event.getEntity().getWorld())) {
            return;
        }

        Entity entity = event.getEntity();

        // for now, only interested in Enderman and Wither boss tomfoolery
        if (!(entity instanceof Enderman) && !(entity instanceof Wither)) {
            return;
        }

        Location loc = event.getBlock().getLocation();

        if (entity instanceof Enderman) {
            if (stopEndermanBlockManipulation(loc)) {
                event.setCancelled(true);
            }
        } else if (entity instanceof Wither) {
            Faction faction = Board.getInstance().getFactionAt(new FLocation(loc));
            MainConfig.Factions.Protection protection = FactionsPlugin.getInstance().conf().factions().protection();
            // it's a bit crude just using fireball protection, but I'd rather not add in a whole new set of xxxBlockWitherExplosion or whatever
            if ((faction.isWilderness() && protection.isWildernessBlockFireballs() && !protection.getWorldsNoWildernessProtection().contains(loc.getWorld().getName())) ||
                    (faction.isNormal() && (faction.hasPlayersOnline() ? protection.isTerritoryBlockFireballs() : protection.isTerritoryBlockFireballsWhenOffline())) ||
                    (faction.isWarZone() && protection.isWarZoneBlockFireballs()) ||
                    faction.isSafeZone()) {
                event.setCancelled(true);
            }
        }
    }

    private boolean stopEndermanBlockManipulation(Location loc) {
        if (loc == null) {
            return false;
        }
        // quick check to see if all Enderman deny options are enabled; if so, no need to check location
        MainConfig.Factions.Protection protection = FactionsPlugin.getInstance().conf().factions().protection();
        if (protection.isWildernessDenyEndermanBlocks() &&
                protection.isTerritoryDenyEndermanBlocks() &&
                protection.isTerritoryDenyEndermanBlocksWhenOffline() &&
                protection.isSafeZoneDenyEndermanBlocks() &&
                protection.isWarZoneDenyEndermanBlocks()) {
            return true;
        }

        FLocation fLoc = new FLocation(loc);
        Faction claimFaction = Board.getInstance().getFactionAt(fLoc);

        if (claimFaction.isWilderness()) {
            return protection.isWildernessDenyEndermanBlocks();
        } else if (claimFaction.isNormal()) {
            return claimFaction.hasPlayersOnline() ? protection.isTerritoryDenyEndermanBlocks() : protection.isTerritoryDenyEndermanBlocksWhenOffline();
        } else if (claimFaction.isSafeZone()) {
            return protection.isSafeZoneDenyEndermanBlocks();
        } else if (claimFaction.isWarZone()) {
            return protection.isWarZoneDenyEndermanBlocks();
        }

        return false;
    }
}
