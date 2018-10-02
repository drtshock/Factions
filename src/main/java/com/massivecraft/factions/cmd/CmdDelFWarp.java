package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.tabcomplete.TabCompleteProvider;
import com.massivecraft.factions.cmd.tabcomplete.providers.ProviderFactionWarps;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CmdDelFWarp extends FCommand {

    public CmdDelFWarp() {
        super();
        this.aliases.add("delwarp");
        this.aliases.add("dw");
        this.aliases.add("deletewarp");
        this.requiredArgs.add("warp name");
        this.senderMustBeMember = true;
        this.senderMustBeModerator = true;
        this.senderMustBePlayer = true;
        this.permission = Permission.SETWARP.node;
    }

    @Override
    public void perform() {
        String warp = argAsString(0);
        if (myFaction.isWarp(warp)) {
            if (!transact(fme)) {
                return;
            }
            myFaction.removeWarp(warp);
            fme.msg(TL.COMMAND_DELFWARP_DELETED, warp);
        } else {
            fme.msg(TL.COMMAND_DELFWARP_INVALID, warp);
        }
    }

    private boolean transact(FPlayer player) {
        return !P.p.getConfig().getBoolean("warp-cost.enabled", false) || player.isAdminBypassing() || payForCommand(P.p.getConfig().getDouble("warp-cost.delwarp", 5), TL.COMMAND_DELFWARP_TODELETE.toString(), TL.COMMAND_DELFWARP_FORDELETE.toString());
    }

    @Override
    public TabCompleteProvider onTabComplete(Player player, String[] args) {
        if (args.length == 1) {
            return new ProviderFactionWarps(FPlayers.getInstance().getByPlayer(player).getFaction());
        }
        return super.onTabComplete(player, args);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_DELFWARP_DESCRIPTION;
    }
}
