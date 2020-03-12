package com.massivecraft.factions.config.file;

import com.google.common.collect.ImmutableList;
import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.config.annotation.WipeOnReload;
import com.massivecraft.factions.util.material.FactionMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"FieldCanBeLocal", "InnerClassMayBeStatic", "BooleanMethodIsAlwaysInverted"})
public class MainConfig {
    public static class AVeryFriendlyFactionsConfig {
        @Comment("Don't change this value yourself, unless you WANT a broken config!")
        private int version = 3;

        @Comment("Debug\n" +
                "Turn this on if you are having issues with something and working on resolving them.\n" +
                "This will spam your console with information that is useful if you know how to read the source.\n" +
                "It's suggested that you only turn this on at the direction of a developer.")
        private boolean debug = false;

        public boolean isDebug() {
            return debug;
        }
    }

    public class Colors {
        public class Relations {
            private String member = "GREEN";
            @WipeOnReload
            private transient ChatColor memberColor;
            private String ally = "LIGHT_PURPLE";
            @WipeOnReload
            private transient ChatColor allyColor;
            private String truce = "DARK_PURPLE";
            @WipeOnReload
            private transient ChatColor truceColor;
            private String neutral = "WHITE";
            @WipeOnReload
            private transient ChatColor neutralColor;
            private String enemy = "RED";
            @WipeOnReload
            private transient ChatColor enemyColor;
            private String peaceful = "GOLD";
            @WipeOnReload
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
            @WipeOnReload
            private transient ChatColor wildernessColor;
            private String safezone = "GOLD";
            @WipeOnReload
            private transient ChatColor safezoneColor;
            private String warzone = "DARK_RED";
            @WipeOnReload
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

    public class Commands {
        public class Fly {
            public class Particles {
                @Comment("Speed of the particles, can be decimal value")
                private double speed = 0.02;
                @Comment("Amount spawned")
                private int amount = 20;
                @Comment("How often should we spawn these particles?\n" +
                        "0 disables this completely")
                private double spawnRate = 0.2;

                public double getSpeed() {
                    return speed;
                }

                public int getAmount() {
                    return amount;
                }

                public double getSpawnRate() {
                    return spawnRate;
                }
            }

            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private int delay = 0;
            @Comment("True to enable the fly command, false to disable")
            private boolean enable = true;
            @Comment("If a player leaves fly (out of territory or took damage)\n" +
                    "how long (in seconds) should they not take fall damage for?\n" +
                    "Set to 0 to have them always take fall damage.")
            private int fallDamageCooldown = 3;
            @Comment("From how far away a player can disable another's flight by being enemy\n" +
                    "Set to 0 if wanted disable\n" +
                    "Note: Will produce lag at higher numbers")
            private int enemyRadius = 7;
            @Comment("How frequently to check enemy radius, in seconds. Set to 0 to disable checking.")
            private int radiusCheck = 1;
            @Comment("Should we disable flight if the player has suffered generic damage")
            private boolean disableOnGenericDamage = false;

            @Comment("Trails show below the players foot when flying, faction.fly.trails\n" +
                    "Players can enable them with /f trail on/off\n" +
                    "Players can also set which effect to show /f trail effect <particle> only if they have faction.fly.trails.<particle>")
            private Particles particles = new Particles();

            public int getDelay() {
                return delay;
            }

            public boolean isEnable() {
                return enable;
            }

            public int getFallDamageCooldown() {
                return fallDamageCooldown;
            }

            public int getEnemyRadius() {
                return enemyRadius;
            }

            public int getRadiusCheck() {
                return radiusCheck;
            }

            public boolean isDisableOnGenericDamage() {
                return disableOnGenericDamage;
            }

            public Particles particles() {
                return particles;
            }
        }

        public class Help {
            @Comment("You can change the page name to whatever you like\n" +
                    "We use '1' to preserve default functionality of /f help 1")
            private Map<String, List<String>> entries = new HashMap<String, List<String>>() {
                {
                    this.put("1", Arrays.asList(
                            "&e&m----------------------------------------------",
                            "                  &c&lFactions Help               ",
                            "&e&m----------------------------------------------",
                            "&3/f create  &e>>  &7Create your own faction",
                            "&3/f who      &e>>  &7Show factions info",
                            "&3/f tag      &e>>  &7Change faction tag",
                            "&3/f join     &e>>  &7Join faction",
                            "&3/f list      &e>>  &7List all factions",
                            "&e&m--------------&r &2/f help 2 for more &e&m--------------"));
                    this.put("2", Arrays.asList(
                            "&e&m------------------&r&c&l Page 2 &e&m--------------------",
                            "&3/f home     &e>>  &7Teleport to faction home",
                            "&3/f sethome &e>>  &7Set your faction home",
                            "&3/f leave    &e>>  &7Leave your faction",
                            "&3/f invite    &e>>  &7Invite a player to your faction",
                            "&3/f deinvite &e>>  &7Revoke invitation to player",
                            "&e&m--------------&r &2/f help 3 for more &e&m--------------"));
                    this.put("3", Arrays.asList(
                            "&e&m------------------&r&c&l Page 3 &e&m--------------------",
                            "&3/f claim     &e>>  &7Claim land",
                            "&3/f unclaim  &e>>  &7Unclaim land",
                            "&3/f kick      &e>>  &7Kick player from your faction",
                            "&3/f mod      &e>>  &7Set player role in faction",
                            "&3/f chat     &e>>  &7Switch to faction chat",
                            "&e&m--------------&r &2/f help 4 for more &e&m--------------"));
                    this.put("4", Arrays.asList(
                            "&e&m------------------&r&c&l Page 4 &e&m--------------------",
                            "&3/f version &e>>  &7Display version information",
                            "&e&m--------------&r&2 End of /f help &e&m-----------------"));
                }
            };
            @Comment("set to true to use legacy factions help")
            private boolean useOldHelp = true;

