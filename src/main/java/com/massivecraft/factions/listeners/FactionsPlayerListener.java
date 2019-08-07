package com.massivecraft.factions.listeners;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import com.massivecraft.factions.*;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.gui.GUI;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.scoreboards.FScoreboard;
import com.massivecraft.factions.scoreboards.FTeamWrapper;
import com.massivecraft.factions.scoreboards.sidebar.FDefaultSidebar;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.VisualizeUtil;
import com.massivecraft.factions.zcore.persist.MemoryFPlayer;
import com.massivecraft.factions.zcore.util.TL;
import com.massivecraft.factions.zcore.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;


public class FactionsPlayerListener extends AbstractListener {

    private P p;

    public FactionsPlayerListener(P p) {
        this.p = p;
        for (Player player : p.getServer().getOnlinePlayers()) {
            initPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        initPlayer(event.getPlayer());
    }

    private void initPlayer(Player player) {
        // Make sure that all online players do have a fplayer.
        final FPlayer me = FPlayers.getInstance().getByPlayer(player);
        ((MemoryFPlayer) me).setName(player.getName());

        // Update the lastLoginTime for this fplayer
        me.setLastLoginTime(System.currentTimeMillis());

        // Store player's current FLocation and notify them where they are
        me.setLastStoodAt(new FLocation(player.getLocation()));

        me.login(); // set kills / deaths

        // Check for Faction announcements. Let's delay this so they actually see it.
        new BukkitRunnable() {
            @Override
            public void run() {
                if (me.isOnline()) {
                    me.getFaction().sendUnreadAnnouncements(me);
                }
            }
        }.runTaskLater(P.p, 33L); // Don't ask me why.

        if (P.p.getConfig().getBoolean("scoreboard.default-enabled", false)) {
            FScoreboard.init(me);
            FScoreboard.get(me).setDefaultSidebar(new FDefaultSidebar(), P.p.getConfig().getInt("default-update-interval", 20));
            FScoreboard.get(me).setSidebarVisibility(me.showScoreboard());
        }

        Faction myFaction = me.getFaction();
        if (!myFaction.isWilderness()) {
            for (FPlayer other : myFaction.getFPlayersWhereOnline(true)) {
                if (other != me && other.isMonitoringJoins()) {
                    other.msg(TL.FACTION_LOGIN, me.getName());
                }
            }
        }

        if (me.isSpyingChat() && !player.hasPermission(Permission.CHATSPY.node)) {
            me.setSpyingChat(false);
            P.p.log(Level.INFO, "Found %s spying chat without permission on login. Disabled their chat spying.", player.getName());
        }

        if (me.isAdminBypassing() && !player.hasPermission(Permission.BYPASS.node)) {
            me.setIsAdminBypassing(false);
            P.p.log(Level.INFO, "Found %s on admin Bypass without permission on login. Disabled it for them.", player.getName());
        }

        // If they have the permission, don't let them autoleave. Bad inverted setter :\
        me.setAutoLeave(!player.hasPermission(Permission.AUTO_LEAVE_BYPASS.node));
        me.setTakeFallDamage(true);

        P.p.seeChunkUtil.updatePlayerInfo(UUID.fromString(me.getId()), me.isSeeingChunk());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuit(PlayerQuitEvent event) {
        FPlayer me = FPlayers.getInstance().getByPlayer(event.getPlayer());

        // Make sure player's power is up to date when they log off.
        me.getPower();
        // and update their last login time to point to when the logged off, for auto-remove routine
        me.setLastLoginTime(System.currentTimeMillis());

        me.logout(); // cache kills / deaths

        // if player is waiting for fstuck teleport but leaves, remove
        if (P.p.getStuckMap().containsKey(me.getPlayer().getUniqueId())) {
            FPlayers.getInstance().getByPlayer(me.getPlayer()).msg(TL.COMMAND_STUCK_CANCELLED);
            P.p.getStuckMap().remove(me.getPlayer().getUniqueId());
            P.p.getTimers().remove(me.getPlayer().getUniqueId());
        }

        Faction myFaction = me.getFaction();
        if (!myFaction.isWilderness()) {
            myFaction.memberLoggedOff();
        }

        if (!myFaction.isWilderness()) {
            for (FPlayer player : myFaction.getFPlayersWhereOnline(true)) {
                if (player != me && player.isMonitoringJoins()) {
                    player.msg(TL.FACTION_LOGOUT, me.getName());
                }
            }
        }

        FScoreboard.remove(me);

        if (P.p.seeChunkUtil != null) {
            P.p.seeChunkUtil.updatePlayerInfo(UUID.fromString(me.getId()), false);
        }
    }

    // Holds the next time a player can have a map shown.
    private HashMap<UUID, Long> showTimes = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        FPlayer me = FPlayers.getInstance().getByPlayer(player);

        // clear visualization
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockY() != event.getTo().getBlockY() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
            VisualizeUtil.clear(event.getPlayer());
            if (me.isWarmingUp()) {
                me.clearWarmup();
                me.msg(TL.WARMUPS_CANCELLED);
            }
        }

        // quick check to make sure player is moving between chunks; good performance boost
        if (event.getFrom().getBlockX() >> 4 == event.getTo().getBlockX() >> 4 && event.getFrom().getBlockZ() >> 4 == event.getTo().getBlockZ() >> 4 && event.getFrom().getWorld() == event.getTo().getWorld()) {
            return;
        }

        // Did we change coord?
        FLocation from = me.getLastStoodAt();
        FLocation to = new FLocation(event.getTo());

        if (from.equals(to)) {
            return;
        }

        // Yes we did change coord (:

        me.setLastStoodAt(to);

        // Did we change "host"(faction)?
        Faction factionFrom = Board.getInstance().getFactionAt(from);
        Faction factionTo = Board.getInstance().getFactionAt(to);
        boolean changedFaction = (factionFrom != factionTo);

        if (p.getConfig().getBoolean("f-fly.enable", false) && changedFaction && !me.isAdminBypassing()) {
            boolean canFly = me.canFlyAtLocation();
            if (me.isFlying() && !canFly) {
                me.setFlying(false);
            } else if (me.isAutoFlying() && !me.isFlying() && canFly) {
                me.setFlying(true);
            }
        }

        if (me.isMapAutoUpdating()) {
            if (showTimes.containsKey(player.getUniqueId()) && (showTimes.get(player.getUniqueId()) > System.currentTimeMillis()) && !me.isFlying()) {
                if (P.p.getConfig().getBoolean("findfactionsexploit.log", false)) {
                    P.p.log(Level.WARNING, "%s tried to show a faction map too soon and triggered exploit blocker.", player.getName());
                }
            } else {
                me.sendFancyMessage(Board.getInstance().getMap(me, to, player.getLocation().getYaw()));
                showTimes.put(player.getUniqueId(), System.currentTimeMillis() + P.p.getConfig().getLong("findfactionsexploit.cooldown", 2000));
            }
        } else {
            Faction myFaction = me.getFaction();
            String ownersTo = myFaction.getOwnerListString(to);

            if (changedFaction) {
                me.sendFactionHereMessage(factionFrom);
                if (P.p.conf().factions().ownedArea().isEnabled() && P.p.conf().factions().ownedArea().isMessageOnBorder() && myFaction == factionTo && !ownersTo.isEmpty()) {
                    me.sendMessage(TL.GENERIC_OWNERS.format(ownersTo));
                }
            } else if (P.p.conf().factions().ownedArea().isEnabled() && P.p.conf().factions().ownedArea().isMessageInsideTerritory() && myFaction == factionTo && !myFaction.isWilderness()) {
                String ownersFrom = myFaction.getOwnerListString(from);
                if (P.p.conf().factions().ownedArea().isMessageByChunk() || !ownersFrom.equals(ownersTo)) {
                    if (!ownersTo.isEmpty()) {
                        me.sendMessage(TL.GENERIC_OWNERS.format(ownersTo));
                    } else if (!TL.GENERIC_PUBLICLAND.toString().isEmpty()) {
                        me.sendMessage(TL.GENERIC_PUBLICLAND.toString());
                    }
                }
            }
        }

        if (me.getAutoClaimFor() != null) {
            me.attemptClaim(me.getAutoClaimFor(), event.getTo(), true);
        } else if (me.isAutoSafeClaimEnabled()) {
            if (!Permission.MANAGE_SAFE_ZONE.has(player)) {
                me.setIsAutoSafeClaimEnabled(false);
            } else {
                if (!Board.getInstance().getFactionAt(to).isSafeZone()) {
                    Board.getInstance().setFactionAt(Factions.getInstance().getSafeZone(), to);
                    me.msg(TL.PLAYER_SAFEAUTO);
                }
            }
        } else if (me.isAutoWarClaimEnabled()) {
            if (!Permission.MANAGE_WAR_ZONE.has(player)) {
                me.setIsAutoWarClaimEnabled(false);
            } else {
                if (!Board.getInstance().getFactionAt(to).isWarZone()) {
                    Board.getInstance().setFactionAt(Factions.getInstance().getWarZone(), to);
                    me.msg(TL.PLAYER_WARAUTO);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
        switch (event.getRightClicked().getType()) {
            case ITEM_FRAME:
                if (!canPlayerUseBlock(event.getPlayer(), Material.ITEM_FRAME, event.getRightClicked().getLocation(), false)) {
                    event.setCancelled(true);
                }
                break;
            case HORSE:
            case SKELETON_HORSE:
            case ZOMBIE_HORSE:
            case DONKEY:
            case MULE:
            case LLAMA:
            case TRADER_LLAMA:
            case PIG:
                if (!this.playerCanInteractHere(event.getPlayer(), event.getRightClicked().getLocation())) {
                    event.setCancelled(true);
                }
                break;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerArmorStandManipulateEvent event) {
        if (!canPlayerUseBlock(event.getPlayer(), Material.ARMOR_STAND, event.getRightClicked().getLocation(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        // only need to check right-clicks and physical as of MC 1.4+; good performance boost
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block == null) {
            return;  // clicked in air, apparently
        }

        if (!canPlayerUseBlock(player, block.getType(), block.getLocation(), false)) {
            event.setCancelled(true);
            if (P.p.conf().exploits().isInteractionSpam()) {
                String name = player.getName();
                InteractAttemptSpam attempt = interactSpammers.get(name);
                if (attempt == null) {
                    attempt = new InteractAttemptSpam();
                    interactSpammers.put(name, attempt);
                }
                int count = attempt.increment();
                if (count >= 10) {
                    FPlayer me = FPlayers.getInstance().getByPlayer(player);
                    me.msg(TL.PLAYER_OUCH);
                    player.damage(NumberConversions.floor((double) count / 10));
                }
            }
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;  // only interested on right-clicks for below
        }

        if (!playerCanUseItemHere(player, block.getLocation(), event.getMaterial(), false)) {
            event.setCancelled(true);
        }
    }


    // for handling people who repeatedly spam attempts to open a door (or similar) in another faction's territory
    private Map<String, InteractAttemptSpam> interactSpammers = new HashMap<>();

    private static class InteractAttemptSpam {
        private int attempts = 0;
        private long lastAttempt = System.currentTimeMillis();

        // returns the current attempt count
        public int increment() {
            long Now = System.currentTimeMillis();
            if (Now > lastAttempt + 2000) {
                attempts = 1;
            } else {
                attempts++;
            }
            lastAttempt = Now;
            return attempts;
        }
    }

    // TODO I feel terrible about this.
    public boolean playerCanInteractHere(Player player, Location location) {
        String name = player.getName();
        if (P.p.conf().factions().protection().getPlayersWhoBypassAllProtection().contains(name)) {
            return true;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        if (me.isAdminBypassing()) {
            return true;
        }

        FLocation loc = new FLocation(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);

        if (P.p.getConfig().getBoolean("hcf.raidable", false) && otherFaction.getLandRounded() >= otherFaction.getPowerRounded()) {
            return true;
        }

        if (otherFaction.isWilderness()) {
            if (!P.p.conf().factions().protection().isWildernessDenyUsage() || P.p.conf().factions().protection().getWorldsNoWildernessProtection().contains(location.getWorld().getName())) {
                return true; // This is not faction territory. Use whatever you like here.
            }
            me.msg(TL.PLAYER_USE_WILDERNESS, "this");
            return false;
        } else if (otherFaction.isSafeZone()) {
            if (!P.p.conf().factions().protection().isSafeZoneDenyUsage() || Permission.MANAGE_SAFE_ZONE.has(player)) {
                return true;
            }
            me.msg(TL.PLAYER_USE_SAFEZONE, "this");
            return false;
        } else if (otherFaction.isWarZone()) {
            if (!P.p.conf().factions().protection().isWarZoneDenyUsage() || Permission.MANAGE_WAR_ZONE.has(player)) {
                return true;
            }
            me.msg(TL.PLAYER_USE_WARZONE, "this");

            return false;
        }

        boolean access = otherFaction.hasAccess(me, PermissibleAction.ITEM);

        // Cancel if we are not in our own territory
        if (!access) {
            me.msg(TL.PLAYER_USE_TERRITORY, "this", otherFaction.getTag(me.getFaction()));
            return false;
        }

        // Also cancel if player doesn't have ownership rights for this claim
        if (P.p.conf().factions().ownedArea().isEnabled() && P.p.conf().factions().ownedArea().isDenyUsage() && !otherFaction.playerHasOwnershipRights(me, loc)) {
            me.msg(TL.PLAYER_USE_OWNED, "this", otherFaction.getOwnerListString(loc));
            return false;
        }

        return true;
    }


    public boolean playerCanUseItemHere(Player player, Location location, Material material, boolean justCheck) {
        String name = player.getName();
        if (P.p.conf().factions().protection().getPlayersWhoBypassAllProtection().contains(name)) {
            return true;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        if (me.isAdminBypassing()) {
            return true;
        }

        FLocation loc = new FLocation(location);
        Faction otherFaction = Board.getInstance().getFactionAt(loc);

        if (P.p.getConfig().getBoolean("hcf.raidable", false) && otherFaction.getLandRounded() >= otherFaction.getPowerRounded()) {
            return true;
        }

        if (otherFaction.hasPlayersOnline()) {
            if (!P.p.conf().factions().protection().getTerritoryDenyUsageMaterials().contains(material)) {
                return true; // Item isn't one we're preventing for online factions.
            }
        } else {
            if (!P.p.conf().factions().protection().getTerritoryDenyUsageMaterialsWhenOffline().contains(material)) {
                return true; // Item isn't one we're preventing for offline factions.
            }
        }

        if (otherFaction.isWilderness()) {
            if (!P.p.conf().factions().protection().isWildernessDenyUsage() || P.p.conf().factions().protection().getWorldsNoWildernessProtection().contains(location.getWorld().getName())) {
                return true; // This is not faction territory. Use whatever you like here.
            }

            if (!justCheck) {
                me.msg(TL.PLAYER_USE_WILDERNESS, TextUtil.getMaterialName(material));
            }

            return false;
        } else if (otherFaction.isSafeZone()) {
            if (!P.p.conf().factions().protection().isSafeZoneDenyUsage() || Permission.MANAGE_SAFE_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg(TL.PLAYER_USE_SAFEZONE, TextUtil.getMaterialName(material));
            }

            return false;
        } else if (otherFaction.isWarZone()) {
            if (!P.p.conf().factions().protection().isWarZoneDenyUsage() || Permission.MANAGE_WAR_ZONE.has(player)) {
                return true;
            }

            if (!justCheck) {
                me.msg(TL.PLAYER_USE_WARZONE, TextUtil.getMaterialName(material));
            }

            return false;
        }

        if (!otherFaction.hasAccess(me, PermissibleAction.ITEM)) {
            if (!justCheck) {
                me.msg(TL.PLAYER_USE_TERRITORY, TextUtil.getMaterialName(material), otherFaction.getTag(me.getFaction()));
            }
            return false;
        }

        // Also cancel if player doesn't have ownership rights for this claim
        if (P.p.conf().factions().ownedArea().isEnabled() && P.p.conf().factions().ownedArea().isDenyUsage() && !otherFaction.playerHasOwnershipRights(me, loc)) {
            if (!justCheck) {
                me.msg(TL.PLAYER_USE_OWNED, TextUtil.getMaterialName(material), otherFaction.getOwnerListString(loc));
            }

            return false;
        }

        return true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        FPlayer me = FPlayers.getInstance().getByPlayer(event.getPlayer());

        me.getPower();  // update power, so they won't have gained any while dead

        Location home = me.getFaction().getHome();
        if (P.p.conf().factions().homes().isEnabled() &&
                P.p.conf().factions().homes().isTeleportToOnDeath() &&
                home != null &&
                (P.p.conf().factions().homes().isRespawnFromNoPowerLossWorlds() || !P.p.conf().factions().landRaidControl().power().getWorldsNoPowerLoss().contains(event.getPlayer().getWorld().getName()))) {
            event.setRespawnLocation(home);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        FPlayer me = FPlayers.getInstance().getByPlayer(event.getPlayer());
        FLocation to = new FLocation(event.getTo());
        me.setLastStoodAt(to);

        // Check the location they're teleporting to and check if they can fly there.
        if (p.getConfig().getBoolean("f-fly.enable", false) && !me.isAdminBypassing()) {
            boolean canFly = me.canFlyAtLocation(to);
            if (me.isFlying() && !canFly) {
                me.setFlying(false, false);
            } else if (me.isAutoFlying() && !me.isFlying() && canFly) {
                me.setFlying(true);
            }
        }

    }

    // For some reason onPlayerInteract() sometimes misses bucket events depending on distance (something like 2-3 blocks away isn't detected),
    // but these separate bucket events below always fire without fail
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Block block = event.getBlockClicked();
        Player player = event.getPlayer();

        if (!playerCanUseItemHere(player, block.getLocation(), event.getBucket(), false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        Block block = event.getBlockClicked();
        Player player = event.getPlayer();

        if (!playerCanUseItemHere(player, block.getLocation(), event.getBucket(), false)) {
            event.setCancelled(true);
        }
    }

    public static boolean preventCommand(String fullCmd, Player player) {
        if ((P.p.conf().factions().protection().getTerritoryNeutralDenyCommands().isEmpty() &&
                P.p.conf().factions().protection().getTerritoryEnemyDenyCommands().isEmpty() &&
                P.p.conf().factions().protection().getPermanentFactionMemberDenyCommands().isEmpty() &&
                P.p.conf().factions().protection().getWildernessDenyCommands().isEmpty() &&
                P.p.conf().factions().protection().getTerritoryAllyDenyCommands().isEmpty() &&
                P.p.conf().factions().protection().getWarzoneDenyCommands().isEmpty())) {
            return false;
        }

        fullCmd = fullCmd.toLowerCase();

        FPlayer me = FPlayers.getInstance().getByPlayer(player);

        String shortCmd;  // command without the slash at the beginning
        if (fullCmd.startsWith("/")) {
            shortCmd = fullCmd.substring(1);
        } else {
            shortCmd = fullCmd;
            fullCmd = "/" + fullCmd;
        }

        if (me.hasFaction() &&
                !me.isAdminBypassing() &&
                !P.p.conf().factions().protection().getPermanentFactionMemberDenyCommands().isEmpty() &&
                me.getFaction().isPermanent() &&
                isCommandInList(fullCmd, shortCmd, P.p.conf().factions().protection().getPermanentFactionMemberDenyCommands().iterator())) {
            me.msg(TL.PLAYER_COMMAND_PERMANENT, fullCmd);
            return true;
        }

        Faction at = Board.getInstance().getFactionAt(new FLocation(player.getLocation()));
        if (at.isWilderness() && !P.p.conf().factions().protection().getWildernessDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInList(fullCmd, shortCmd, P.p.conf().factions().protection().getWildernessDenyCommands().iterator())) {
            me.msg(TL.PLAYER_COMMAND_WILDERNESS, fullCmd);
            return true;
        }

        Relation rel = at.getRelationTo(me);
        if (at.isNormal() && rel.isAlly() && !P.p.conf().factions().protection().getTerritoryAllyDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInList(fullCmd, shortCmd, P.p.conf().factions().protection().getTerritoryAllyDenyCommands().iterator())) {
            me.msg(TL.PLAYER_COMMAND_ALLY, fullCmd);
            return false;
        }

        if (at.isNormal() && rel.isNeutral() && !P.p.conf().factions().protection().getTerritoryNeutralDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInList(fullCmd, shortCmd, P.p.conf().factions().protection().getTerritoryNeutralDenyCommands().iterator())) {
            me.msg(TL.PLAYER_COMMAND_NEUTRAL, fullCmd);
            return true;
        }

        if (at.isNormal() && rel.isEnemy() && !P.p.conf().factions().protection().getTerritoryEnemyDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInList(fullCmd, shortCmd, P.p.conf().factions().protection().getTerritoryEnemyDenyCommands().iterator())) {
            me.msg(TL.PLAYER_COMMAND_ENEMY, fullCmd);
            return true;
        }

        if (at.isWarZone() && !P.p.conf().factions().protection().getWarzoneDenyCommands().isEmpty() && !me.isAdminBypassing() && isCommandInList(fullCmd, shortCmd, P.p.conf().factions().protection().getWarzoneDenyCommands().iterator())) {
            me.msg(TL.PLAYER_COMMAND_WARZONE, fullCmd);
            return true;
        }

        return false;
    }

    private static boolean isCommandInList(String fullCmd, String shortCmd, Iterator<String> iter) {
        String cmdCheck;
        while (iter.hasNext()) {
            cmdCheck = iter.next();
            if (cmdCheck == null) {
                iter.remove();
                continue;
            }

            cmdCheck = cmdCheck.toLowerCase();
            if (fullCmd.startsWith(cmdCheck) || shortCmd.startsWith(cmdCheck)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractGUI(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof GUI) {
            event.setCancelled(true);
            GUI ui = (GUI) event.getClickedInventory().getHolder();
            ui.click(event.getRawSlot(), event.getClick());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMoveGUI(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof GUI) {
            event.setCancelled(true);
        }
    }


    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerKick(PlayerKickEvent event) {
        FPlayer badGuy = FPlayers.getInstance().getByPlayer(event.getPlayer());
        if (badGuy == null) {
            return;
        }

        // if player was banned (not just kicked), get rid of their stored info
        if (P.p.conf().factions().isRemovePlayerDataWhenBanned() && event.getReason().equals("Banned by admin.")) {
            if (badGuy.getRole() == Role.ADMIN) {
                badGuy.getFaction().promoteNewLeader();
            }

            badGuy.leave(false);
            badGuy.remove();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    final public void onFactionJoin(FPlayerJoinEvent event) {
        FTeamWrapper.applyUpdatesLater(event.getFaction());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFactionLeave(FPlayerLeaveEvent event) {
        FTeamWrapper.applyUpdatesLater(event.getFaction());
    }
}
