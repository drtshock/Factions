package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;

public class CmdFWarp extends FCommand {

    public CmdFWarp() {
        super();
        this.aliases.add("warp");
        this.aliases.add("warps");
        this.optionalArgs.put("warpname", "warpname");

        this.permission = Permission.WARP.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        //TODO: check if in combat.
        if (args.size() == 0) {
            FancyMessage msg = new FancyMessage(TL.COMMAND_FWARP_WARPS.toString()).color(ChatColor.GOLD);
            for (String s : myFaction.getWarps().keySet()) {
                msg.then(s + " ").tooltip(TL.COMMAND_FWARP_CLICKTOWARP.toString()).command("f warp " + s).color(ChatColor.WHITE);
            }
            sendFancyMessage(msg);
        } else if (args.size() > 1) {
            fme.msg(TL.COMMAND_FWARP_COMMANDFORMAT.toString());
        } else {
            String warpName = argAsString(0);
            if (myFaction.isWarp(argAsString(0))) {
                if (!transact(fme)) {
                    return;
                }
                fme.getPlayer().teleport(myFaction.getWarp(warpName).getLocation());
                fme.msg(TL.COMMAND_FWARP_WARPED.toString(), warpName);
            } else {
                fme.msg(TL.COMMAND_FWARP_INVALID.toString(), warpName);
            }
        }
    }

    private boolean transact(FPlayer player) {
        return P.p.getConfig().getBoolean("warp-cost.enabled", false) && !player.isAdminBypassing() && Econ.modifyMoney(player, P.p.getConfig().getDouble("warp-cost.warp", 5), TL.COMMAND_FWARP_TOWARP.toString(), TL.COMMAND_FWARP_FORWARPING.toString());
    }
}