            public Map<String, List<String>> getEntries() {
                return entries != null ? Collections.unmodifiableMap(entries) : Collections.emptyMap();
            }

            public boolean isUseOldHelp() {
                return useOldHelp;
            }
        }

        public class Home {
            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private int delay = 0;

            public int getDelay() {
                return delay;
            }
        }

        public class ListCmd {
            @Comment("You can only use {pagenumber} and {pagecount} in the header.\nBlank entry results in nothing being displayed.")
            private String header = "&e&m----------&r&e[ &2Faction List &9{pagenumber}&e/&9{pagecount} &e]&m----------";
            @Comment("You can only use {pagenumber} and {pagecount} in the footer.\nBlank entry results in nothing being displayed.")
            private String footer = "";
            @Comment("You can use any variables here")
            private String factionlessEntry = "<i>Factionless<i> {factionless} online";
            @Comment("You can use any variable here")
            private String entry = "<a>{faction-relation-color}{faction} <i>{online} / {members} online, <a>Land / Power / Maxpower: <i>{chunks}/{power}/{maxPower}";

            public String getHeader() {
                return header;
            }

            public String getFooter() {
                return footer;
            }

            public String getFactionlessEntry() {
                return factionlessEntry;
            }

            public String getEntry() {
                return entry;
            }
        }

        public class MapCmd {
            @Comment("This will help limit how many times a player can be sent a map of factions.\n" +
                    "Set this to the cooldown you want, in milliseconds, for a map to be shown to a player.\n" +
                    "This can prevent some teleportation-based exploits for finding factions.\n" +
                    "The old default was 2000, which blocks any movement faster than running.\n" +
                    "The new default is 700, which should also allow boats and horses.")
            private int cooldown = 700;

            public int getCooldown() {
                return cooldown;
            }
        }

        public class Near {
            @Comment("Making this radius larger increases lag, do so at your own risk\n" +
                    "If on a high radius it is advised to add a cooldown to the command\n" +
                    "Also using {distance} placeholder in the lang would cause more lag on a bigger radius")
            private int radius = 20;

            public int getRadius() {
                return radius;
            }
        }

        public class SeeChunk {
            private boolean particles = true;
            @Comment("Get a list of particle names here: https://factions.support/particles/")
            private String particleName = "REDSTONE";
            @Comment("If the chosen particle is compatible with coloring we will color\n" +
                    "it based on the current chunk's faction")
            private boolean relationalColor = true;
            @Comment("How often should we update the particles to the current player's location?")
            private double particleUpdateTime = 0.75;

            public boolean isParticles() {
                return particles;
            }

            public String getParticleName() {
                return particleName;
            }

            public boolean isRelationalColor() {
                return relationalColor;
            }

            public double getParticleUpdateTime() {
                return particleUpdateTime;
            }
        }

        public class Show {
            @Comment("You can use any variable here, including fancy messages. Color codes and or tags work fine.\n" +
                    "Lines that aren't defined wont be sent (home not set, faction not peaceful / permanent, dtr freeze)\n" +
                    "Supports placeholders.\n" +
                    "First line can be {header} for default header, or any string (we recommend &m for smooth lines ;p)\n" +
                    "The line with 'permanent' in it only appears if the faction is permanent.")
            private List<String> format = new ArrayList<String>() {
                {
                    this.add("{header}");
                    this.add("<a>Description: <i>{description}");
                    this.add("<a>Joining: <i>{joining}    {peaceful}");
                    this.add("<a>Land / Power / Maxpower: <i> {chunks}/{power}/{maxPower}");
                    this.add("<a>Raidable: {raidable}");
                    this.add("<a>Founded: <i>{create-date}");
                    this.add("<a>This faction is permanent, remaining even with no members.'");
                    this.add("<a>Land value: <i>{land-value} {land-refund}");
                    this.add("<a>Balance: <i>{faction-balance}");
                    this.add("<a>Bans: <i>{faction-bancount}");
                    this.add("<a>Allies(<i>{allies}<a>/<i>{max-allies}<a>): {allies-list} ");
                    this.add("<a>Online: (<i>{online}<a>/<i>{members}<a>): {online-list}");
                    this.add("<a>Offline: (<i>{offline}<a>/<i>{members}<a>): {offline-list}");
                }
            };
            @Comment("Set true to not display empty fancy messages")
            private boolean minimal = false;
            @Comment("Factions that should be exempt from /f show, case sensitive, useful for a\n" +
                    "serverteam faction, since the command shows vanished players otherwise")
            private List<String> exempt = new ArrayList<String>() {
                {
                    this.add("put_faction_tag_here");
                }
            };

