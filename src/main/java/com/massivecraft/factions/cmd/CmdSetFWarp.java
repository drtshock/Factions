package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Material;

public class CmdSetFWarp extends FCommand {

    public CmdSetFWarp() {
        super();

        this.aliases.add("setwarp");
        this.aliases.add("sw");

        this.requiredArgs.add("warp name");
        this.optionalArgs.put("tag", "tag");
        this.errorOnToManyArgs = false;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
        this.senderMustBePlayer = true;

        this.permission = Permission.SETWARP.node;
    }

    @Override
    public void perform() {
        if (!(fme.getRelationToLocation() == Relation.MEMBER)) {
            fme.msg(TL.COMMAND_SETFWARP_NOTCLAIMED);
            return;
        }

        Access access = myFaction.getAccess(fme, PermissableAction.SETWARP);
        // This statement allows us to check if they've specifically denied it, or default to
        // the old setting of allowing moderators to set warps.
        if (access == Access.DENY || (access == Access.UNDEFINED && !assertMinRole(Role.MODERATOR))) {
            fme.msg(TL.GENERIC_NOPERMISSION, "set warp");
            return;
        }

        int maxWarps = P.p.getConfig().getInt("max-warps", 5);
        if (maxWarps <= myFaction.getWarps().size()) {
            fme.msg(TL.COMMAND_SETFWARP_LIMIT, maxWarps);
            return;
        }

        if (!transact(fme)) {
            return;
        }

        String warp = argAsString(0);

        String materialString = getArgTag("m");
        String password = getArgTag("pw");

        LazyLocation loc = new LazyLocation(fme.getPlayer().getLocation());
        myFaction.setWarp(warp, loc);
        if (password != null) {
            myFaction.setWarpPassword(warp, password);
        }
        if (materialString != null) {
            Material material = Material.matchMaterial(materialString);
            if (material != null) {
                myFaction.setWarpMaterial(warp, material);
            } else {
                fme.msg(TL.COMMAND_SETFWARP_INVALID_MATERIAL, argAsString(2));
            }
        }
        fme.msg(TL.COMMAND_SETFWARP_SET, warp, password != null ? password : "");
    }

    private String getArgTag(String tag) {
        // Skip first arg as it is warp name
        for (int i = 1; i < args.size(); i++) {
            String[] split = args.get(i).split(":");
            if (split.length == 2) {
                if (split[0].equalsIgnoreCase(tag)) {
                    return split[1];
                }
            }
        }
        return null;
    }

    private boolean transact(FPlayer player) {
        return !P.p.getConfig().getBoolean("warp-cost.enabled", false) || player.isAdminBypassing() || payForCommand(P.p.getConfig().getDouble("warp-cost.setwarp", 5), TL.COMMAND_SETFWARP_TOSET.toString(), TL.COMMAND_SETFWARP_FORSET.toString());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SETFWARP_DESCRIPTION;
    }
}
