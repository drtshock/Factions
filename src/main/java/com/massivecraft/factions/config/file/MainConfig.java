package com.massivecraft.factions.config.file;

import com.google.common.collect.ImmutableList;
import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.util.material.FactionMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainConfig {
    public class Colors {
        public class Relations {
            private String member = "GREEN";
            private transient ChatColor memberColor;
            private String ally = "LIGHT_PURPLE";
            private transient ChatColor allyColor;
            private String truce = "DARK_PURPLE";
            private transient ChatColor truceColor;
            private String neutral = "WHITE";
            private transient ChatColor neutralColor;
            private String enemy = "RED";
            private transient ChatColor enemyColor;
            private String peaceful = "GOLD";
            private transient ChatColor peacefulColor;

            public ChatColor getMember() {
                return memberColor = Colors.this.getColor(this.member, this.memberColor, ChatColor.GREEN);
            }

            public ChatColor getAlly() {
                return allyColor = Colors.this.getColor(this.ally, this.allyColor, ChatColor.LIGHT_PURPLE);
            }

            public ChatColor getTruce() {
                return truceColor = Colors.this.getColor(this.truce, this.truceColor, ChatColor.DARK_PURPLE);
            }

            public ChatColor getNeutral() {
                return neutralColor = Colors.this.getColor(this.neutral, this.neutralColor, ChatColor.WHITE);
            }

            public ChatColor getEnemy() {
                return enemyColor = Colors.this.getColor(this.enemy, this.enemyColor, ChatColor.RED);
            }

            public ChatColor getPeaceful() {
                return peacefulColor = Colors.this.getColor(this.peaceful, this.peacefulColor, ChatColor.GOLD);
            }
        }

        public class Factions {
            private String wilderness = "GRAY";
            private transient ChatColor wildernessColor;
            private String safezone = "GOLD";
            private transient ChatColor safezoneColor;
            private String warzone = "DARK_RED";
            private transient ChatColor warzoneColor;

            public ChatColor getWilderness() {
                return wildernessColor = Colors.this.getColor(this.wilderness, this.wildernessColor, ChatColor.GRAY);
            }

            public ChatColor getSafezone() {
                return safezoneColor = Colors.this.getColor(this.safezone, this.safezoneColor, ChatColor.GOLD);
            }

            public ChatColor getWarzone() {
                return warzoneColor = Colors.this.getColor(this.warzone, this.warzoneColor, ChatColor.DARK_RED);
            }
        }

        private Factions factions = new Factions();
        private Relations relations = new Relations();

        private ChatColor getColor(String name, ChatColor current, ChatColor defaultColor) {
            if (current != null) {
                return current;
            }
            ChatColor ret;
            try {
                ret = ChatColor.valueOf(name);
            } catch (IllegalArgumentException e) {
                ret = defaultColor;
            }
            return ret;
        }

        public Factions factions() {
            return factions;
        }

        public Relations relations() {
            return relations;
        }
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

                public boolean canLeaveWithNegativePower() {
                    return canLeaveWithNegativePower;
                }

                public boolean isWarZonePowerLoss() {
                    return warZonePowerLoss;
                }

                public boolean isWildernessPowerLoss() {
                    return wildernessPowerLoss;
                }

                public double getPlayerMin() {
                    return playerMin;
                }

                public double getPlayerMax() {
                    return playerMax;
                }

                public double getPlayerStarting() {
                    return playerStarting;
                }

                public double getPowerPerMinute() {
                    return powerPerMinute;
                }

                public double getLossPerDeath() {
                    return lossPerDeath;
                }

                public boolean isRegenOffline() {
                    return regenOffline;
                }

                public double getOfflineLossPerDay() {
                    return offlineLossPerDay;
                }

                public double getOfflineLossLimit() {
                    return offlineLossLimit;
                }

                public double getFactionMax() {
                    return factionMax;
                }

                public boolean isRespawnHomeFromNoPowerLossWorlds() {
                    return respawnHomeFromNoPowerLossWorlds;
                }

                public Set<String> getWorldsNoPowerLoss() {
                    return worldsNoPowerLoss == null ? Collections.emptySet() : worldsNoPowerLoss;
                }

                public boolean isPeacefulMembersDisablePowerLoss() {
                    return peacefulMembersDisablePowerLoss;
                }
            }

            @Comment("Sets the mode of land/raid control")
            private String system = "power";
            @Comment("Controls the power system of land/raid control\nSet the 'system' value to 'power' to use this system")
            private Power power = new Power();

            public String getSystem() {
                return system;
            }

            public Power power() {
                return power;
            }
        }

        public class Prefix {
            private String admin = "***";
            private String coleader = "**";
            private String mod = "*";
            private String normal = "+";
            private String recruit = "-";

            public String getAdmin() {
                return admin;
            }

            public String getColeader() {
                return coleader;
            }

            public String getMod() {
                return mod;
            }

            public String getNormal() {
                return normal;
            }

            public String getRecruit() {
                return recruit;
            }
        }

        public class Chat {
            @Comment("Allow for players to chat only within their faction, with allies, etc.\n" +
                    "Set to false to only allow public chats through this plugin.")
            private boolean factionOnlyChat = true;
            // Configuration on the Faction tag in chat messages.
            @Comment("If true, disables adding of faction tag so another plugin can manage this")
            private boolean tagHandledByAnotherPlugin = false;
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

            public boolean isFactionOnlyChat() {
                return factionOnlyChat;
            }

            public boolean isTagHandledByAnotherPlugin() {
                return tagHandledByAnotherPlugin;
            }

            public boolean isTagRelationColored() {
                return tagRelationColored;
            }

            public String getTagReplaceString() {
                return tagReplaceString;
            }

            public String getTagInsertAfterString() {
                return tagInsertAfterString;
            }

            public String getTagInsertBeforeString() {
                return tagInsertBeforeString;
            }

            public int getTagInsertIndex() {
                return tagInsertIndex;
            }

            public boolean isTagPadBefore() {
                return tagPadBefore;
            }

            public boolean isTagPadAfter() {
                return tagPadAfter;
            }

            public String getTagFormat() {
                return tagFormat;
            }

            public boolean isAlwaysShowChatTag() {
                return alwaysShowChatTag;
            }

            public String getFactionChatFormat() {
                return factionChatFormat;
            }

            public String getAllianceChatFormat() {
                return allianceChatFormat;
            }

            public String getTruceChatFormat() {
                return truceChatFormat;
            }

            public String getModChatFormat() {
                return modChatFormat;
            }

            public boolean isBroadcastDescriptionChanges() {
                return broadcastDescriptionChanges;
            }

            public boolean isBroadcastTagChanges() {
                return broadcastTagChanges;
            }
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

            public boolean isEnabled() {
                return enabled;
            }

            public boolean isMustBeInClaimedTerritory() {
                return mustBeInClaimedTerritory;
            }

            public boolean isTeleportToOnDeath() {
                return teleportToOnDeath;
            }

            public boolean isTeleportCommandEnabled() {
                return teleportCommandEnabled;
            }

            public boolean isTeleportCommandEssentialsIntegration() {
                return teleportCommandEssentialsIntegration;
            }

            public boolean isTeleportCommandSmokeEffectEnabled() {
                return teleportCommandSmokeEffectEnabled;
            }

            public float getTeleportCommandSmokeEffectThickness() {
                return teleportCommandSmokeEffectThickness;
            }

            public boolean isTeleportAllowedFromEnemyTerritory() {
                return teleportAllowedFromEnemyTerritory;
            }

            public boolean isTeleportAllowedFromDifferentWorld() {
                return teleportAllowedFromDifferentWorld;
            }

            public double getTeleportAllowedEnemyDistance() {
                return teleportAllowedEnemyDistance;
            }

            public boolean isTeleportIgnoreEnemiesIfInOwnTerritory() {
                return teleportIgnoreEnemiesIfInOwnTerritory;
            }
        }

        public class PVP {
            private boolean disablePVPBetweenNeutralFactions = false;
            private boolean disablePVPForFactionlessPlayers = false;
            private boolean enablePVPAgainstFactionlessInAttackersLand = false;
            private int noPVPDamageToOthersForXSecondsAfterLogin = 3;
            private Set<String> worldsIgnorePvP = new HashSet<>();

            public boolean isDisablePVPBetweenNeutralFactions() {
                return disablePVPBetweenNeutralFactions;
            }

            public boolean isDisablePVPForFactionlessPlayers() {
                return disablePVPForFactionlessPlayers;
            }

            public boolean isEnablePVPAgainstFactionlessInAttackersLand() {
                return enablePVPAgainstFactionlessInAttackersLand;
            }

            public int getNoPVPDamageToOthersForXSecondsAfterLogin() {
                return noPVPDamageToOthersForXSecondsAfterLogin;
            }

            public Set<String> getWorldsIgnorePvP() {
                return worldsIgnorePvP == null ? Collections.emptySet() : worldsIgnorePvP;
            }
        }

        public class SpecialCase {
            private boolean peacefulTerritoryDisablePVP = true;
            private boolean peacefulTerritoryDisableMonsters = false;
            private boolean peacefulTerritoryDisableBoom = false;
            private boolean permanentFactionsDisableLeaderPromotion = false;

            public boolean isPeacefulTerritoryDisablePVP() {
                return peacefulTerritoryDisablePVP;
            }

            public boolean isPeacefulTerritoryDisableMonsters() {
                return peacefulTerritoryDisableMonsters;
            }

            public boolean isPeacefulTerritoryDisableBoom() {
                return peacefulTerritoryDisableBoom;
            }

            public boolean isPermanentFactionsDisableLeaderPromotion() {
                return permanentFactionsDisableLeaderPromotion;
            }
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

            public boolean isMustBeConnected() {
                return mustBeConnected;
            }

            public boolean isCanBeUnconnectedIfOwnedByOtherFaction() {
                return canBeUnconnectedIfOwnedByOtherFaction;
            }

            public int getRequireMinFactionMembers() {
                return requireMinFactionMembers;
            }

            public int getLandsMax() {
                return landsMax;
            }

            public int getLineClaimLimit() {
                return lineClaimLimit;
            }

            public int getRadiusClaimFailureLimit() {
                return radiusClaimFailureLimit;
            }

            public Set<String> getWorldsNoClaiming() {
                return worldsNoClaiming == null ? Collections.emptySet() : worldsNoClaiming;
            }
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
            private boolean warZoneBlockCreepers = true;
            private boolean warZoneBlockFireballs = true;
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

            private Set<String> territoryDenyUsageMaterials = new HashSet<>();
            private Set<String> territoryDenyUsageMaterialsWhenOffline = new HashSet<>();
            private transient Set<Material> territoryDenyUsageMaterialsMat;
            private transient Set<Material> territoryDenyUsageMaterialsWhenOfflineMat;

            @Comment("Mainly for other plugins/mods that use a fake player to take actions, which shouldn't be subject to our protections")
            private Set<String> playersWhoBypassAllProtection = new HashSet<>();
            private Set<String> worldsNoWildernessProtection = new HashSet<>();

            private Protection() {
                protectUsage("FIRE_CHARGE");
                protectUsage("FLINT_AND_STEEL");
                protectUsage("BUCKET");
                protectUsage("WATER_BUCKET");
                protectUsage("LAVA_BUCKET");
            }

            private void protectUsage(String material) {
                territoryDenyUsageMaterials.add(material);
                territoryDenyUsageMaterialsWhenOffline.add(material);
            }

            public Set<String> getPermanentFactionMemberDenyCommands() {
                return permanentFactionMemberDenyCommands == null ? Collections.emptySet() : permanentFactionMemberDenyCommands;
            }

            public Set<String> getTerritoryNeutralDenyCommands() {
                return territoryNeutralDenyCommands == null ? Collections.emptySet() : territoryNeutralDenyCommands;
            }

            public Set<String> getTerritoryEnemyDenyCommands() {
                return territoryEnemyDenyCommands == null ? Collections.emptySet() : territoryEnemyDenyCommands;
            }

            public Set<String> getTerritoryAllyDenyCommands() {
                return territoryAllyDenyCommands == null ? Collections.emptySet() : territoryAllyDenyCommands;
            }

            public Set<String> getWarzoneDenyCommands() {
                return warzoneDenyCommands == null ? Collections.emptySet() : warzoneDenyCommands;
            }

            public Set<String> getWildernessDenyCommands() {
                return wildernessDenyCommands == null ? Collections.emptySet() : wildernessDenyCommands;
            }

            public boolean isTerritoryBlockCreepers() {
                return territoryBlockCreepers;
            }

            public boolean isTerritoryBlockCreepersWhenOffline() {
                return territoryBlockCreepersWhenOffline;
            }

            public boolean isTerritoryBlockFireballs() {
                return territoryBlockFireballs;
            }

            public boolean isTerritoryBlockFireballsWhenOffline() {
                return territoryBlockFireballsWhenOffline;
            }

            public boolean isTerritoryBlockTNT() {
                return territoryBlockTNT;
            }

            public boolean isTerritoryBlockTNTWhenOffline() {
                return territoryBlockTNTWhenOffline;
            }

            public boolean isTerritoryDenyEndermanBlocks() {
                return territoryDenyEndermanBlocks;
            }

            public boolean isTerritoryDenyEndermanBlocksWhenOffline() {
                return territoryDenyEndermanBlocksWhenOffline;
            }

            public boolean isSafeZoneDenyBuild() {
                return safeZoneDenyBuild;
            }

            public boolean isSafeZoneDenyUsage() {
                return safeZoneDenyUsage;
            }

            public boolean isSafeZoneBlockTNT() {
                return safeZoneBlockTNT;
            }

            public boolean isSafeZonePreventAllDamageToPlayers() {
                return safeZonePreventAllDamageToPlayers;
            }

            public boolean isSafeZoneDenyEndermanBlocks() {
                return safeZoneDenyEndermanBlocks;
            }

            public boolean isWarZoneDenyBuild() {
                return warZoneDenyBuild;
            }

            public boolean isWarZoneDenyUsage() {
                return warZoneDenyUsage;
            }

            public boolean isWarZoneBlockCreepers() {
                return warZoneBlockCreepers;
            }

            public boolean isWarZoneBlockFireballs() {
                return warZoneBlockFireballs;
            }

            public boolean isWarZoneBlockTNT() {
                return warZoneBlockTNT;
            }

            public boolean isWarZoneFriendlyFire() {
                return warZoneFriendlyFire;
            }

            public boolean isWarZoneDenyEndermanBlocks() {
                return warZoneDenyEndermanBlocks;
            }

            public boolean isWildernessDenyBuild() {
                return wildernessDenyBuild;
            }

            public boolean isWildernessDenyUsage() {
                return wildernessDenyUsage;
            }

            public boolean isWildernessBlockCreepers() {
                return wildernessBlockCreepers;
            }

            public boolean isWildernessBlockFireballs() {
                return wildernessBlockFireballs;
            }

            public boolean isWildernessBlockTNT() {
                return wildernessBlockTNT;
            }

            public boolean isWildernessDenyEndermanBlocks() {
                return wildernessDenyEndermanBlocks;
            }

            public boolean isPistonProtectionThroughDenyBuild() {
                return pistonProtectionThroughDenyBuild;
            }

            public Set<Material> getTerritoryDenyUsageMaterials() {
                if (territoryDenyUsageMaterialsMat == null) {
                    territoryDenyUsageMaterialsMat = new HashSet<>();
                    territoryDenyUsageMaterials.forEach(m -> territoryDenyUsageMaterialsMat.add(FactionMaterial.from(m).get()));
                    territoryDenyUsageMaterialsMat.remove(Material.AIR);
                }
                return territoryDenyUsageMaterialsMat;
            }

            public Set<Material> getTerritoryDenyUsageMaterialsWhenOffline() {
                if (territoryDenyUsageMaterialsWhenOfflineMat == null) {
                    territoryDenyUsageMaterialsWhenOfflineMat = new HashSet<>();
                    territoryDenyUsageMaterialsWhenOffline.forEach(m -> territoryDenyUsageMaterialsWhenOfflineMat.add(FactionMaterial.from(m).get()));
                    territoryDenyUsageMaterialsWhenOfflineMat.remove(Material.AIR);
                }
                return territoryDenyUsageMaterialsWhenOfflineMat;
            }

            public Set<String> getPlayersWhoBypassAllProtection() {
                return playersWhoBypassAllProtection == null ? Collections.emptySet() : playersWhoBypassAllProtection;
            }

            public Set<String> getWorldsNoWildernessProtection() {
                return worldsNoWildernessProtection == null ? Collections.emptySet() : worldsNoWildernessProtection;
            }
        }

        public class OwnedArea {
            private boolean enabled = true;
            private int limitPerFaction = 0;
            private boolean moderatorsBypass = true;
            private boolean denyBuild = true;
            private boolean painBuild = false;
            private boolean protectMaterials = true;
            private boolean denyUsage = true;

            private boolean messageOnBorder = true;
            private boolean messageInsideTerritory = true;
            private boolean messageByChunk = false;

            public boolean isEnabled() {
                return enabled;
            }

            public int getLimitPerFaction() {
                return limitPerFaction;
            }

            public boolean isModeratorsBypass() {
                return moderatorsBypass;
            }

            public boolean isDenyBuild() {
                return denyBuild;
            }

            public boolean isPainBuild() {
                return painBuild;
            }

            public boolean isProtectMaterials() {
                return protectMaterials;
            }

            public boolean isDenyUsage() {
                return denyUsage;
            }

            public boolean isMessageOnBorder() {
                return messageOnBorder;
            }

            public boolean isMessageInsideTerritory() {
                return messageInsideTerritory;
            }

            public boolean isMessageByChunk() {
                return messageByChunk;
            }
        }

        public class Commands {

            public class ShowClaims {
                private List<String> show = defaultShow();
                private String coordinate = "&8&l(&a{x}&8:&a{z}&8&l) ";

                public List<String> getShow() {
                    if (show == null) {
                        return defaultShow();
                    }
                    return show;
                }

                public String getCoordinate() {
                    return coordinate;
                }

                private List<String> defaultShow() {
                    return new ArrayList<String>() {
                        {
                            this.add("{header}");
                            this.add("&2&lOverworld {showclaims_world}");
                            this.add("&4&lNether {showclaims_world_nether}");
                            this.add("&5&lThe End {showclaims_world_the_end}");
                        }
                    };
                }
            }

            private ShowClaims showClaims = new ShowClaims();

            public ShowClaims showClaims() {
                return showClaims;
            }
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
        private Commands commands = new Commands();

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

        public boolean isSeparateOfflinePerms() {
            return separateOfflinePerms;
        }

        public Chat chat() {
            return chat;
        }

        public Homes homes() {
            return homes;
        }

        public PVP pvp() {
            return pvp;
        }

        public SpecialCase specialCase() {
            return specialCase;
        }

        public Claims claims() {
            return claims;
        }

        public Protection protection() {
            return protection;
        }

        public OwnedArea ownedArea() {
            return ownedArea;
        }

        public Prefix prefixes() {
            return prefixes;
        }

        public LandRaidControl landRaidControl() {
            return landRaidControl;
        }

        public Commands commands() {
            return commands;
        }

        public boolean isAllowMultipleColeaders() {
            return allowMultipleColeaders;
        }

        public int getTagLengthMin() {
            return tagLengthMin;
        }

        public int getTagLengthMax() {
            return tagLengthMax;
        }

        public boolean isTagForceUpperCase() {
            return tagForceUpperCase;
        }

        public boolean isNewFactionsDefaultOpen() {
            return newFactionsDefaultOpen;
        }

        public int getFactionMemberLimit() {
            return factionMemberLimit;
        }

        public String getNewPlayerStartingFactionID() {
            return newPlayerStartingFactionID;
        }

        public double getSaveToFileEveryXMinutes() {
            return saveToFileEveryXMinutes;
        }

        public double getAutoLeaveAfterDaysOfInactivity() {
            return autoLeaveAfterDaysOfInactivity;
        }

        public double getAutoLeaveRoutineRunsEveryXMinutes() {
            return autoLeaveRoutineRunsEveryXMinutes;
        }

        public int getAutoLeaveRoutineMaxMillisecondsPerTick() {
            return autoLeaveRoutineMaxMillisecondsPerTick;
        }

        public boolean isRemovePlayerDataWhenBanned() {
            return removePlayerDataWhenBanned;
        }

        public boolean isAutoLeaveDeleteFPlayerData() {
            return autoLeaveDeleteFPlayerData;
        }

        public double getConsiderFactionsReallyOfflineAfterXMinutes() {
            return considerFactionsReallyOfflineAfterXMinutes;
        }

        public int getActionDeniedPainAmount() {
            return actionDeniedPainAmount;
        }
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

        public boolean isFactionCreate() {
            return factionCreate;
        }

        public boolean isFactionDisband() {
            return factionDisband;
        }

        public boolean isFactionJoin() {
            return factionJoin;
        }

        public boolean isFactionKick() {
            return factionKick;
        }

        public boolean isFactionLeave() {
            return factionLeave;
        }

        public boolean isLandClaims() {
            return landClaims;
        }

        public boolean isLandUnclaims() {
            return landUnclaims;
        }

        public boolean isMoneyTransactions() {
            return moneyTransactions;
        }

        public boolean isPlayerCommands() {
            return playerCommands;
        }
    }

    public class Exploits {
        private boolean obsidianGenerators = true;
        private boolean enderPearlClipping = true;
        private boolean interactionSpam = true;
        private boolean tntWaterlog = false;
        private boolean liquidFlow = false;
        private boolean preventDuping = true;

        public boolean isObsidianGenerators() {
            return obsidianGenerators;
        }

        public boolean isEnderPearlClipping() {
            return enderPearlClipping;
        }

        public boolean isInteractionSpam() {
            return interactionSpam;
        }

        public boolean isTntWaterlog() {
            return tntWaterlog;
        }

        public boolean isLiquidFlow() {
            return liquidFlow;
        }

        public boolean doPreventDuping() {
            return preventDuping;
        }
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
        private double costDelhome = 30.0;
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

        public boolean isEnabled() {
            return enabled;
        }

        public String getUniverseAccount() {
            return universeAccount;
        }

        public double getCostClaimWilderness() {
            return costClaimWilderness;
        }

        public double getCostClaimFromFactionBonus() {
            return costClaimFromFactionBonus;
        }

        public double getOverclaimRewardMultiplier() {
            return overclaimRewardMultiplier;
        }

        public double getClaimAdditionalMultiplier() {
            return claimAdditionalMultiplier;
        }

        public double getClaimRefundMultiplier() {
            return claimRefundMultiplier;
        }

        public double getClaimUnconnectedFee() {
            return claimUnconnectedFee;
        }

        public double getCostCreate() {
            return costCreate;
        }

        public double getCostOwner() {
            return costOwner;
        }

        public double getCostSethome() {
            return costSethome;
        }

        public double getCostDelhome() {
            return costDelhome;
        }

        public double getCostJoin() {
            return costJoin;
        }

        public double getCostLeave() {
            return costLeave;
        }

        public double getCostKick() {
            return costKick;
        }

        public double getCostInvite() {
            return costInvite;
        }

        public double getCostHome() {
            return costHome;
        }

        public double getCostTag() {
            return costTag;
        }

        public double getCostDesc() {
            return costDesc;
        }

        public double getCostTitle() {
            return costTitle;
        }

        public double getCostList() {
            return costList;
        }

        public double getCostMap() {
            return costMap;
        }

        public double getCostPower() {
            return costPower;
        }

        public double getCostShow() {
            return costShow;
        }

        public double getCostStuck() {
            return costStuck;
        }

        public double getCostOpen() {
            return costOpen;
        }

        public double getCostAlly() {
            return costAlly;
        }

        public double getCostTruce() {
            return costTruce;
        }

        public double getCostEnemy() {
            return costEnemy;
        }

        public double getCostNeutral() {
            return costNeutral;
        }

        public double getCostNoBoom() {
            return costNoBoom;
        }

        public boolean isBankEnabled() {
            return bankEnabled;
        }

        public boolean isBankMembersCanWithdraw() {
            return bankMembersCanWithdraw;
        }

        public boolean isBankFactionPaysCosts() {
            return bankFactionPaysCosts;
        }

        public boolean isBankFactionPaysLandCosts() {
            return bankFactionPaysLandCosts;
        }
    }

    public class Map {
        private int height = 17;
        private int width = 49;
        private boolean showFactionKey = true;
        private boolean showNeutralFactionsOnMap = true;
        private boolean showEnemyFactions = true;
        private boolean showTruceFactions = true;

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }

        public boolean isShowFactionKey() {
            return showFactionKey;
        }

        public boolean isShowNeutralFactionsOnMap() {
            return showNeutralFactionsOnMap;
        }

        public boolean isShowEnemyFactions() {
            return showEnemyFactions;
        }

        public boolean isShowTruceFactions() {
            return showTruceFactions;
        }
    }

    public class Data {
        public class Json {
            @Comment("If true, data files will be stored without extra whitespace and linebreaks.\n" +
                    "This becomes less readable, but can cut storage use in half.")
            private boolean efficientStorage = false;

            public boolean useEfficientStorage() {
                return efficientStorage;
            }
        }

        @Comment("Presently, the only option is JSON.")
        private String storage = "JSON";
        private Json json = new Json();

        public Json json() {
            return json;
        }
    }

    public class RestrictWorlds {
        @Comment("If true, Factions will only function on certain worlds")
        private boolean restrictWorlds = false;
        @Comment("If restrictWorlds is true, this setting determines if the world list below is a whitelist or blacklist.\n" +
                "True for whitelist, false for blacklist.")
        private boolean whitelist = true;
        private Set<String> worldList = new HashSet<>();

        public boolean isRestrictWorlds() {
            return restrictWorlds;
        }

        public boolean isWhitelist() {
            return whitelist;
        }

        public Set<String> getWorldList() {
            return worldList == null ? Collections.emptySet() : worldList;
        }
    }

    public class PlayerVaults {
        @Comment("The %s is for the faction id")
        private String vaultPrefix = "faction-%s";
        private int defaultMaxVaults = 0;

        public String getVaultPrefix() {
            return vaultPrefix;
        }

        public int getDefaultMaxVaults() {
            return defaultMaxVaults;
        }
    }

    public class WorldGuard {
        private boolean checking;
        private boolean buildPriority;

        public boolean isChecking() {
            return checking;
        }

        public boolean isBuildPriority() {
            return buildPriority;
        }
    }

    @Comment("The command base (by default f, making the command /f)")
    private List<String> commandBase = new ArrayList<String>() {
        {
            this.add("f");
        }
    };

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
    @Comment("Data storage settings")
    private Data data = new Data();
    private RestrictWorlds restrictWorlds = new RestrictWorlds();
    @Comment("PlayerVaults faction vault settings")
    private PlayerVaults playerVaults = new PlayerVaults();
    @Comment("WorldGuard settings")
    private WorldGuard worldGuard = new WorldGuard();

    public List<String> getCommandBase() {
        return commandBase == null ? ImmutableList.of("f") : commandBase;
    }

    public Colors colors() {
        return colors;
    }

    public Factions factions() {
        return factions;
    }

    public Logging logging() {
        return logging;
    }

    public Exploits exploits() {
        return exploits;
    }

    public Economy economy() {
        return economy;
    }

    public Map map() {
        return map;
    }

    public RestrictWorlds restrictWorlds() {
        return restrictWorlds;
    }

    public PlayerVaults playerVaults() {
        return playerVaults;
    }

    public WorldGuard worldGuard() {
        return worldGuard;
    }

    public Data data() {
        return data;
    }
}