            public List<String> getFormat() {
                return format != null ? Collections.unmodifiableList(format) : Collections.emptyList();
            }

            public boolean isMinimal() {
                return minimal;
            }

            public List<String> getExempt() {
                return exempt != null ? Collections.unmodifiableList(exempt) : Collections.emptyList();
            }
        }

        public class Stuck {
            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private int delay = 30;
            @Comment("This radius defines how far from where they ran the command the player\n" +
                    "may travel while waiting to be unstuck. If they leave this radius, the\n" +
                    "command will be cancelled.")
            private int radius = 10;

            public int getDelay() {
                return delay;
            }

            public int getRadius() {
                return radius;
            }
        }

        public class TNT {
            private boolean enable = false;
            @Comment("Maximum storage. Set to -1 (or lower) to disable")
            private int maxStorage = -1;
            private int maxRadius = 5;

            public int getMaxRadius() {
                return maxRadius;
            }

            public int getMaxStorage() {
                return maxStorage;
            }

            public boolean isAboveMaxStorage(int amount) {
                if (maxStorage < 0) {
                    return false;
                }
                return amount > maxStorage;
            }

            public boolean isEnable() {
                return enable;
            }
        }

        public class Warp {
            @Comment("Warmup seconds before command executes. Set to 0 for no warmup.")
            private int delay = 0;
            @Comment("What should be the maximum amount of warps that a Faction can set?")
            private int maxWarps = 5;

            public int getDelay() {
                return delay;
            }

            public int getMaxWarps() {
                return maxWarps;
            }
        }

        public class ToolTips {
            @Comment("Faction on-hover tooltip information")
            private List<String> faction = new ArrayList<String>() {
                {
                    this.add("&6Leader: &f{leader}");
                    this.add("&6Claimed: &f{chunks}");
                    this.add("&6Raidable: &f{raidable}");
                    this.add("&6Warps: &f{warps}");
                    this.add("&6Power: &f{power}/{maxPower}");
                    this.add("&6Members: &f{online}/{members}");
                }
            };
            @Comment("Player on-hover tooltip information")
            private List<String> player = new ArrayList<String>() {
                {
                    this.add("&6Last Seen: &f{lastSeen}");
                    this.add("&6Power: &f{player-power}");
                    this.add("&6Rank: &f{group}");
                    this.add("&6Balance: &a${balance}");
                }
            };

            public List<String> getFaction() {
                return faction != null ? Collections.unmodifiableList(faction) : Collections.emptyList();
            }

            public List<String> getPlayer() {
                return player != null ? Collections.unmodifiableList(player) : Collections.emptyList();
            }
        }

        private Fly fly = new Fly();
        private Help help = new Help();
        private Home home = new Home();
        private ListCmd list = new ListCmd();
        private MapCmd map = new MapCmd();
        private Near near = new Near();
        private SeeChunk seeChunk = new SeeChunk();
        private Show show = new Show();
        private Stuck stuck = new Stuck();
        @Comment("TNT bank!")
        private TNT tnt = new TNT();
        private ToolTips toolTips = new ToolTips();
        private Warp warp = new Warp();

        public Fly fly() {
            return fly;
        }

        public Help help() {
            return help;
        }

        public Home home() {
            return home;
        }

        public ListCmd list() {
            return list;
        }

        public MapCmd map() {
            return map;
        }

        public Near near() {
            return near;
        }

        public SeeChunk seeChunk() {
            return seeChunk;
        }

        public Show show() {
            return show;
        }

        public Stuck stuck() {
            return stuck;
        }

        public TNT tnt() {
            return tnt;
        }

        public ToolTips toolTips() {
            return toolTips;
        }

        public Warp warp() {
            return warp;
        }
    }

    public class Factions {
        public class LandRaidControl {
            public class DTR {
                private double startingDTR = 2.0;
                private double maxDTR = 10.0;
                private double minDTR = -3.0;
                private double perPlayer = 1;
                private double regainPerMinutePerPlayer = 0.05;
                private double regainPerMinuteMaxRate = 0.1;
                private double lossPerDeath = 1;
                @Comment("Time, in seconds, to freeze DTR regeneration after a faction member dies")
                private int freezeTime = 0;
                private boolean freezePreventsJoin = true;
                private boolean freezePreventsLeave = true;
                private boolean freezePreventsDisband = true;
                private double freezeKickPenalty = 0.5;
                private String freezeTimeFormat = "H:mm:ss";
                @Comment("Additional claims allowed for each player in the faction")
                private int landPerPlayer = 3;
                @Comment("Claims the faction starts with.\n" +
                        "Note: A faction of one player has this many PLUS the perPlayer amount.")
                private int landStarting = 6;
                private int decimalDigits = 2;
                private Map<String, Number> worldDeathModifiers = new HashMap<String, Number>() {
                    {
                        this.put("world_nether", 0.5D);
                        this.put("world_the_end", 0.25D);
                    }
                };

