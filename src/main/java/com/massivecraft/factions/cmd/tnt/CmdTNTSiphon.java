package com.massivecraft.factions.cmd.tnt;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.CommandContext;
import com.massivecraft.factions.cmd.CommandRequirements;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;

import java.util.List;

public class CmdTNTSiphon extends FCommand {
    public CmdTNTSiphon() {
        super();
        this.aliases.add("siphon");
        this.aliases.add("s");
        this.requiredArgs.add("radius");
        this.optionalArgs.put("amount", "all");

        this.requirements = new CommandRequirements.Builder(Permission.TNT_SIPHON).withAction(PermissibleAction.TNTDEPOSIT).memberOnly().build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!context.faction.equals(Board.getInstance().getFactionAt(new FLocation(context.player.getLocation())))) {
            context.msg(TL.COMMAND_TNT_TERRITORYONLY);
            return;
        }
        final int radius = context.argAsInt(0, -1);
        final int amount = context.argAsInt(1, -1);

        if (radius <= 0) {
            context.msg(TL.COMMAND_TNT_SIPHON_FAIL_POSITIVE);
            return;
        }

        if (FactionsPlugin.getInstance().conf().commands().tnt().isAboveMaxStorage(context.faction.getTNTBank() + 1)) {
            context.msg(TL.COMMAND_TNT_SIPHON_FAIL_FULL);
            return;
        }

        if (radius > FactionsPlugin.getInstance().conf().commands().tnt().getMaxRadius()) {
            context.msg(TL.COMMAND_TNT_SIPHON_FAIL_MAXRADIUS, radius, FactionsPlugin.getInstance().conf().commands().tnt().getMaxRadius());
            return;
        }

        List<Dispenser> list = CmdTNTFill.getDispensers(context.player.getLocation(), radius, context.faction.getId());

        int canTake;
        if (FactionsPlugin.getInstance().conf().commands().tnt().getMaxStorage() < 0) {
            canTake = Integer.MAX_VALUE;
        } else {
            canTake = FactionsPlugin.getInstance().conf().commands().tnt().getMaxStorage();
        }

        canTake -= context.faction.getTNTBank();

        if (amount > 0 && amount < canTake) {
            canTake = amount;
        }

        int remaining = canTake;

        for (Dispenser dispenser : list) {
            if (remaining > (64 * 9)) {
                remaining -= CmdTNTFill.getCount(dispenser.getInventory().all(Material.TNT).values());
                dispenser.getInventory().remove(Material.TNT);
            } else {
                remaining = CmdTNTFill.getCount(dispenser.getInventory().removeItem(CmdTNTFill.getStacks(remaining)).values());
            }
            if (remaining == 0) {
                break;
            }
        }

        int acquired = canTake - remaining;

        context.faction.setTNTBank(context.faction.getTNTBank() + acquired);

        context.msg(TL.COMMAND_TNT_SIPHON_MESSAGE, acquired, context.faction.getTNTBank());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TNT_SIPHON_DESCRIPTION;
    }
}
