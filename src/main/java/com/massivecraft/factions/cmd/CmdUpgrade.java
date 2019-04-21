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

        this.optionalArgs.put("upgrade", "gui");
        this.optionalArgs.put("action", "info");

        this.requirements = new CommandRequirements.Builder(Permission.UPGRADE)
                .memberOnly()
                .withRole(Role.MODERATOR)
                .build();
    }

    @Override

    public void perform(CommandContext context) {
        if (context.args.size() >= 1) {
            FUpgrade upgrade = P.p.factionUpgrades.getUpgrade(context.args.get(0));
            if (upgrade == null) {
                context.fPlayer.msg(TL.COMMAND_UPGRADE_INVALID, context.args.get(0));
                return;
            }

            if (context.args.size() == 1) {
                context.fPlayer.msg(TL.COMMAND_UPGRADE_LEVEL_CURRENT, upgrade.translation(), context.faction.getUpgradeLevel(upgrade.id()));
            } else if (context.args.size() == 2) {
                if (context.args.get(1).equals("levelup")) {
                    context.faction.levelUpUpgrade(upgrade.id(), context.fPlayer);
                }
            }
        } else {
            // TODO: Implement GUI
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_UPGRADE_DESCRIPTION;
    }

}