                public int getDecimalDigits() {
                    return decimalDigits;
                }

                public int getLandPerPlayer() {
                    return landPerPlayer;
                }

                public int getLandStarting() {
                    return landStarting;
                }

                public int getFreezeTime() {
                    return freezeTime;
                }

                public String getFreezeTimeFormat() {
                    return freezeTimeFormat;
                }

                public boolean isFreezePreventsJoin() {
                    return freezePreventsJoin;
                }

                public boolean isFreezePreventsLeave() {
                    return freezePreventsLeave;
                }

                public boolean isFreezePreventsDisband() {
                    return freezePreventsDisband;
                }

                public double getFreezeKickPenalty() {
                    return freezeKickPenalty;
                }

                public double getMinDTR() {
                    return minDTR;
                }

                public double getPerPlayer() {
                    return perPlayer;
                }

                public double getRegainPerMinutePerPlayer() {
                    return regainPerMinutePerPlayer;
                }

                public double getRegainPerMinuteMaxRate() {
                    return regainPerMinuteMaxRate;
                }

                public double getMaxDTR() {
                    return maxDTR;
                }

                public double getStartingDTR() {
                    return startingDTR;
                }

                public double getLossPerDeathBase() {
                    return this.lossPerDeath;
                }

                public double getLossPerDeath(World world) {
                    if (this.worldDeathModifiers == null) {
                        this.worldDeathModifiers = new HashMap<>();
                    }
                    return this.lossPerDeath * this.worldDeathModifiers.getOrDefault(world.getName(), 1D).doubleValue();
                }
            }

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
                private Set<String> worldsNoPowerLoss = new HashSet<String>() {
                    {
                        this.add("exampleWorld");
                    }
                };
                private boolean peacefulMembersDisablePowerLoss = true;
                private boolean warZonePowerLoss = true;
                private boolean wildernessPowerLoss = true;
                @Comment("Disallow joining/leaving/kicking while power is negative")
                private boolean canLeaveWithNegativePower = true;
                @Comment("Allow a faction to be raided if they have more land than power.\n" +
                        "This will make claimed territory lose all protections\n" +
                        "  allowing factions to open chests, break blocks, etc. if they\n" +
                        "  have claimed chunks >= power.")
                private boolean raidability = false;
                @Comment("After a player dies, how long should the faction not be able to regen power?\n" +
                        "This resets on each death but does not accumulate.\n" +
                        "Set to 0 for no freeze. Time is in seconds.")
                private int powerFreeze = 0;

                public boolean isRaidability() {
                    return raidability;
                }

                public int getPowerFreeze() {
                    return powerFreeze;
                }

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
                    return worldsNoPowerLoss == null ? Collections.emptySet() : Collections.unmodifiableSet(worldsNoPowerLoss);
                }

                public boolean isPeacefulMembersDisablePowerLoss() {
                    return peacefulMembersDisablePowerLoss;
                }
            }

            @Comment("Sets the mode of land/raid control")
            private String system = "power";
            private DTR dtr = new DTR();
            @Comment("Controls the power system of land/raid control\nSet the 'system' value to 'power' to use this system")
            private Power power = new Power();

            public String getSystem() {
                return system;
            }

            public DTR dtr() {
                return this.dtr;
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

        public class MaxRelations {
            private boolean enabled = false;
            private int ally = 10;
            private int truce = 10;
            private int neutral = -1;
            private int enemy = 10;

            public boolean isEnabled() {
                return enabled;
            }

            public int getAlly() {
                return ally;
            }

            public int getTruce() {
                return truce;
            }

            public int getNeutral() {
                return neutral;
            }

            public int getEnemy() {
                return enemy;
            }
        }

        public class PVP {
            private boolean disablePVPBetweenNeutralFactions = false;
            private boolean disablePVPForFactionlessPlayers = false;
            private boolean enablePVPAgainstFactionlessInAttackersLand = false;
            private int noPVPDamageToOthersForXSecondsAfterLogin = 3;
            private Set<String> worldsIgnorePvP = new HashSet<String>() {
                {
                    this.add("exampleWorldName");
                }
            };

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
                return worldsIgnorePvP == null ? Collections.emptySet() : Collections.unmodifiableSet(worldsIgnorePvP);
            }
        }

