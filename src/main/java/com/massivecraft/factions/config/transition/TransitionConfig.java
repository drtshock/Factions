package com.massivecraft.factions.config.transition;

import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.config.transition.oldclass.OldConf;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TransitionConfig {
    public class Colors {
        public class Relations {
            private String member = "GREEN";
            private String ally = "LIGHT_PURPLE";
            private String truce = "DARK_PURPLE";
            private String neutral = "WHITE";
            private String enemy = "RED";
            private String peaceful = "GOLD";
        }

        public class Factions {
            private String wilderness = "GRAY";
            private String safezone = "GOLD";
            private String warzone = "DARK_RED";
        }

        private Factions factions = new Factions();
        private Relations relations = new Relations();
    }

    public class Factions {
        public class LandRaidControl {
            public class Power {
                private double playerMin = -10.0D;
                private double playerMax = 10.0D;
                private double playerStarting = 0.0D;
                @Comment("Default health rate of 0.2 takes 5 minutes to recover one power")
                private double powerPerMinute = 0.2;
                @Comment("How much is lost on death")
                private double lossPerDeath = 4.0;
                @Comment("Does a player regenerate power while offline?")
                private boolean regenOffline = false;
                @Comment("A player loses this much per day offline")
                private double offlineLossPerDay = 0.0;
                @Comment("A player stops losing power from being offline once they reach this amount")
                private double offlineLossLimit = 0.0;
                @Comment("If greater than 0, used as a cap for how much power a faction can have\nAdditional power from players beyond this acts as a \"buffer\" of sorts")
                private double factionMax = 0.0;
                private boolean respawnHomeFromNoPowerLossWorlds = true;
                private Set<String> worldsNoPowerLoss = new HashSet<>();
                private boolean peacefulMembersDisablePowerLoss = true;
                private boolean warZonePowerLoss = true;
                private boolean wildernessPowerLoss = true;
                @Comment("Disallow joining/leaving/kicking while power is negative")
                private boolean canLeaveWithNegativePower = true;
            }

            @Comment("Sets the mode of land/raid control")
            private String system = "power";
            @Comment("Controls the power system of land/raid control\nSet the 'system' value to 'power' to use this system")
            private Power power = new Power();
        }

        public class Prefix {
            private String admin = "***";
            private String coleader = "**";
            private String mod = "*";
            private String normal = "+";
            private String recruit = "-";
        }

        public class Chat {
            @Comment("Allow for players to chat only within their faction, with allies, etc.\n" +
                    "Set to false to only allow public chats through this plugin.")
            private boolean factionOnlyChat = true;
            // Configuration on the Faction tag in chat messages.
            @Comment("If true, disables adding of faction tag so another plugin can manage this")
            private transient boolean tagHandledByAnotherPlugin = false;
            private boolean tagRelationColored = true;
            private String tagReplaceString = "[FACTION]";
            private String tagInsertAfterString = "";
            private String tagInsertBeforeString = "";
            private int tagInsertIndex = 0;
            private boolean tagPadBefore = false;
            private boolean tagPadAfter = true;
            private String tagFormat = "%s\u00A7f";
            private boolean alwaysShowChatTag = true;
            private String factionChatFormat = "%s:\u00A7f %s";
            private String allianceChatFormat = "\u00A7d%s:\u00A7f %s";
            private String truceChatFormat = "\u00A75%s:\u00A7f %s";
            private String modChatFormat = "\u00A7c%s:\u00A7f %s";
            private boolean broadcastDescriptionChanges = false;
            private boolean broadcastTagChanges = false;
        }

        public class Homes {
            private boolean enabled = true;
            private boolean mustBeInClaimedTerritory = true;
            private boolean teleportToOnDeath = true;
            private boolean teleportCommandEnabled = true;
            private boolean teleportCommandEssentialsIntegration = true;
            private boolean teleportCommandSmokeEffectEnabled = true;
            private float teleportCommandSmokeEffectThickness = 3f;
            private boolean teleportAllowedFromEnemyTerritory = true;
            private boolean teleportAllowedFromDifferentWorld = true;
            private double teleportAllowedEnemyDistance = 32.0;
            private boolean teleportIgnoreEnemiesIfInOwnTerritory = true;
        }

        public class PVP {
            private boolean disablePVPBetweenNeutralFactions = false;
            private boolean disablePVPForFactionlessPlayers = false;
            private boolean enablePVPAgainstFactionlessInAttackersLand = false;
            private int noPVPDamageToOthersForXSecondsAfterLogin = 3;
            private Set<String> worldsIgnorePvP = new HashSet<>();
        }

        public class SpecialCase {
            private boolean peacefulTerritoryDisablePVP = true;
            private boolean peacefulTerritoryDisableMonsters = false;
            private boolean peacefulTerritoryDisableBoom = false;
            private boolean permanentFactionsDisableLeaderPromotion = false;
        }

        public class Claims {
            private boolean mustBeConnected = false;
            private boolean canBeUnconnectedIfOwnedByOtherFaction = true;
            private int requireMinFactionMembers = 1;
            private int landsMax = 0;
            private int lineClaimLimit = 5;
            @Comment("If someone is doing a radius claim and the process fails to claim land this many times in a row, it will exit")
            private int radiusClaimFailureLimit = 9;
            private Set<String> worldsNoClaiming = new HashSet<>();
        }

        public class Protection {
            @Comment("Commands which will be prevented if the player is a member of a permanent faction")
            private Set<String> permanentFactionMemberDenyCommands = new HashSet<>();

            @Comment("Commands which will be prevented when in claimed territory of a neutral faction")
            private Set<String> territoryNeutralDenyCommands = new HashSet<>();
            @Comment("Commands which will be prevented when in claimed territory of an enemy faction")
            private Set<String> territoryEnemyDenyCommands = new HashSet<String>() {
                {
                    this.add("home");
                    this.add("sethome");
                    this.add("spawn");
                    this.add("tpahere");
                    this.add("tpaccept");
                    this.add("tpa");
                }
            };
            @Comment("Commands which will be prevented when in claimed territory of an ally faction")
            private Set<String> territoryAllyDenyCommands = new HashSet<>();
            @Comment("Commands which will be prevented when in warzone")
            private Set<String> warzoneDenyCommands = new HashSet<>();
            @Comment("Commands which will be prevented when in wilderness")
            private Set<String> wildernessDenyCommands = new HashSet<>();

            private boolean territoryBlockCreepers = false;
            private boolean territoryBlockCreepersWhenOffline = false;
            private boolean territoryBlockFireballs = false;
            private boolean territoryBlockFireballsWhenOffline = false;
            private boolean territoryBlockTNT = false;
            private boolean territoryBlockTNTWhenOffline = false;
            private boolean territoryDenyEndermanBlocks = true;
            private boolean territoryDenyEndermanBlocksWhenOffline = true;

            private boolean safeZoneDenyBuild = true;
            private boolean safeZoneDenyUsage = true;
            private boolean safeZoneBlockTNT = true;
            private boolean safeZonePreventAllDamageToPlayers = false;
            private boolean safeZoneDenyEndermanBlocks = true;

            private boolean warZoneDenyBuild = true;
            private boolean warZoneDenyUsage = true;
            private boolean warZoneBlockCreepers = false;
            private boolean warZoneBlockFireballs = false;
            private boolean warZoneBlockTNT = true;
            private boolean warZoneFriendlyFire = false;
            private boolean warZoneDenyEndermanBlocks = true;

            private boolean wildernessDenyBuild = false;
            private boolean wildernessDenyUsage = false;
            private boolean wildernessBlockCreepers = false;
            private boolean wildernessBlockFireballs = false;
            private boolean wildernessBlockTNT = false;
            private boolean wildernessDenyEndermanBlocks = false;

            private boolean pistonProtectionThroughDenyBuild = true;

            private Set<String> territoryProtectedMaterials = new HashSet<>();
            private Set<String> territoryDenyUsageMaterials = new HashSet<>();
            private Set<String> territoryProtectedMaterialsWhenOffline = new HashSet<>();
            private Set<String> territoryDenyUsageMaterialsWhenOffline = new HashSet<>();

            @Comment("Mainly for other plugins/mods that use a fake player to take actions, which shouldn't be subject to our protections")
            private Set<String> playersWhoBypassAllProtection = new HashSet<>();
            private Set<String> worldsNoWildernessProtection = new HashSet<>();

            private Protection() {
                protectMaterial("DARK_OAK_DOOR");
                protectMaterial("BIRCH_DOOR");
                protectMaterial("ACACIA_DOOR");
                protectMaterial("IRON_DOOR");
                protectMaterial("JUNGLE_DOOR");
                protectMaterial("OAK_DOOR");
                protectMaterial("SPRUCE_DOOR");

                protectMaterial("ACACIA_TRAPDOOR");
                protectMaterial("BIRCH_TRAPDOOR");
                protectMaterial("DARK_OAK_TRAPDOOR");
                protectMaterial("IRON_TRAPDOOR");
                protectMaterial("JUNGLE_TRAPDOOR");
                protectMaterial("OAK_TRAPDOOR");
                protectMaterial("SPRUCE_TRAPDOOR");

                protectMaterial("ACACIA_FENCE");
                protectMaterial("BIRCH_FENCE");
                protectMaterial("DARK_OAK_FENCE");
                protectMaterial("OAK_FENCE");
                protectMaterial("NETHER_BRICK_FENCE");
                protectMaterial("SPRUCE_FENCE");

                protectMaterial("OAK_FENCE_GATE");
                protectMaterial("SPRUCE_FENCE_GATE");
                protectMaterial("BIRCH_FENCE_GATE");
                protectMaterial("JUNGLE_FENCE_GATE");
                protectMaterial("ACACIA_FENCE_GATE");
                protectMaterial("DARK_OAK_FENCE_GATE");

                protectMaterial("DISPENSER");
                protectMaterial("CHEST");
                protectMaterial("FURNACE");
                protectMaterial("REPEATER");
                protectMaterial("JUKEBOX");
                protectMaterial("BREWING_STAND");
                protectMaterial("ENCHANTING_TABLE");
                protectMaterial("CAULDRON");
                protectMaterial("FARMLAND");
                protectMaterial("BEACON");
                protectMaterial("ANVIL");
                protectMaterial("TRAPPED_CHEST");
                protectMaterial("DROPPER");
                protectMaterial("HOPPER");

                protectUsage("FIRE_CHARGE");
                protectUsage("FLINT_AND_STEEL");
                protectUsage("BUCKET");
                protectUsage("WATER_BUCKET");
                protectUsage("LAVA_BUCKET");
            }

            private void protectMaterial(String material) {
                this.territoryProtectedMaterials.add(material);
                territoryProtectedMaterialsWhenOffline.add(material);
            }

            private void protectUsage(String material) {
                territoryDenyUsageMaterials.add(material);
                territoryDenyUsageMaterialsWhenOffline.add(material);
            }
        }

        public class OwnedArea {
            private boolean enabled = true;
            private int limitPerFaction = 0;
            private boolean moderatorsCanSet = false;
            private boolean moderatorsBypass = true;
            private boolean denyBuild = true;
            private boolean painBuild = false;
            private boolean protectMaterials = true;
            private boolean denyUsage = true;

            private boolean messageOnBorder = true;
            private boolean messageInsideTerritory = true;
            private boolean messageByChunk = false;
        }

        private Chat chat = new Chat();
        private Homes homes = new Homes();
        private PVP pvp = new PVP();
        private SpecialCase specialCase = new SpecialCase();
        private Claims claims = new Claims();
        private Protection protection = new Protection();
        @Comment("For claimed areas where further faction-member ownership can be defined")
        private OwnedArea ownedArea = new OwnedArea();
        @Comment("Displayed prefixes for different roles within a faction")
        private Prefix prefixes = new Prefix();
        private LandRaidControl landRaidControl = new LandRaidControl();

        private boolean allowMultipleColeaders = false;

        @Comment("Minimum faction tag length")
        private int tagLengthMin = 3;
        @Comment("Maximum faction tag length")
        private int tagLengthMax = 10;
        private boolean tagForceUpperCase = false;

        private boolean newFactionsDefaultOpen = false;

        @Comment("When faction membership hits this limit, players will no longer be able to join using /f join; default is 0, no limit")
        private int factionMemberLimit = 0;

        @Comment("What faction ID to start new players in when they first join the server; default is 0, \"no faction\"")
        private String newPlayerStartingFactionID = "0";

        private double saveToFileEveryXMinutes = 30.0;

        private double autoLeaveAfterDaysOfInactivity = 10.0;
        private double autoLeaveRoutineRunsEveryXMinutes = 5.0;
        private int autoLeaveRoutineMaxMillisecondsPerTick = 5;  // 1 server tick is roughly 50ms, so default max 10% of a tick
        private boolean removePlayerDataWhenBanned = true;
        private boolean autoLeaveDeleteFPlayerData = true; // Let them just remove player from Faction.
        private double considerFactionsReallyOfflineAfterXMinutes = 0.0;
        private int actionDeniedPainAmount = 1;

        @Comment("If enabled, perms can be managed separately for when the faction is offline")
        private boolean separateOfflinePerms = false;
    }

    public class Logging {
        private boolean factionCreate = true;
        private boolean factionDisband = true;
        private boolean factionJoin = true;
        private boolean factionKick = true;
        private boolean factionLeave = true;
        private boolean landClaims = true;
        private boolean landUnclaims = true;
        private boolean moneyTransactions = true;
        private boolean playerCommands = true;
    }

    public class Exploits {
        private boolean obsidianGenerators = true;
        private boolean enderPearlClipping = true;
        private boolean interactionSpam = true;
        private boolean tntWaterlog = false;
        private boolean liquidFlow = false;
    }

    public class Economy {
        private boolean enabled = false;
        private String universeAccount = "";
        private double costClaimWilderness = 30.0;
        private double costClaimFromFactionBonus = 30.0;
        private double overclaimRewardMultiplier = 0.0;
        private double claimAdditionalMultiplier = 0.5;
        private double claimRefundMultiplier = 0.7;
        private double claimUnconnectedFee = 0.0;
        private double costCreate = 100.0;
        private double costOwner = 15.0;
        private double costSethome = 30.0;
        private double costJoin = 0.0;
        private double costLeave = 0.0;
        private double costKick = 0.0;
        private double costInvite = 0.0;
        private double costHome = 0.0;
        private double costTag = 0.0;
        private double costDesc = 0.0;
        private double costTitle = 0.0;
        private double costList = 0.0;
        private double costMap = 0.0;
        private double costPower = 0.0;
        private double costShow = 0.0;
        private double costStuck = 0.0;
        private double costOpen = 0.0;
        private double costAlly = 0.0;
        private double costTruce = 0.0;
        private double costEnemy = 0.0;
        private double costNeutral = 0.0;
        private double costNoBoom = 0.0;

        @Comment("Faction banks, to pay for land claiming and other costs instead of individuals paying for them")
        private boolean bankEnabled = true;
        @Comment("Have to be at least moderator to withdraw or pay money to another faction")
        private boolean bankMembersCanWithdraw = false;
        @Comment("The faction pays for faction command costs, such as sethome")
        private boolean bankFactionPaysCosts = true;
        @Comment("The faction pays for land claiming costs.")
        private boolean bankFactionPaysLandCosts = true;
    }

    public class Map {
        private int height = 17;
        private int width = 49;
        private boolean showFactionKey = true;
        private boolean showNeutralFactions = true;
        private boolean showEnemyFactions = true;
        private boolean showTruceFactions = true;
    }

    public class PlayerVaults {
        @Comment("The %s is for the faction id")
        private String vaultPrefix = "faction-%s";
        private int defaultMaxVaults = 0;
    }

    public class WorldGuard {
        private boolean checking;
        private boolean buildPriority;
    }

    @Comment("The command base (by default f, making the command /f)")
    private List<String> commandBase;

    @Comment("Colors for relationships and default factions")
    private Colors colors = new Colors();

    private Factions factions = new Factions();
    @Comment("What should be logged?")
    private Logging logging = new Logging();
    @Comment("Controls certain exploit preventions")
    private Exploits exploits = new Exploits();
    @Comment("Economy support requires Vault and a compatible economy plugin")
    private Economy economy = new Economy();
    @Comment("Control for the default settings of /f map")
    private Map map = new Map();
    @Comment("PlayerVaults faction vault settings")
    private PlayerVaults playerVaults = new PlayerVaults();
    @Comment("WorldGuard settings")
    private WorldGuard worldGuard = new WorldGuard();

    public TransitionConfig(OldConf c) {
        this.commandBase = new ArrayList<>(c.baseCommandAliases);

        this.worldGuard.buildPriority = c.worldGuardBuildPriority;
        this.worldGuard.checking = c.worldGuardChecking;

        this.playerVaults.defaultMaxVaults = c.defaultMaxVaults;
        this.playerVaults.vaultPrefix = c.vaultPrefix;

        this.colors.relations.member = c.colorMember.name();
        this.colors.relations.ally = c.colorAlly.name();
        this.colors.relations.truce = c.colorTruce.name();
        this.colors.relations.neutral = c.colorNeutral.name();
        this.colors.relations.enemy = c.colorEnemy.name();
        this.colors.relations.peaceful = c.colorPeaceful.name();
        this.colors.factions.wilderness = c.colorWilderness.name();
        this.colors.factions.safezone = c.colorSafezone.name();
        this.colors.factions.warzone = c.colorWar.name();

        this.factions.landRaidControl.power.playerMin = c.powerPlayerMin;
        this.factions.landRaidControl.power.playerMax = c.powerPlayerMax;
        this.factions.landRaidControl.power.playerStarting = c.powerPlayerStarting;
        this.factions.landRaidControl.power.powerPerMinute = c.powerPerMinute;
        this.factions.landRaidControl.power.lossPerDeath = c.powerPerDeath;
        this.factions.landRaidControl.power.regenOffline = c.powerRegenOffline;
        this.factions.landRaidControl.power.offlineLossPerDay = c.powerOfflineLossPerDay;
        this.factions.landRaidControl.power.offlineLossLimit = c.powerOfflineLossLimit;
        this.factions.landRaidControl.power.factionMax = c.powerFactionMax;
        this.factions.landRaidControl.power.respawnHomeFromNoPowerLossWorlds = c.homesRespawnFromNoPowerLossWorlds;
        this.factions.landRaidControl.power.worldsNoPowerLoss = c.worldsNoPowerLoss;
        this.factions.landRaidControl.power.peacefulMembersDisablePowerLoss = c.peacefulMembersDisablePowerLoss;
        this.factions.landRaidControl.power.warZonePowerLoss = c.warZonePowerLoss;
        this.factions.landRaidControl.power.wildernessPowerLoss = c.wildernessPowerLoss;
        this.factions.landRaidControl.power.canLeaveWithNegativePower = c.canLeaveWithNegativePower;

        this.factions.prefixes.admin = c.prefixAdmin;
        this.factions.prefixes.coleader = c.prefixColeader;
        this.factions.prefixes.mod = c.prefixMod;
        this.factions.prefixes.normal = c.prefixNormal;
        this.factions.prefixes.recruit = c.prefixRecruit;

        this.factions.chat.factionOnlyChat = c.factionOnlyChat;
        this.factions.chat.tagHandledByAnotherPlugin = c.chatTagHandledByAnotherPlugin || c.chatTagEnabled;
        this.factions.chat.tagRelationColored = c.chatTagRelationColored;
        this.factions.chat.tagReplaceString = c.chatTagReplaceString;
        this.factions.chat.tagInsertAfterString = c.chatTagInsertAfterString;
        this.factions.chat.tagInsertBeforeString = c.chatTagInsertBeforeString;
        this.factions.chat.tagInsertIndex = c.chatTagInsertIndex;
        this.factions.chat.tagPadBefore = c.chatTagPadBefore;
        this.factions.chat.tagPadAfter = c.chatTagPadAfter;
        this.factions.chat.tagFormat = c.chatTagFormat;
        this.factions.chat.alwaysShowChatTag = c.alwaysShowChatTag;
        this.factions.chat.factionChatFormat = c.factionChatFormat;
        this.factions.chat.allianceChatFormat = c.allianceChatFormat;
        this.factions.chat.truceChatFormat = c.truceChatFormat;
        this.factions.chat.modChatFormat = c.modChatFormat;
        this.factions.chat.broadcastDescriptionChanges = c.broadcastDescriptionChanges;
        this.factions.chat.broadcastTagChanges = c.broadcastTagChanges;

        this.factions.homes.enabled = c.homesEnabled;
        this.factions.homes.mustBeInClaimedTerritory = c.homesMustBeInClaimedTerritory;
        this.factions.homes.teleportToOnDeath = c.homesTeleportToOnDeath;
        this.factions.homes.teleportCommandEnabled = c.homesTeleportCommandEnabled;
        this.factions.homes.teleportCommandEssentialsIntegration = c.homesTeleportCommandEssentialsIntegration;
        this.factions.homes.teleportCommandSmokeEffectEnabled = c.homesTeleportCommandSmokeEffectEnabled;
        this.factions.homes.teleportCommandSmokeEffectThickness = c.homesTeleportCommandSmokeEffectThickness;
        this.factions.homes.teleportAllowedFromEnemyTerritory = c.homesTeleportAllowedFromEnemyTerritory;
        this.factions.homes.teleportAllowedFromDifferentWorld = c.homesTeleportAllowedFromDifferentWorld;
        this.factions.homes.teleportAllowedEnemyDistance = c.homesTeleportAllowedEnemyDistance;
        this.factions.homes.teleportIgnoreEnemiesIfInOwnTerritory = c.homesTeleportIgnoreEnemiesIfInOwnTerritory;

        this.factions.pvp.disablePVPBetweenNeutralFactions = c.disablePVPBetweenNeutralFactions;
        this.factions.pvp.disablePVPForFactionlessPlayers = c.disablePVPForFactionlessPlayers;
        this.factions.pvp.enablePVPAgainstFactionlessInAttackersLand = c.enablePVPAgainstFactionlessInAttackersLand;
        this.factions.pvp.noPVPDamageToOthersForXSecondsAfterLogin = c.noPVPDamageToOthersForXSecondsAfterLogin;
        this.factions.pvp.worldsIgnorePvP = c.worldsIgnorePvP;

        this.factions.specialCase.peacefulTerritoryDisablePVP = c.peacefulTerritoryDisablePVP;
        this.factions.specialCase.peacefulTerritoryDisableMonsters = c.peacefulTerritoryDisableMonsters;
        this.factions.specialCase.peacefulTerritoryDisableBoom = c.peacefulTerritoryDisableBoom;
        this.factions.specialCase.permanentFactionsDisableLeaderPromotion = c.permanentFactionsDisableLeaderPromotion;

        this.factions.claims.mustBeConnected = c.claimsMustBeConnected;
        this.factions.claims.canBeUnconnectedIfOwnedByOtherFaction = c.claimsCanBeUnconnectedIfOwnedByOtherFaction;
        this.factions.claims.requireMinFactionMembers = c.claimsRequireMinFactionMembers;
        this.factions.claims.landsMax = c.claimedLandsMax;
        this.factions.claims.lineClaimLimit = c.lineClaimLimit;
        this.factions.claims.radiusClaimFailureLimit = c.radiusClaimFailureLimit;
        this.factions.claims.worldsNoClaiming = c.worldsNoClaiming;

        this.factions.protection.permanentFactionMemberDenyCommands = c.permanentFactionMemberDenyCommands;
        this.factions.protection.territoryNeutralDenyCommands = c.territoryNeutralDenyCommands;
        this.factions.protection.territoryEnemyDenyCommands = c.territoryEnemyDenyCommands;
        this.factions.protection.territoryAllyDenyCommands = c.territoryAllyDenyCommands;
        this.factions.protection.warzoneDenyCommands = c.warzoneDenyCommands;
        this.factions.protection.wildernessDenyCommands = c.wildernessDenyCommands;
        this.factions.protection.territoryBlockCreepers = c.territoryBlockCreepers;
        this.factions.protection.territoryBlockCreepersWhenOffline = c.territoryBlockCreepersWhenOffline;
        this.factions.protection.territoryBlockFireballs = c.territoryBlockFireballs;
        this.factions.protection.territoryBlockFireballsWhenOffline = c.territoryBlockFireballsWhenOffline;
        this.factions.protection.territoryBlockTNT = c.territoryBlockTNT;
        this.factions.protection.territoryBlockTNTWhenOffline = c.territoryBlockTNTWhenOffline;
        this.factions.protection.territoryDenyEndermanBlocks = c.territoryDenyEndermanBlocks;
        this.factions.protection.territoryDenyEndermanBlocksWhenOffline = c.territoryDenyEndermanBlocksWhenOffline;
        this.factions.protection.safeZoneDenyBuild = c.safeZoneDenyBuild;
        this.factions.protection.safeZoneDenyUsage = c.safeZoneDenyUseage;
        this.factions.protection.safeZoneBlockTNT = c.safeZoneBlockTNT;
        this.factions.protection.safeZonePreventAllDamageToPlayers = c.safeZonePreventAllDamageToPlayers;
        this.factions.protection.safeZoneDenyEndermanBlocks = c.safeZoneDenyEndermanBlocks;
        this.factions.protection.warZoneDenyBuild = c.warZoneDenyBuild;
        this.factions.protection.warZoneDenyUsage = c.warZoneDenyUseage;
        this.factions.protection.warZoneBlockCreepers = c.warZoneBlockCreepers;
        this.factions.protection.warZoneBlockFireballs = c.warZoneBlockFireballs;
        this.factions.protection.warZoneBlockTNT = c.warZoneBlockTNT;
        this.factions.protection.warZoneFriendlyFire = c.warZoneFriendlyFire;
        this.factions.protection.warZoneDenyEndermanBlocks = c.warZoneDenyEndermanBlocks;
        this.factions.protection.wildernessDenyBuild = c.wildernessDenyBuild;
        this.factions.protection.wildernessDenyUsage = c.wildernessDenyUseage;
        this.factions.protection.wildernessBlockCreepers = c.wildernessBlockCreepers;
        this.factions.protection.wildernessBlockFireballs = c.wildernessBlockFireballs;
        this.factions.protection.wildernessBlockTNT = c.wildernessBlockTNT;
        this.factions.protection.wildernessDenyEndermanBlocks = c.wildernessDenyEndermanBlocks;
        this.factions.protection.pistonProtectionThroughDenyBuild = c.pistonProtectionThroughDenyBuild;
        this.factions.protection.territoryProtectedMaterials = c.territoryProtectedMaterials.stream().filter(Objects::nonNull).map(Material::name).collect(Collectors.toSet());
        this.factions.protection.territoryDenyUsageMaterials = c.territoryDenyUseageMaterials.stream().filter(Objects::nonNull).map(Material::name).collect(Collectors.toSet());
        this.factions.protection.territoryProtectedMaterialsWhenOffline = c.territoryProtectedMaterialsWhenOffline.stream().filter(Objects::nonNull).map(Material::name).collect(Collectors.toSet());
        this.factions.protection.territoryDenyUsageMaterialsWhenOffline = c.territoryDenyUseageMaterialsWhenOffline.stream().filter(Objects::nonNull).map(Material::name).collect(Collectors.toSet());
        this.factions.protection.playersWhoBypassAllProtection = c.playersWhoBypassAllProtection;
        this.factions.protection.worldsNoWildernessProtection = c.worldsNoWildernessProtection;

        this.factions.ownedArea.enabled = c.ownedAreasEnabled;
        this.factions.ownedArea.limitPerFaction = c.ownedAreasLimitPerFaction;
        this.factions.ownedArea.moderatorsCanSet = c.ownedAreasModeratorsCanSet;
        this.factions.ownedArea.moderatorsBypass = c.ownedAreaModeratorsBypass;
        this.factions.ownedArea.denyBuild = c.ownedAreaDenyBuild;
        this.factions.ownedArea.painBuild = c.ownedAreaPainBuild;
        this.factions.ownedArea.protectMaterials = c.ownedAreaProtectMaterials;
        this.factions.ownedArea.denyUsage = c.ownedAreaDenyUseage;
        this.factions.ownedArea.messageOnBorder = c.ownedMessageOnBorder;
        this.factions.ownedArea.messageInsideTerritory = c.ownedMessageInsideTerritory;
        this.factions.ownedArea.messageByChunk = c.ownedMessageByChunk;

        this.factions.allowMultipleColeaders = c.allowMultipleColeaders;
        this.factions.tagLengthMin = c.factionTagLengthMin;
        this.factions.tagLengthMax = c.factionTagLengthMax;
        this.factions.tagForceUpperCase = c.factionTagForceUpperCase;
        this.factions.newFactionsDefaultOpen = c.newFactionsDefaultOpen;
        this.factions.factionMemberLimit = c.factionMemberLimit;
        this.factions.newPlayerStartingFactionID = c.newPlayerStartingFactionID;
        this.factions.saveToFileEveryXMinutes = c.saveToFileEveryXMinutes;
        this.factions.autoLeaveAfterDaysOfInactivity = c.autoLeaveAfterDaysOfInactivity;
        this.factions.autoLeaveRoutineRunsEveryXMinutes = c.autoLeaveRoutineRunsEveryXMinutes;
        this.factions.autoLeaveRoutineMaxMillisecondsPerTick = c.autoLeaveRoutineMaxMillisecondsPerTick;
        this.factions.removePlayerDataWhenBanned = c.removePlayerDataWhenBanned;
        this.factions.autoLeaveDeleteFPlayerData = c.autoLeaveDeleteFPlayerData;
        this.factions.considerFactionsReallyOfflineAfterXMinutes = c.considerFactionsReallyOfflineAfterXMinutes;
        this.factions.actionDeniedPainAmount = c.actionDeniedPainAmount;

        this.logging.factionCreate = c.logFactionCreate;
        this.logging.factionDisband = c.logFactionDisband;
        this.logging.factionJoin = c.logFactionJoin;
        this.logging.factionKick = c.logFactionKick;
        this.logging.factionLeave = c.logFactionLeave;
        this.logging.landClaims = c.logLandClaims;
        this.logging.landUnclaims = c.logLandUnclaims;
        this.logging.moneyTransactions = c.logMoneyTransactions;
        this.logging.playerCommands = c.logPlayerCommands;

        this.exploits.obsidianGenerators = c.handleExploitObsidianGenerators;
        this.exploits.enderPearlClipping = c.handleExploitEnderPearlClipping;
        this.exploits.interactionSpam = c.handleExploitInteractionSpam;
        this.exploits.tntWaterlog = c.handleExploitTNTWaterlog;
        this.exploits.liquidFlow = c.handleExploitLiquidFlow;

        this.economy.enabled = c.econEnabled;
        this.economy.universeAccount = c.econUniverseAccount;
        this.economy.costClaimWilderness = c.econCostClaimWilderness;
        this.economy.costClaimFromFactionBonus = c.econCostClaimFromFactionBonus;
        this.economy.overclaimRewardMultiplier = c.econOverclaimRewardMultiplier;
        this.economy.claimAdditionalMultiplier = c.econClaimAdditionalMultiplier;
        this.economy.claimRefundMultiplier = c.econClaimRefundMultiplier;
        this.economy.claimUnconnectedFee = c.econClaimUnconnectedFee;
        this.economy.costCreate = c.econCostCreate;
        this.economy.costOwner = c.econCostOwner;
        this.economy.costSethome = c.econCostSethome;
        this.economy.costJoin = c.econCostJoin;
        this.economy.costLeave = c.econCostLeave;
        this.economy.costKick = c.econCostKick;
        this.economy.costInvite = c.econCostInvite;
        this.economy.costHome = c.econCostHome;
        this.economy.costTag = c.econCostTag;
        this.economy.costDesc = c.econCostDesc;
        this.economy.costTitle = c.econCostTitle;
        this.economy.costList = c.econCostList;
        this.economy.costMap = c.econCostMap;
        this.economy.costPower = c.econCostPower;
        this.economy.costShow = c.econCostShow;
        this.economy.costStuck = c.econCostStuck;
        this.economy.costOpen = c.econCostOpen;
        this.economy.costAlly = c.econCostAlly;
        this.economy.costTruce = c.econCostTruce;
        this.economy.costEnemy = c.econCostEnemy;
        this.economy.costNeutral = c.econCostNeutral;
        this.economy.costNoBoom = c.econCostNoBoom;
        this.economy.bankEnabled = c.bankEnabled;
        this.economy.bankMembersCanWithdraw = c.bankMembersCanWithdraw;
        this.economy.bankFactionPaysCosts = c.bankFactionPaysCosts;
        this.economy.bankFactionPaysLandCosts = c.bankFactionPaysLandCosts;

        this.map.height = c.mapHeight;
        this.map.width = c.mapWidth;
        this.map.showFactionKey = c.showMapFactionKey;
        this.map.showNeutralFactions = c.showNeutralFactionsOnMap;
        this.map.showEnemyFactions = c.showEnemyFactionsOnMap;
        this.map.showTruceFactions = c.showTruceFactionsOnMap;

        this.colors.factions.safezone = c.colorSafezone.name();
        this.colors.factions.warzone = c.colorWar.name();
        this.colors.factions.wilderness = c.colorWilderness.name();
        this.colors.relations.ally = c.colorAlly.name();
        this.colors.relations.enemy = c.colorEnemy.name();
        this.colors.relations.member = c.colorMember.name();
        this.colors.relations.neutral = c.colorNeutral.name();
        this.colors.relations.peaceful = c.colorPeaceful.name();
        this.colors.relations.truce = c.colorTruce.name();
    }
}