package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.gui.PermissibleActionGUI;
import com.massivecraft.factions.gui.PermissibleRelationGUI;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CmdPerm extends FCommand {

    public CmdPerm() {
        super();
        this.aliases.add("perm");
        this.aliases.add("perms");
        this.aliases.add("permission");
        this.aliases.add("permissions");

        this.optionalArgs.put("relation", "relation");
        this.optionalArgs.put("action", "action");
        this.optionalArgs.put("access", "access");

        this.requirements = new CommandRequirements.Builder(Permission.PERMISSIONS)
                .memberOnly()
                .withRole(Role.ADMIN)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (context.args.size() == 0) {
            PermissibleRelationGUI ui = new PermissibleRelationGUI(true, context.fPlayer); // TODO offline
            ui.open();
            return;
        } else if (context.args.size() == 1 && getPermissible(context.argAsString(0)) != null) {
            PermissibleActionGUI ui = new PermissibleActionGUI(true, context.fPlayer, getPermissible(context.argAsString(0))); // TODO offline
            ui.open();
            return;
        }

        // If not opening GUI, then setting the permission manually.
        if (context.args.size() != 3) {
            context.fPlayer.msg(TL.COMMAND_PERM_DESCRIPTION);
            return;
        }

        Set<Permissible> permissibles = new HashSet<>();
        Set<PermissibleAction> permissibleActions = new HashSet<>();

        boolean allRelations = context.argAsString(0).equalsIgnoreCase("all");
        boolean allActions = context.argAsString(1).equalsIgnoreCase("all");

        if (allRelations) {
            permissibles.addAll(context.faction.getPermissions().keySet());
        } else {
            Permissible permissible = getPermissible(context.argAsString(0));

            if (permissible == null) {
                context.fPlayer.msg(TL.COMMAND_PERM_INVALID_RELATION);
                return;
            }

            permissibles.add(permissible);
        }

        if (allActions) {
            permissibleActions.addAll(Arrays.asList(PermissibleAction.values()));
        } else {
            PermissibleAction permissibleAction = PermissibleAction.fromString(context.argAsString(1));
            if (permissibleAction == null) {
                context.fPlayer.msg(TL.COMMAND_PERM_INVALID_ACTION);
                return;
            }

            permissibleActions.add(permissibleAction);
        }

        boolean access;
        switch (context.argAsString(2).toLowerCase()) {
            case "allow":
            case "true":
                access = true;
                break;
            case "deny":
            case "false":
                access = false;
                break;
            default:
                context.fPlayer.msg(TL.COMMAND_PERM_INVALID_ACCESS);
                return;
        }

        for (Permissible permissible : permissibles) {
            for (PermissibleAction permissibleAction : permissibleActions) {
                context.fPlayer.getFaction().setPermission(true, permissible, permissibleAction, access);
            }
        }

        context.fPlayer.msg(TL.COMMAND_PERM_SET, context.argAsString(1), access, context.argAsString(0));
        P.p.log(String.format(TL.COMMAND_PERM_SET.toString(), context.argAsString(1), access, context.argAsString(0)) + " for faction " + context.fPlayer.getTag());
    }

    private Permissible getPermissible(String name) {
        if (Role.fromString(name.toUpperCase()) != null) {
            return Role.fromString(name.toUpperCase());
        } else if (Relation.fromString(name.toUpperCase()) != null) {
            return Relation.fromString(name.toUpperCase());
        } else {
            return null;
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_PERM_DESCRIPTION;
    }

}