        public class SpecialCase {
            private boolean peacefulTerritoryDisablePVP = true;
            private boolean peacefulTerritoryDisableMonsters = false;
            private boolean peacefulTerritoryDisableBoom = false;
            private boolean permanentFactionsDisableLeaderPromotion = false;
            @Comment("Material names of things whose placement is ignored in faction territory")
            private Set<String> ignoreBuildMaterials = new HashSet<String>() {
                {
                    this.add("exampleMaterial");
                }
            };
            @WipeOnReload
            private transient Set<Material> ignoreBuildMaterialsMat;

            public Set<Material> getIgnoreBuildMaterials() {
                if (ignoreBuildMaterialsMat == null) {
                    ignoreBuildMaterialsMat = new HashSet<>();
                    ignoreBuildMaterials.forEach(m -> ignoreBuildMaterialsMat.add(FactionMaterial.from(m).get()));
                    ignoreBuildMaterialsMat.remove(Material.AIR);
                    ignoreBuildMaterials = Collections.unmodifiableSet(ignoreBuildMaterials);
                }
                return ignoreBuildMaterialsMat;
            }

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

        public class Portals {
            @Comment("If true, portals will be limited to the minimum relation below")
            private boolean limit = false;
            @Comment("What should the minimum relation be to create a portal in territory?\n" +
                    "Goes in the order of: ENEMY, NEUTRAL, ALLY, MEMBER.\n" +
                    "Minimum relation allows that and all listed to the right to create portals.\n" +
                    "Example: put ALLY to allow ALLY and MEMBER to be able to create portals.\n" +
                    "If typed incorrectly, defaults to NEUTRAL.")
            private String minimumRelation = "MEMBER";

            public boolean isLimit() {
                return limit;
            }

            public String getMinimumRelation() {
                return minimumRelation;
            }
        }

        public class Claims {
            private boolean mustBeConnected = false;
            private boolean canBeUnconnectedIfOwnedByOtherFaction = true;
            private int requireMinFactionMembers = 1;
            private int landsMax = 0;
            private int lineClaimLimit = 5;
            private int fillClaimMaxClaims = 25;
            private int fillClaimMaxDistance = 5;
            @Comment("If someone is doing a radius claim and the process fails to claim land this many times in a row, it will exit")
            private int radiusClaimFailureLimit = 9;
            private Set<String> worldsNoClaiming = new HashSet<String>() {
                {
                    this.add("exampleWorldName");
                }
            };
            @Comment("Buffer Zone is an chunk area required between claims of different Factions.\n" +
                    "This is default to 0 and has always been that way. Meaning Factions can have\n" +
                    "  claims that border each other.\n" +
                    "If this is set to 3, then Factions need to have 3 chunks between their claim\n" +
                    "  and another Faction's claim.\n" +
                    "It's recommended to keep this pretty low as the radius check could be a\n" +
                    "  heavy operation if set to a large number.\n" +
                    "If this is set to 0, we won't even bother checking which is how Factions has\n" +
                    "  always been.")
            private int bufferZone = 0;
            @Comment("Should we allow Factions to over claim if they are raidable?\n" +
                    "This has always been true, allowing factions to over claim others.")
            private boolean allowOverClaim = true;
            @Comment("If true (and allowOverClaim is true, claiming over another faction's land will ignore buffer zone settings.")
            private boolean allowOverClaimIgnoringBuffer = false;

            public boolean isAllowOverClaim() {
                return allowOverClaim;
            }

            public boolean isAllowOverClaimAndIgnoringBuffer() {
                return allowOverClaim && allowOverClaimIgnoringBuffer;
            }

            public int getBufferZone() {
                return bufferZone;
            }

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

            public int getFillClaimMaxClaims() {
                return fillClaimMaxClaims;
            }

            public int getFillClaimMaxDistance() {
                return fillClaimMaxDistance;
            }

            public int getLineClaimLimit() {
                return lineClaimLimit;
            }

            public int getRadiusClaimFailureLimit() {
                return radiusClaimFailureLimit;
            }

            public Set<String> getWorldsNoClaiming() {
                return worldsNoClaiming == null ? Collections.emptySet() : Collections.unmodifiableSet(worldsNoClaiming);
            }
        }

        public class Protection {
            @Comment("Commands which will be prevented if the player is a member of a permanent faction")
            private Set<String> permanentFactionMemberDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };

            @Comment("Commands which will be prevented when in claimed territory of a neutral faction")
            private Set<String> territoryNeutralDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };
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
            private Set<String> territoryAllyDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };
            @Comment("Commands which will be prevented when in warzone")
            private Set<String> warzoneDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };
            @Comment("Commands which will be prevented when in wilderness")
            private Set<String> wildernessDenyCommands = new HashSet<String>() {
                {
                    this.add("exampleCommand");
                }
            };

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
            private boolean warZonePreventMonsterSpawns = false;

            private boolean wildernessDenyBuild = false;
            private boolean wildernessDenyUsage = false;
            private boolean wildernessBlockCreepers = false;
            private boolean wildernessBlockFireballs = false;
            private boolean wildernessBlockTNT = false;

