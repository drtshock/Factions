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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class CmdTNTDeposit extends FCommand {
    public CmdTNTDeposit() {
        super();
        this.aliases.add("deposit");
        this.aliases.add("d");
        this.requiredArgs.add("amount");

        this.requirements = new CommandRequirements.Builder(Permission.TNT_DEPOSIT).withAction(PermissibleAction.TNTDEPOSIT).memberOnly().build();
    }

    @Override
    public void perform(CommandContext context) {
        Player player = context.player;
        if (!context.faction.equals(Board.getInstance().getFactionAt(new FLocation(player.getLocation())))) {
            context.msg(TL.COMMAND_TNT_TERRITORYONLY);
            return;
        }
        int amount = context.argAsInt(0, -1);
        if (amount <= 0) {
            context.msg(TL.COMMAND_TNT_DEPOSIT_FAIL_POSITIVE, amount);
            return;
        }

        if (!player.getInventory().containsAtLeast(new ItemStack(Material.TNT), amount)) {
            context.msg(TL.COMMAND_TNT_DEPOSIT_FAIL_NOTENOUGH, amount);
            return;
        }

        if (FactionsPlugin.getInstance().conf().commands().tnt().isAboveMaxStorage(context.faction.getTNTBank() + amount)) {
            if (FactionsPlugin.getInstance().conf().commands().tnt().getMaxStorage() == context.faction.getTNTBank()) {
                context.msg(TL.COMMAND_TNT_DEPOSIT_FAIL_FULL, FactionsPlugin.getInstance().conf().commands().tnt().getMaxStorage());
                return;
            }
            amount = FactionsPlugin.getInstance().conf().commands().tnt().getMaxStorage() - context.faction.getTNTBank();
        }
        int current = amount;
        Map<Integer, ? extends ItemStack> all = player.getInventory().all(Material.TNT);
        for (Map.Entry<Integer, ? extends ItemStack> entry : all.entrySet()) {
            final int count = entry.getValue().getAmount();
            final int newCount = Math.max(0, count - current);
            current -= (count - newCount);
            if (newCount == 0) {
                player.getInventory().setItem(entry.getKey(), null);
            } else {
                player.getInventory().getItem(entry.getKey()).setAmount(newCount);
            }
            if (current == 0) {
                break;
            }
        }
        context.faction.setTNTBank(context.faction.getTNTBank() + amount);
        context.msg(TL.COMMAND_TNT_DEPOSIT_SUCCESS, context.faction.getTNTBank());
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TNT_DEPOSIT_DESCRIPTION;
    }
}
