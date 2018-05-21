package com.massivecraft.factions.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fupgrade.FUpgrade;
import com.massivecraft.factions.zcore.util.TL;

public class CmdUpgrade extends FCommand {

    public CmdUpgrade() {
        this.aliases.add("upgrade");
        this.aliases.add("upgrades");

        this.optionalArgs.put("action", "gui");
        this.optionalArgs.put("upgrade", "upgrade");


        this.requirements = new CommandRequirements.Builder(Permission.UPGRADE)
                .memberOnly()
                .withRole(Role.MODERATOR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (context.args.size() == 2) {
            FUpgrade upgrade = P.p.factionUpgrades.getUpgrade(context.args.get(1));
            if (upgrade == null) {
                context.msg(TL.COMMAND_UPGRADE_INVALID, context.args.get(1));
                return;
            }

            if (context.args.get(0).equals("levelup")) {
                context.faction.levelUpUpgrade(upgrade.id(), context.fPlayer);
            }
        }

        // TODO: Implement GUI
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_UPGRADE_DESCRIPTION;
    }

}
