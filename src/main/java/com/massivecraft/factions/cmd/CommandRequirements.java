package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class CommandRequirements {

    // Permission required to execute command
    public Permission permission;

    // Must be player
    public boolean playerOnly;
    // Must be member of faction
    public boolean memberOnly;

    // Must be atleast this role
    public Role minRole;

    // PermissableAction check if the player has allow for this before checking the role
    public PermissableAction action;

    private CommandRequirements(Permission permission, boolean playerOnly, boolean memberOnly, Role minRole, PermissableAction action) {
        this.permission = permission;
        this.playerOnly = playerOnly;
        this.memberOnly = memberOnly;
        this.minRole = minRole;
        this.action = action;
    }

    public boolean computeRequirements(CommandContext context, boolean informIfNot) {
        // Did not modify CommandRequirements return false and log
        if (permission == null) {
            P.p.log(Level.WARNING, "CommandRequirements not implemented: " + context.alias);
            return true;
        }

        if (context.player != null) {
            // Is Player
            if (!context.fPlayer.hasFaction() && memberOnly) {
                if (informIfNot) {
                    context.sender.sendMessage(ChatColor.YELLOW + "You are not member of any faction.");
                }
                return false;
            }

            if (!P.p.perm.has(context.sender, permission.node, informIfNot)) {
                return false;
            }

            // Permissable Action provided compute that before minRole
            if (action != null) {
                Access access = context.faction.getAccess(context.fPlayer, action);
                if (access == Access.DENY) {
                    if (informIfNot) {
                        context.fPlayer.msg(TL.GENERIC_NOPERMISSION, action.getName());
                    }
                    return false;
                }

                if (access != Access.ALLOW) {
                    // They have undefined assert their role
                    if (minRole != null && !context.fPlayer.getRole().isAtLeast(minRole)) {
                        // They do not fullfill the minRole
                        return false;
                    }
                }
                // They have been explicitly allowed
                return true;
            } else {
                return minRole == null || context.fPlayer.getRole().isAtLeast(minRole);
            }
        } else {
            if (playerOnly) {
                if (informIfNot) {
                    context.sender.sendMessage(TL.GENERIC_PLAYERONLY.toString());
                }
                return false;
            }
            return context.sender.hasPermission(permission.node);
        }
    }

    public static class Builder {

        private Permission permission;

        private boolean playerOnly = false;
        private boolean memberOnly = false;

        private Role minRole = Role.RECRUIT;
        private PermissableAction action;

        public Builder(Permission permission) {
            this.permission = permission;
        }

        public Builder playerOnly() {
            playerOnly = true;
            return this;
        }

        public Builder memberOnly() {
            playerOnly = true;
            memberOnly = true;
            return this;
        }

        public Builder withMinRole(Role role) {
            this.minRole = role;
            return this;
        }

        public Builder withAction(PermissableAction action) {
            this.action = action;
            return this;
        }

        public CommandRequirements build() {
            return new CommandRequirements(permission, playerOnly, memberOnly, minRole, action);
        }

    }

}
