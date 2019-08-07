package com.massivecraft.factions.config.transition.oldclass;

import java.util.*;

import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class OldConf {
    public List<String> baseCommandAliases = new ArrayList<>();

    // Colors
    public ChatColor colorMember = ChatColor.GREEN;
    public ChatColor colorAlly = ChatColor.LIGHT_PURPLE;
    public ChatColor colorTruce = ChatColor.DARK_PURPLE;
    public ChatColor colorNeutral = ChatColor.WHITE;
    public ChatColor colorEnemy = ChatColor.RED;

    public ChatColor colorPeaceful = ChatColor.GOLD;
    public ChatColor colorWilderness = ChatColor.GRAY;
    public ChatColor colorSafezone = ChatColor.GOLD;
    public ChatColor colorWar = ChatColor.DARK_RED;

    // Power
    public double powerPlayerMax = 10.0;
    public double powerPlayerMin = -10.0;
    public double powerPlayerStarting = 0.0;
    public double powerPerMinute = 0.2; // Default health rate... it takes 5 min to heal one power
    public double powerPerDeath = 4.0; // A death makes you lose 4 power
    public boolean powerRegenOffline = false;  // does player power regenerate even while they're offline?
    public double powerOfflineLossPerDay = 0.0;  // players will lose this much power per day offline
    public double powerOfflineLossLimit = 0.0;  // players will no longer lose power from being offline once their power drops to this amount or less
    public double powerFactionMax = 0.0;  // if greater than 0, the cap on how much power a faction can have (additional power from players beyond that will act as a "buffer" of sorts)

    public String prefixAdmin = "***";
    public String prefixColeader = "**";
    public String prefixMod = "*";
    public String prefixNormal = "+";
    public String prefixRecruit = "-";

    public boolean allowMultipleColeaders = false;

    public int factionTagLengthMin = 3;
    public int factionTagLengthMax = 10;
    public boolean factionTagForceUpperCase = false;

    public boolean newFactionsDefaultOpen = false;

    // when faction membership hits this limit, players will no longer be able to join using /f join; default is 0, no limit
    public int factionMemberLimit = 0;

    // what faction ID to start new players in when they first join the server; default is 0, "no faction"
    public String newPlayerStartingFactionID = "0";

    public boolean showMapFactionKey = true;
    public boolean showNeutralFactionsOnMap = true;
    public boolean showEnemyFactionsOnMap = true;
    public boolean showTruceFactionsOnMap = true;

    // Disallow joining/leaving/kicking while power is negative
    public boolean canLeaveWithNegativePower = true;

    // Configuration for faction-only chat
    public boolean factionOnlyChat = true;
    // Configuration on the Faction tag in chat messages.
    public boolean chatTagEnabled = true;
    public transient boolean chatTagHandledByAnotherPlugin = false;
    public boolean chatTagRelationColored = true;
    public String chatTagReplaceString = "[FACTION]";
    public String chatTagInsertAfterString = "";
    public String chatTagInsertBeforeString = "";
    public int chatTagInsertIndex = 0;
    public boolean chatTagPadBefore = false;
    public boolean chatTagPadAfter = true;
    public String chatTagFormat = "%s" + ChatColor.WHITE;
    public boolean alwaysShowChatTag = true;
    public String factionChatFormat = "%s:" + ChatColor.WHITE + " %s";
    public String allianceChatFormat = ChatColor.LIGHT_PURPLE + "%s:" + ChatColor.WHITE + " %s";
    public String truceChatFormat = ChatColor.DARK_PURPLE + "%s:" + ChatColor.WHITE + " %s";
    public String modChatFormat = ChatColor.RED + "%s:" + ChatColor.WHITE + " %s";

    public boolean broadcastDescriptionChanges = false;
    public boolean broadcastTagChanges = false;

    public double saveToFileEveryXMinutes = 30.0;

    public double autoLeaveAfterDaysOfInactivity = 10.0;
    public double autoLeaveRoutineRunsEveryXMinutes = 5.0;
    public int autoLeaveRoutineMaxMillisecondsPerTick = 5;  // 1 server tick is roughly 50ms, so default max 10% of a tick
    public boolean removePlayerDataWhenBanned = true;
    public boolean autoLeaveDeleteFPlayerData = true; // Let them just remove player from Faction.

    public boolean worldGuardChecking = false;
    public boolean worldGuardBuildPriority = false;

    // server logging options
    public boolean logFactionCreate = true;
    public boolean logFactionDisband = true;
    public boolean logFactionJoin = true;
    public boolean logFactionKick = true;
    public boolean logFactionLeave = true;
    public boolean logLandClaims = true;
    public boolean logLandUnclaims = true;
    public boolean logMoneyTransactions = true;
    public boolean logPlayerCommands = true;

    // prevent some potential exploits
    public boolean handleExploitObsidianGenerators = true;
    public boolean handleExploitEnderPearlClipping = true;
    public boolean handleExploitInteractionSpam = true;
    public boolean handleExploitTNTWaterlog = false;
    public boolean handleExploitLiquidFlow = false;

    public boolean homesEnabled = true;
    public boolean homesMustBeInClaimedTerritory = true;
    public boolean homesTeleportToOnDeath = true;
    public boolean homesRespawnFromNoPowerLossWorlds = true;
    public boolean homesTeleportCommandEnabled = true;
    public boolean homesTeleportCommandEssentialsIntegration = true;
    public boolean homesTeleportCommandSmokeEffectEnabled = true;
    public float homesTeleportCommandSmokeEffectThickness = 3f;
    public boolean homesTeleportAllowedFromEnemyTerritory = true;
    public boolean homesTeleportAllowedFromDifferentWorld = true;
    public double homesTeleportAllowedEnemyDistance = 32.0;
    public boolean homesTeleportIgnoreEnemiesIfInOwnTerritory = true;

    public boolean disablePVPBetweenNeutralFactions = false;
    public boolean disablePVPForFactionlessPlayers = false;
    public boolean enablePVPAgainstFactionlessInAttackersLand = false;

    public int noPVPDamageToOthersForXSecondsAfterLogin = 3;

    public boolean peacefulTerritoryDisablePVP = true;
    public boolean peacefulTerritoryDisableMonsters = false;
    public boolean peacefulTerritoryDisableBoom = false;
    public boolean peacefulMembersDisablePowerLoss = true;

    public boolean permanentFactionsDisableLeaderPromotion = false;

    public boolean claimsMustBeConnected = false;
    public boolean claimsCanBeUnconnectedIfOwnedByOtherFaction = true;
    public int claimsRequireMinFactionMembers = 1;
    public int claimedLandsMax = 0;
    public int lineClaimLimit = 5;

    // if someone is doing a radius claim and the process fails to claim land this many times in a row, it will exit
    public int radiusClaimFailureLimit = 9;

    public double considerFactionsReallyOfflineAfterXMinutes = 0.0;

    public int actionDeniedPainAmount = 1;

    // commands which will be prevented if the player is a member of a permanent faction
    public Set<String> permanentFactionMemberDenyCommands = new LinkedHashSet<>();

    // commands which will be prevented when in claimed territory of another faction
    public Set<String> territoryNeutralDenyCommands = new LinkedHashSet<>();
    public Set<String> territoryEnemyDenyCommands = new LinkedHashSet<>();
    public Set<String> territoryAllyDenyCommands = new LinkedHashSet<>();
    public Set<String> warzoneDenyCommands = new LinkedHashSet<>();
    public Set<String> wildernessDenyCommands = new LinkedHashSet<>();

    // IGNORED STARTS HERE LOL
    public boolean defaultFlyPermEnemy = false;
    public boolean defaultFlyPermNeutral = false;
    public boolean defaultFlyPermTruce = false;
    public boolean defaultFlyPermAlly = true;
    public boolean defaultFlyPermMember = true;

    public boolean territoryDenyBuild = true;
    public boolean territoryDenyBuildWhenOffline = true;
    public boolean territoryPainBuild = false;
    public boolean territoryPainBuildWhenOffline = false;
    public boolean territoryDenyUseage = true;
    public boolean territoryEnemyDenyBuild = true;
    public boolean territoryEnemyDenyBuildWhenOffline = true;
    public boolean territoryEnemyPainBuild = false;
    public boolean territoryEnemyPainBuildWhenOffline = false;
    public boolean territoryEnemyDenyUseage = true;
    public boolean territoryEnemyProtectMaterials = true;
    public boolean territoryAllyDenyBuild = true;
    public boolean territoryAllyDenyBuildWhenOffline = true;
    public boolean territoryAllyPainBuild = false;
    public boolean territoryAllyPainBuildWhenOffline = false;
    public boolean territoryAllyDenyUseage = true;
    public boolean territoryAllyProtectMaterials = true;
    public boolean territoryTruceDenyBuild = true;
    public boolean territoryTruceDenyBuildWhenOffline = true;
    public boolean territoryTrucePainBuild = false;
    public boolean territoryTrucePainBuildWhenOffline = false;
    public boolean territoryTruceDenyUseage = true;
    public boolean territoryTruceProtectMaterials = true;
    // IGNORED ENDS HERE LOL
    public boolean territoryBlockCreepers = false;
    public boolean territoryBlockCreepersWhenOffline = false;
    public boolean territoryBlockFireballs = false;
    public boolean territoryBlockFireballsWhenOffline = false;
    public boolean territoryBlockTNT = false;
    public boolean territoryBlockTNTWhenOffline = false;
    public boolean territoryDenyEndermanBlocks = true;
    public boolean territoryDenyEndermanBlocksWhenOffline = true;

    public boolean safeZoneDenyBuild = true;
    public boolean safeZoneDenyUseage = true;
    public boolean safeZoneBlockTNT = true;
    public boolean safeZonePreventAllDamageToPlayers = false;
    public boolean safeZoneDenyEndermanBlocks = true;

    public boolean warZoneDenyBuild = true;
    public boolean warZoneDenyUseage = true;
    public boolean warZoneBlockCreepers = false;
    public boolean warZoneBlockFireballs = false;
    public boolean warZoneBlockTNT = true;
    public boolean warZonePowerLoss = true;
    public boolean warZoneFriendlyFire = false;
    public boolean warZoneDenyEndermanBlocks = true;

    public boolean wildernessDenyBuild = false;
    public boolean wildernessDenyUseage = false;
    public boolean wildernessBlockCreepers = false;
    public boolean wildernessBlockFireballs = false;
    public boolean wildernessBlockTNT = false;
    public boolean wildernessPowerLoss = true;
    public boolean wildernessDenyEndermanBlocks = false;

    // for claimed areas where further faction-member ownership can be defined
    public boolean ownedAreasEnabled = true;
    public int ownedAreasLimitPerFaction = 0;
    public boolean ownedAreasModeratorsCanSet = false;
    public boolean ownedAreaModeratorsBypass = true;
    public boolean ownedAreaDenyBuild = true;
    public boolean ownedAreaPainBuild = false;
    public boolean ownedAreaProtectMaterials = true;
    public boolean ownedAreaDenyUseage = true;

    public boolean ownedMessageOnBorder = true;
    public boolean ownedMessageInsideTerritory = true;
    public boolean ownedMessageByChunk = false;

    public boolean pistonProtectionThroughDenyBuild = true;

    public Set<Material> territoryProtectedMaterials = Sets.newHashSet();
    public Set<Material> territoryDenyUseageMaterials = EnumSet.noneOf(Material.class);
    public Set<Material> territoryProtectedMaterialsWhenOffline = EnumSet.noneOf(Material.class);
    public Set<Material> territoryDenyUseageMaterialsWhenOffline = EnumSet.noneOf(Material.class);

    // Economy settings
    public boolean econEnabled = false;
    public String econUniverseAccount = "";
    public double econCostClaimWilderness = 30.0;
    public double econCostClaimFromFactionBonus = 30.0;
    public double econOverclaimRewardMultiplier = 0.0;
    public double econClaimAdditionalMultiplier = 0.5;
    public double econClaimRefundMultiplier = 0.7;
    public double econClaimUnconnectedFee = 0.0;
    public double econCostCreate = 100.0;
    public double econCostOwner = 15.0;
    public double econCostSethome = 30.0;
    public double econCostJoin = 0.0;
    public double econCostLeave = 0.0;
    public double econCostKick = 0.0;
    public double econCostInvite = 0.0;
    public double econCostHome = 0.0;
    public double econCostTag = 0.0;
    public double econCostDesc = 0.0;
    public double econCostTitle = 0.0;
    public double econCostList = 0.0;
    public double econCostMap = 0.0;
    public double econCostPower = 0.0;
    public double econCostShow = 0.0;
    public double econCostStuck = 0.0;
    public double econCostOpen = 0.0;
    public double econCostAlly = 0.0;
    public double econCostTruce = 0.0;
    public double econCostEnemy = 0.0;
    public double econCostNeutral = 0.0;
    public double econCostNoBoom = 0.0;


    // -------------------------------------------- //
    // INTEGRATION: DYNMAP
    // -------------------------------------------- //

    // Should the dynmap intagration be used?
    public boolean dynmapUse = false;

    // Name of the Factions layer
    public String dynmapLayerName = "Factions";

    // Should the layer be visible per default
    public boolean dynmapLayerVisible = true;

    // Ordering priority in layer menu (low goes before high - default is 0)
    public int dynmapLayerPriority = 2;

    // (optional) set minimum zoom level before layer is visible (0 = default, always visible)
    public int dynmapLayerMinimumZoom = 0;

    // Format for popup - substitute values for macros
    public String dynmapDescription =
            "<div class=\"infowindow\">\n"
                    + "<span style=\"font-weight: bold; font-size: 150%;\">%name%</span><br>\n"
                    + "<span style=\"font-style: italic; font-size: 110%;\">%description%</span><br>"
                    + "<br>\n"
                    + "<span style=\"font-weight: bold;\">Leader:</span> %players.leader%<br>\n"
                    + "<span style=\"font-weight: bold;\">Admins:</span> %players.admins.count%<br>\n"
                    + "<span style=\"font-weight: bold;\">Moderators:</span> %players.moderators.count%<br>\n"
                    + "<span style=\"font-weight: bold;\">Members:</span> %players.normals.count%<br>\n"
                    + "<span style=\"font-weight: bold;\">TOTAL:</span> %players.count%<br>\n"
                    + "</br>\n"
                    + "<span style=\"font-weight: bold;\">Bank:</span> %money%<br>\n"
                    + "<br>\n"
                    + "</div>";

    // Enable the %money% macro. Only do this if you know your economy manager is thread-safe.
    public boolean dynmapDescriptionMoney = false;

    // Allow players in faction to see one another on Dynmap (only relevant if Dynmap has 'player-info-protected' enabled)
    public boolean dynmapVisibilityByFaction = true;

    // Optional setting to limit which regions to show.
    // If empty all regions are shown.
    // Specify Faction either by name or UUID.
    // To show all regions on a given world, add 'world:<worldname>' to the list.
    public Set<String> dynmapVisibleFactions = new HashSet<>();

    // Optional setting to hide specific Factions.
    // Specify Faction either by name or UUID.
    // To hide all regions on a given world, add 'world:<worldname>' to the list.
    public Set<String> dynmapHiddenFactions = new HashSet<>();

    // Region Style
    public final transient String DYNMAP_STYLE_LINE_COLOR = "#00FF00";
    public final transient double DYNMAP_STYLE_LINE_OPACITY = 0.8D;
    public final transient int DYNMAP_STYLE_LINE_WEIGHT = 3;
    public final transient String DYNMAP_STYLE_FILL_COLOR = "#00FF00";
    public final transient double DYNMAP_STYLE_FILL_OPACITY = 0.35D;
    public final transient String DYNMAP_STYLE_HOME_MARKER = "greenflag";
    public final transient boolean DYNMAP_STYLE_BOOST = false;

    /*public DynmapStyle dynmapDefaultStyle = new DynmapStyle()
            .setStrokeColor(DYNMAP_STYLE_LINE_COLOR)
            .setLineOpacity(DYNMAP_STYLE_LINE_OPACITY)
            .setLineWeight(DYNMAP_STYLE_LINE_WEIGHT)
            .setFillColor(DYNMAP_STYLE_FILL_COLOR)
            .setFillOpacity(DYNMAP_STYLE_FILL_OPACITY)
            .setHomeMarker(DYNMAP_STYLE_HOME_MARKER)
            .setBoost(DYNMAP_STYLE_BOOST);

    // Optional per Faction style overrides. Any defined replace those in dynmapDefaultStyle.
    // Specify Faction either by name or UUID.
    public Map<String, DynmapStyle> dynmapFactionStyles = ImmutableMap.of(
            "SafeZone", new DynmapStyle().setStrokeColor("#FF00FF").setFillColor("#FF00FF").setBoost(false),
            "WarZone", new DynmapStyle().setStrokeColor("#FF0000").setFillColor("#FF0000").setBoost(false)
    );
*/

    //Faction banks, to pay for land claiming and other costs instead of individuals paying for them
    public boolean bankEnabled = true;
    public boolean bankMembersCanWithdraw = false; //Have to be at least moderator to withdraw or pay money to another faction
    public boolean bankFactionPaysCosts = true; //The faction pays for faction command costs, such as sethome
    public boolean bankFactionPaysLandCosts = true; //The faction pays for land claiming costs.

    // mainly for other plugins/mods that use a fake player to take actions, which shouldn't be subject to our protections
    public Set<String> playersWhoBypassAllProtection = new LinkedHashSet<>();

    public Set<String> worldsNoClaiming = new LinkedHashSet<>();
    public Set<String> worldsNoPowerLoss = new LinkedHashSet<>();
    public Set<String> worldsIgnorePvP = new LinkedHashSet<>();
    public Set<String> worldsNoWildernessProtection = new LinkedHashSet<>();

    // faction-<factionId>
    public String vaultPrefix = "faction-%s";
    public int defaultMaxVaults = 0;

    // Taller and wider for "bigger f map"
    public int mapHeight = 17;
    public int mapWidth = 49;
}