            private boolean wildernessDenyEndermanBlocks = false;

            private boolean pistonProtectionThroughDenyBuild = true;

            private Set<String> territoryDenyUsageMaterials = new HashSet<>();
            private Set<String> territoryDenyUsageMaterialsWhenOffline = new HashSet<>();
            @WipeOnReload
            private transient Set<Material> territoryDenyUsageMaterialsMat;
            @WipeOnReload
            private transient Set<Material> territoryDenyUsageMaterialsWhenOfflineMat;

            @Comment("Mainly for other plugins/mods that use a fake player to take actions, which shouldn't be subject to our protections")
            private Set<String> playersWhoBypassAllProtection = new HashSet<String>() {
                {
                    this.add("example-player-name");
                }
            };
            private Set<String> worldsNoWildernessProtection = new HashSet<String>() {
                {
                    this.add("exampleWorld");
                }
            };

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
                return permanentFactionMemberDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(permanentFactionMemberDenyCommands);
            }

            public Set<String> getTerritoryNeutralDenyCommands() {
                return territoryNeutralDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(territoryNeutralDenyCommands);
            }

            public Set<String> getTerritoryEnemyDenyCommands() {
                return territoryEnemyDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(territoryEnemyDenyCommands);
            }

            public Set<String> getTerritoryAllyDenyCommands() {
                return territoryAllyDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(territoryAllyDenyCommands);
            }

            public Set<String> getWarzoneDenyCommands() {
                return warzoneDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(warzoneDenyCommands);
            }

            public Set<String> getWildernessDenyCommands() {
                return wildernessDenyCommands == null ? Collections.emptySet() : Collections.unmodifiableSet(wildernessDenyCommands);
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

            public boolean isWarZonePreventMonsterSpawns() {
                return warZonePreventMonsterSpawns;
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
                    territoryDenyUsageMaterialsMat = Collections.unmodifiableSet(territoryDenyUsageMaterialsMat);
                }
                return territoryDenyUsageMaterialsMat;
            }

            public Set<Material> getTerritoryDenyUsageMaterialsWhenOffline() {
                if (territoryDenyUsageMaterialsWhenOfflineMat == null) {
                    territoryDenyUsageMaterialsWhenOfflineMat = new HashSet<>();
                    territoryDenyUsageMaterialsWhenOffline.forEach(m -> territoryDenyUsageMaterialsWhenOfflineMat.add(FactionMaterial.from(m).get()));
                    territoryDenyUsageMaterialsWhenOfflineMat.remove(Material.AIR);
                    territoryDenyUsageMaterialsWhenOfflineMat = Collections.unmodifiableSet(territoryDenyUsageMaterialsWhenOfflineMat);
                }
                return territoryDenyUsageMaterialsWhenOfflineMat;
            }

            public Set<String> getPlayersWhoBypassAllProtection() {
                return playersWhoBypassAllProtection == null ? Collections.emptySet() : Collections.unmodifiableSet(playersWhoBypassAllProtection);
            }

            public Set<String> getWorldsNoWildernessProtection() {
                return worldsNoWildernessProtection == null ? Collections.emptySet() : Collections.unmodifiableSet(worldsNoWildernessProtection);
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

        public class Other {
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

            @Comment("Should we delete player homes that they set via Essentials when they leave a Faction\n" +
                    "if they have homes set in that Faction's territory?")
            private boolean deleteEssentialsHomes = true;

            @Comment("Default Relation allows you to change the default relation for Factions.\n" +
                    "Example usage would be so people can't leave then make a new Faction while Raiding\n" +
                    "  in order to be able to execute commands if the default relation is neutral.")
            private String defaultRelation = "neutral";

            @Comment("If true, disables pistons entirely within faction territory.\n" +
                    "Prevents flying piston machines in faction territory.")
            private boolean disablePistonsInTerritory = false;

            @Comment("Any faction names CONTAINING any of these items will be disallowed")
            private List<String> nameBlacklist = new ArrayList<String>() {
                {
                    this.add("blockedwordhere");
                    this.add("anotherblockedthinghere");
                }
            };

            public List<String> getNameBlacklist() {
                return nameBlacklist == null ? Collections.emptyList() : Collections.unmodifiableList(this.nameBlacklist);
            }

            public boolean isDisablePistonsInTerritory() {
                return disablePistonsInTerritory;
            }

            public boolean isDeleteEssentialsHomes() {
                return deleteEssentialsHomes;
            }

            public String getDefaultRelation() {
                return defaultRelation;
            }

            public boolean isSeparateOfflinePerms() {
                return separateOfflinePerms;
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

        public class EnterTitles {
            private boolean enabled = true;
            private int fadeIn = 10;
            private int stay = 70;
            private int fadeOut = 20;
            private boolean alsoShowChat = false;
            private String title = "{faction-relation-color}{faction}";
            private String subtitle = "&7{description}";

            public boolean isEnabled() {
                return enabled;
            }

            public int getFadeIn() {
                return fadeIn;
            }

            public int getStay() {
                return stay;
            }

            public int getFadeOut() {
                return fadeOut;
            }

            public boolean isAlsoShowChat() {
                return alsoShowChat;
            }

            public String getTitle() {
                return title;
            }

            public String getSubtitle() {
                return subtitle;
            }
        }

        private Chat chat = new Chat();
        private Homes homes = new Homes();
        @Comment("Limits factions to having a max number of each relation.\n" +
                "Setting to 0 means none allowed. -1 for disabled.\n" +
                "This will have no effect on default or existing relations, only when relations are changed.\n" +
                "It is advised that you set the default relation to -1 so they can always go back to that.\n" +
                "Otherwise Factions could be stuck with not being able to unenemy other Factions.")
        private MaxRelations maxRelations = new MaxRelations();
        private PVP pvp = new PVP();
        private SpecialCase specialCase = new SpecialCase();
        private Claims claims = new Claims();
        @Comment("Do you want to limit portal creation?")
        private Portals portals = new Portals();
        private Protection protection = new Protection();
        @Comment("For claimed areas where further faction-member ownership can be defined")
        private OwnedArea ownedArea = new OwnedArea();
        @Comment("Displayed prefixes for different roles within a faction")
        private Prefix prefixes = new Prefix();
        private LandRaidControl landRaidControl = new LandRaidControl();
        @Comment("Remaining settings not categorized")
        private Other other = new Other();
        @Comment("Should we send titles when players enter Factions? Durations are in ticks (20 ticks every second)")
        private EnterTitles enterTitles = new EnterTitles();

        public EnterTitles enterTitles() {
            return enterTitles;
        }

        public Chat chat() {
            return chat;
        }

        public Homes homes() {
            return homes;
        }

        public MaxRelations maxRelations() {
            return maxRelations;
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

        public Portals portals() {
            return portals;
        }

        public Protection protection() {
            return protection;
        }

        public Other other() {
            return other;
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
        @Comment("Must be true for any economy features")
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
        private double costDTR = 0.0;
        private double costOpen = 0.0;
        private double costAlly = 0.0;
        private double costTruce = 0.0;
        private double costEnemy = 0.0;
        private double costNeutral = 0.0;
        private double costNoBoom = 0.0;
        // Calculate if enabled
        private double costWarp = 0.0;
        private double costSetWarp = 0.0;
        private double costDelWarp = 0.0;

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

        public double getCostDTR() {
            return costDTR;
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

        public double getCostWarp() {
            return costWarp;
        }

        public double getCostSetWarp() {
            return costSetWarp;
        }

        public double getCostDelWarp() {
            return costDelWarp;
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

    public class MapSettings {
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
        private Set<String> worldList = new HashSet<String>() {
            {
                this.add("exampleWorld");
            }
        };

        public boolean isRestrictWorlds() {
            return restrictWorlds;
        }

        public boolean isWhitelist() {
            return whitelist;
        }

        public Set<String> getWorldList() {
            return worldList == null ? Collections.emptySet() : Collections.unmodifiableSet(worldList);
        }
    }

    public class Scoreboard {
        public class Constant {
            private boolean enabled = false;
            @Comment("Can use any placeholders, but does not update once set")
            private String title = "Faction Status";
            @Comment("If true, show faction prefixes on nametags and in tab list if scoreboard is enabled")
            private boolean prefixes = true;
            private List<String> content = new ArrayList<String>() {
                {
                    this.add("&6Your Faction");
                    this.add("{faction}");
                    this.add("&3Your Power");
                    this.add("{power}");
                    this.add("&aBalance");
                    this.add("${balance}");
                }
            };
            private boolean factionlessEnabled = false;
            private List<String> factionlessContent = new ArrayList<String>() {
                {
                    this.add("Make a new Faction");
                    this.add("Use /f create");
                }
            };
            private String factionlessTitle = "Status";

            public boolean isEnabled() {
                return enabled;
            }

            public String getTitle() {
                return title;
            }

            public boolean isPrefixes() {
                return prefixes;
            }

            public List<String> getContent() {
                return content != null ? Collections.unmodifiableList(content) : Collections.emptyList();
            }

            public boolean isFactionlessEnabled() {
                return factionlessEnabled;
            }

            public List<String> getFactionlessContent() {
                return factionlessContent != null ? Collections.unmodifiableList(factionlessContent) : Collections.emptyList();
            }

            public String getFactionlessTitle() {
                return factionlessTitle;
            }
        }

        public class Info {
            @Comment("send faction change message as well when scoreboard is up?")
            private boolean alsoSendChat = true;
            @Comment("How long do we want scoreboards to stay")
            private int expiration = 7;
            private boolean enabled = false;
            @Comment("Supports placeholders")
            private List<String> content = new ArrayList<String>() {
                {
                    this.add("&6Power");
                    this.add("{power}");
                    this.add("&3Members");
                    this.add("{online}/{members}");
                    this.add("&4Leader");
                    this.add("{leader}");
                    this.add("&bTerritory");
                    this.add("{chunks}");
                }
            };
            private String title = "{faction-relation-color}{faction}";

            public boolean isAlsoSendChat() {
                return alsoSendChat;
            }

            public int getExpiration() {
                return expiration;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public List<String> getContent() {
                return content != null ? Collections.unmodifiableList(content) : Collections.emptyList();
            }

            public String getTitle() {
                return title;
            }
        }

        @Comment("Constant scoreboard stays around all the time, displaying status info.\n" +
                "Also, if prefixes are enabled while it is enabled, will show prefixes on nametags and tab")
        private Constant constant = new Constant();
        @Comment("Info scoreboard is displayed when a player walks into a new Faction's territory.\n" +
                "Scoreboard disappears after <expiration> seconds.")
        private Info info = new Info();

        public Constant constant() {
            return constant;
        }

        public Info info() {
            return info;
        }
    }

    public class LWC {
        private boolean enabled = true;
        private boolean resetLocksOnUnclaim = false;
        private boolean resetLocksOnCapture = false;

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isResetLocksOnUnclaim() {
            return resetLocksOnUnclaim;
        }

        public boolean isResetLocksOnCapture() {
            return resetLocksOnCapture;
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
        private boolean checking = false;
        private boolean buildPriority = false;

        public boolean isChecking() {
            return checking;
        }

        public boolean isBuildPriority() {
            return buildPriority;
        }
    }

    public class WorldBorder {
        @Comment("WorldBorder support\n" +
                "This is for Minecraft's built-in command. To get your current border: /minecraft:worldborder get\n" +
                "A buffer of 0 means faction claims can go right up to the border of the world.\n" +
                "The buffer is in chunks, so 1 as a buffer means an entire chunk of buffer between\n" +
                "the border of the world and what can be claimed to factions")
        private int buffer = 0;

        public int getBuffer() {
            return buffer;
        }
    }

    @Comment("The command base (by default f, making the command /f)")
    private List<String> commandBase = new ArrayList<String>() {
        {
            this.add("f");
        }
    };

    @Comment("FactionsUUID by drtshock\n" +
            "Support and documentation https://factions.support\n" +
            "Updates https://www.spigotmc.org/resources/factionsuuid.1035/\n" +
            "\n" +
            "Made with love <3")
    private AVeryFriendlyFactionsConfig aVeryFriendlyFactionsConfig = new AVeryFriendlyFactionsConfig();

    @Comment("Colors for relationships and default factions")
    private Colors colors = new Colors();
    private Commands commands = new Commands();
    private Factions factions = new Factions();
    @Comment("What should be logged?")
    private Logging logging = new Logging();
    @Comment("Controls certain exploit preventions")
    private Exploits exploits = new Exploits();
    @Comment("Economy support requires Vault and a compatible economy plugin\n" +
            "If you wish to use economy features, be sure to set 'enabled' in this section to true!")
    private Economy economy = new Economy();
    @Comment("Control for the default settings of /f map")
    private MapSettings map = new MapSettings();
    @Comment("Data storage settings")
    private Data data = new Data();
    private RestrictWorlds restrictWorlds = new RestrictWorlds();
    private Scoreboard scoreboard = new Scoreboard();
    @Comment("LWC integration\n" +
            "This support targets the modern fork of LWC, called LWC Extended.\n" +
            "You can find it here: https://www.spigotmc.org/resources/lwc-extended.69551/\n" +
            "Note: Modern LWC is no longer supported, and its former maintainer now runs LWC Extended")
    private LWC lwc = new LWC();
    @Comment("PlayerVaults faction vault settings.\n" +
            "Enable faction-owned vaults!\n" +
            "https://www.spigotmc.org/resources/playervaultsx.51204/")
    private PlayerVaults playerVaults = new PlayerVaults();
    @Comment("WorldGuard settings")
    private WorldGuard worldGuard = new WorldGuard();
    private WorldBorder worldBorder = new WorldBorder();

    public List<String> getCommandBase() {
        return commandBase == null ? ImmutableList.of("f") : Collections.unmodifiableList(commandBase);
    }

    public AVeryFriendlyFactionsConfig getaVeryFriendlyFactionsConfig() {
        return aVeryFriendlyFactionsConfig;
    }

    public Colors colors() {
        return colors;
    }

    public Commands commands() {
        return commands;
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

    public MapSettings map() {
        return map;
    }

    public RestrictWorlds restrictWorlds() {
        return restrictWorlds;
    }

    public Scoreboard scoreboard() {
        return scoreboard;
    }

    public PlayerVaults playerVaults() {
        return playerVaults;
    }

    public WorldGuard worldGuard() {
        return worldGuard;
    }

    public LWC lwc() {
        return lwc;
    }

    public WorldBorder worldBorder() {
        return worldBorder;
    }

    public Data data() {
        return data;
    }
}