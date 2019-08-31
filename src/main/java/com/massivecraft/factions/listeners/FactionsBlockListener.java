package com.massivecraft.factions.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.struct.Permission;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.EntityBlockFormEvent;


public class FactionsBlockListener implements Listener {

    public FactionsPlugin plugin;

    public FactionsBlockListener(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.canBuild()) {
            return;
        }

        // special case for flint&steel, which should only be prevented by DenyUsage list
        if (event.getBlockPlaced().getType() == Material.FIRE) {
            return;
        }

        if (!playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getLocation(), PermissibleAction.BUILD, "build", false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event) {
        if (!FactionsPlugin.getInstance().conf().exploits().isLiquidFlow()) {
            return;
        }
        if (event.getBlock().isLiquid()) {
            if (event.getToBlock().isEmpty()) {
                Faction from = Board.getInstance().getFactionAt(new FLocation(event.getBlock()));
                Faction to = Board.getInstance().getFactionAt(new FLocation(event.getToBlock()));
                if (from == to) {
                    // not concerned with inter-faction events
                    return;
                }
                // from faction != to faction
                if (to.isNormal()) {
                    if (from.isNormal() && from.getRelationTo(to).isAlly()) {
                        return;
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getLocation(), PermissibleAction.DESTROY, "destroy", false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        if (event.getInstaBreak() && !playerCanBuildDestroyBlock(event.getPlayer(), event.getBlock().getLocation(), PermissibleAction.DESTROY, "destroy", false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (!FactionsPlugin.getInstance().conf().factions().protection().isPistonProtectionThroughDenyBuild()) {
            return;
        }

        Faction pistonFaction = Board.getInstance().getFactionAt(new FLocation(event.getBlock()));

        // target end-of-the-line empty (air) block which is being pushed into, including if piston itself would extend into air
        Block targetBlock = event.getBlock().getRelative(event.getDirection(), event.getLength() + 1);

        // if potentially pushing into air/water/lava in another territory, we need to check it out
        if ((targetBlock.isEmpty() || targetBlock.isLiquid()) && !canPistonMoveBlock(pistonFaction, targetBlock.getLocation())) {
            event.setCancelled(true);
        }

        /*
         * note that I originally was testing the territory of each affected block, but since I found that pistons can only push
         * up to 12 blocks and the width of any territory is 16 blocks, it should be safe (and much more lightweight) to test
         * only the final target block as done above
         */
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        // if not a sticky piston, retraction should be fine
        if (!event.isSticky() || !FactionsPlugin.getInstance().conf().factions().protection().isPistonProtectionThroughDenyBuild()) {
            return;
        }

        // if the retracted blocks list is empty, no worries
        if (event.getBlocks().isEmpty()) {
            return;
        }

        // get the first potentially retracted block
        Location targetLoc = event.getBlocks().get(0).getLocation();
        Faction otherFaction = Board.getInstance().getFactionAt(new FLocation(targetLoc));

        // Check if the piston is moving in a faction's territory. This disables pistons entirely in faction territory.
        if (otherFaction.isNormal() && FactionsPlugin.getInstance().getConfig().getBoolean("disable-pistons-in-territory", false)) {
            event.setCancelled(true);
            return;
        }

        // if potentially retracted block is just air/water/lava, no worries
        if (targetLoc.getBlock().isEmpty() || targetLoc.getBlock().isLiquid()) {
            return;
        }

        Faction pistonFaction = Board.getInstance().getFactionAt(new FLocation(event.getBlock()));

        if (!canPistonMoveBlock(pistonFaction, targetLoc)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onFrostWalker(EntityBlockFormEvent event) {
        if (event.getEntity() == null || event.getEntity().getType() != EntityType.PLAYER || event.getBlock() == null) {
            return;
        }

        Player player = (Player) event.getEntity();
        Location location = event.getBlock().getLocation();

        // only notify every 10 seconds
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        boolean justCheck = fPlayer.getLastFrostwalkerMessage() + 10000 > System.currentTimeMillis();
        if (!justCheck) {
            fPlayer.setLastFrostwalkerMessage();
        }

        // Check if they have build permissions here. If not, block this from happening.
        if (!playerCanBuildDestroyBlock(player, location, PermissibleAction.FROSTWALK, "frostwalk", justCheck)) {
            event.setCancelled(true);
        }
    }

    private boolean canPistonMoveBlock(Faction pistonFaction, Location target) {

        Faction otherFaction = Board.getInstance().getFactionAt(new FLocation(target));

        if (pistonFaction == otherFaction) {
            return true;
        }

        if (otherFaction.isWilderness()) {
            return !FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyBuild() || FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(target.getWorld().getName());

        } else if (otherFaction.isSafeZone()) {
            return !FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyBuild();

        } else if (otherFaction.isWarZone()) {
            return !FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyBuild();

        }

        Relation rel = pistonFaction.getRelationTo(otherFaction);

        return otherFaction.hasAccess(otherFaction.hasPlayersOnline(), rel, PermissibleAction.BUILD);
    }

    public static boolean playerCanBuildDestroyBlock(Player player, Location location, PermissibleAction permissibleAction, String action, boolean justCheck) {
        String name = player.getName();
        if (FactionsPlugin.getInstance().conf().factions().protection().getPlayersWhoBypassAllProtection().contains(name)) {
            return true;
        }

        FPlayer me = FPlayers.getInstance().getById(player.getUniqueId().toString());
        if (me.isAdminBypassing()) {
            return true;
        }

        FLocation loc = new FLocation(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);

        if (otherFaction.isWilderness()) {
            if (FactionsPlugin.getInstance().conf().worldGuard().isBuildPriority() && FactionsPlugin.getInstance().getWorldguard() != null && FactionsPlugin.getInstance().getWorldguard().playerCanBuild(player, location)) {
                return true;
            }

            if (!FactionsPlugin.getInstance().conf().factions().protection().isWildernessDenyBuild() || FactionsPlugin.getInstance().conf().factions().protection().getWorldsNoWildernessProtection().contains(location.getWorld().getName())) {
                return true; // This is not faction territory. Use whatever you like here.
            }

            if (!justCheck) {
                me.msg("<b>You can't " + action + " in the wilderness.");
            }

            return false;
        } else if (otherFaction.isSafeZone()) {
            if (FactionsPlugin.getInstance().conf().worldGuard().isBuildPriority() && FactionsPlugin.getInstance().getWorldguard() != null && FactionsPlugin.getInstance().getWorldguard().playerCanBuild(player, location)) {
                return true;
            }

            if (!FactionsPlugin.getInstance().conf().factions().protection().isSafeZoneDenyBuild() || Permission.MANAGE_SAFE_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg("<b>You can't " + action + " in a safe zone.");
            }

            return false;
        } else if (otherFaction.isWarZone()) {
            if (FactionsPlugin.getInstance().conf().worldGuard().isBuildPriority() && FactionsPlugin.getInstance().getWorldguard() != null && FactionsPlugin.getInstance().getWorldguard().playerCanBuild(player, location)) {
                return true;
            }

            if (!FactionsPlugin.getInstance().conf().factions().protection().isWarZoneDenyBuild() || Permission.MANAGE_WAR_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg("<b>You can't " + action + " in a war zone.");
            }

            return false;
        }
        if (FactionsPlugin.getInstance().getConfig().getBoolean("hcf.raidable", false) && otherFaction.getLandRounded() >= otherFaction.getPowerRounded()) {
            return true;
        }

        Faction myFaction = me.getFaction();
        boolean pain = !justCheck && otherFaction.hasAccess(me, PermissibleAction.PAINBUILD);

        // If the faction hasn't: defined access or denied, fallback to config values
        if (!otherFaction.hasAccess(me, permissibleAction)) {
            if (pain && permissibleAction != PermissibleAction.FROSTWALK) {
                player.damage(FactionsPlugin.getInstance().conf().factions().getActionDeniedPainAmount());
                me.msg("<b>It is painful to " + action + " in the territory of " + otherFaction.getTag(myFaction));
                return true;
            } else if (!justCheck) {
                me.msg("<b>You can't " + action + " in the territory of " + otherFaction.getTag(myFaction));
            }
            return false;
        }

        // Also cancel and/or cause pain if player doesn't have ownership rights for this claim
        if (FactionsPlugin.getInstance().conf().factions().ownedArea().isEnabled() && (FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyBuild() || FactionsPlugin.getInstance().conf().factions().ownedArea().isPainBuild()) && !otherFaction.playerHasOwnershipRights(me, loc)) {
            if (pain && FactionsPlugin.getInstance().conf().factions().ownedArea().isPainBuild()) {
                player.damage(FactionsPlugin.getInstance().conf().factions().getActionDeniedPainAmount());

                if (!FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyBuild()) {
                    me.msg("<b>It is painful to try to " + action + " in this territory, it is owned by: " + otherFaction.getOwnerListString(loc));
                }
            }
            if (FactionsPlugin.getInstance().conf().factions().ownedArea().isDenyBuild()) {
                if (!justCheck) {
                    me.msg("<b>You can't " + action + " in this territory, it is owned by: " + otherFaction.getOwnerListString(loc));
                }

                return false;
            }
        }

        return true;
    }
}
