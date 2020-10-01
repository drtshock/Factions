package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;

import java.util.UUID;

public class CmdShowInvites extends FCommand {

    public CmdShowInvites() {
        super();
        this.aliases.add("showinvites");

        this.requirements = new CommandRequirements.Builder(Permission.SHOW_INVITES)
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FancyMessage msg = new FancyMessage(TL.COMMAND_SHOWINVITES_PENDING.toString()).color(ChatColor.GOLD);
        for (UUID id : context.faction.getInvites()) {
            FPlayer fp = FPlayers.getInstance().getById(id);
            String name = fp != null ? fp.getName() : id.toString();
            msg.then(name + " ").color(ChatColor.WHITE).tooltip(TL.COMMAND_SHOWINVITES_CLICKTOREVOKE.format(name)).command("/" + FactionsPlugin.getInstance().conf().getCommandBase().get(0) + " deinvite " + name);
        }

        context.sendFancyMessage(msg);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SHOWINVITES_DESCRIPTION;
    }


}
