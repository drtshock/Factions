package com.massivecraft.factions.config.file;

import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.config.annotation.ConfigName;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;

public class DefaultPermissionsConfig {
    public static class Permissions {
        public class PermissiblePermInfo {
            public boolean isLocked() {
                return this.locked;
            }

            public boolean defaultAllowed() {
                return this.value;
            }

            private boolean locked = false;
            @ConfigName("default")
            private boolean value = false;
        }

        public class FactionOnlyPermInfo {
            protected PermissiblePermInfo coleader = new PermissiblePermInfo();
            protected PermissiblePermInfo moderator = new PermissiblePermInfo();
            protected PermissiblePermInfo normal = new PermissiblePermInfo();
            protected PermissiblePermInfo recruit = new PermissiblePermInfo();

            public PermissiblePermInfo get(Permissible permissible) {
                if (permissible instanceof Role) {
                    switch ((Role) permissible) {
                        case COLEADER:
                            return this.coleader;
                        case MODERATOR:
                            return this.moderator;
                        case NORMAL:
                            return this.normal;
                        case RECRUIT:
                            return this.recruit;
                    }
                }
                // TODO print warning
                return null;
            }
        }

        public class FullPermInfo extends FactionOnlyPermInfo {
            protected PermissiblePermInfo ally = new PermissiblePermInfo();
            protected PermissiblePermInfo truce = new PermissiblePermInfo();
            protected PermissiblePermInfo neutral = new PermissiblePermInfo();
            protected PermissiblePermInfo enemy = new PermissiblePermInfo();

            public PermissiblePermInfo get(Permissible permissible) {
                if (permissible instanceof Relation) {
                    switch ((Relation) permissible) {
                        case ALLY:
                            return this.ally;
                        case TRUCE:
                            return this.truce;
                        case NEUTRAL:
                            return this.neutral;
                        case ENEMY:
                            return this.enemy;
                    }
                }
                return super.get(permissible);
            }
        }

        public FactionOnlyPermInfo getBan() {
            return this.ban;
        }

        public FullPermInfo getBuild() {
            return this.build;
        }

        public FullPermInfo getDestroy() {
            return this.destroy;
        }

        public FullPermInfo getFrostWalk() {
            return this.frostWalk;
        }

        public FullPermInfo getPainBuild() {
            return this.painBuild;
        }

        public FullPermInfo getDoor() {
            return this.door;
        }

        public FullPermInfo getButton() {
            return this.button;
        }

        public FullPermInfo getLever() {
            return this.lever;
        }

        public FullPermInfo getContainer() {
            return this.container;
        }

        public FactionOnlyPermInfo getInvite() {
            return this.invite;
        }

        public FactionOnlyPermInfo getKick() {
            return this.kick;
        }

        public FullPermInfo getItem() {
            return this.item;
        }

        public FactionOnlyPermInfo getSetHome() {
            return this.sethome;
        }

        public FactionOnlyPermInfo getEconomy() {
            return this.economy;
        }

        public FactionOnlyPermInfo getTerritory() {
            return this.territory;
        }

        public FullPermInfo getPlate() {
            return this.plate;
        }

        public FactionOnlyPermInfo getDisband() {
            return this.disband;
        }

        public FactionOnlyPermInfo getPromote() {
            return this.promote;
        }

        public FactionOnlyPermInfo getSetWarp() {
            return this.setwarp;
        }

        public FullPermInfo getWarp() {
            return this.warp;
        }

        public FullPermInfo getFly() {
            return this.fly;
        }

        @Comment("Can ban others from the faction")
        private FactionOnlyPermInfo ban = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can build in faction territory (while not raidable)")
        private FullPermInfo build = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can destroy in faction territory (while not raidable)")
        private FullPermInfo destroy = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Can frost walk in faction territory (while not raidable)")
        private FullPermInfo frostWalk = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Gets hurt for trying to build or destroy in faction territory (while not raidable)")
        private FullPermInfo painBuild = new FullPermInfo() {
            {
                this.enemy.value = true;
            }
        };
        @Comment("Use doors in faction territory (while not raidable)")
        private FullPermInfo door = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Use buttons in faction territory (while not raidable)")
        private FullPermInfo button = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Use levers in faction territory (while not raidable)")
        private FullPermInfo lever = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Use containers in faction territory (while not raidable)")
        private FullPermInfo container = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
            }
        };
        @Comment("Able to invite others to the faction")
        private FactionOnlyPermInfo invite = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        private FactionOnlyPermInfo kick = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Use items in faction territory (while not raidable)")
        private FullPermInfo item = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Can set the faction home")
        private FactionOnlyPermInfo sethome = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can access faction economy")
        private FactionOnlyPermInfo economy = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
            }
        };
        @Comment("Can claim/unclaim faction territory")
        private FactionOnlyPermInfo territory = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can interact with plates")
        private FullPermInfo plate = new FullPermInfo();
        private FactionOnlyPermInfo disband = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
            }
        };
        @Comment("Can promote members up to their own role within the faction")
        private FactionOnlyPermInfo promote = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can set a faction warp")
        private FactionOnlyPermInfo setwarp = new FactionOnlyPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
            }
        };
        @Comment("Can use faction warps")
        private FullPermInfo warp = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
        @Comment("Can fly in faction territory")
        private FullPermInfo fly = new FullPermInfo() {
            {
                this.coleader.value = true;
                this.moderator.value = true;
                this.normal.value = true;
                this.recruit.value = true;
                this.ally.value = true;
            }
        };
    }

    @Comment("Permissions settings\n" +
            "Each main section represents one permission.\n" +
            "Inside is each relation.\n" +
            "Each relation has a default value (true=allowed, false=disallowed)\n" +
            "  and true/false for if it's locked to editing by factions admins.")
    private Permissions permissions = new Permissions();

    public Permissions getPermissions() {
        return this.permissions;
    }
}