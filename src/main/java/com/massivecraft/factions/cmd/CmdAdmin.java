package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.event.FPlayerJoinEvent;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.MiscUtil;
import com.massivecraft.factions.zcore.util.TL;

import org.bukkit.Bukkit;

public class CmdAdmin extends FCommand {

    public CmdAdmin() {
        super();
        this.aliases.add("admin");

        this.requiredArgs.add("player name");
        //this.optionalArgs.put("", "");

        this.permission = Permission.ADMIN.node;
        this.disableOnLock = true;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        FPlayer fyou = this.argAsBestFPlayerMatch(0);
        if (fyou == null) {
            return;
        }

        boolean permAny = Permission.ADMIN_ANY.has(sender, false);
        Faction targetFaction = fyou.getFaction();
    	values.put("targetplayer", fyou.describeTo(fme));
    	values.put("admin", senderIsConsole ? TL.A_SERVER_ADMIN.toString() : fme.describeTo(fyou));
    	values.put("adminUC", MiscUtil.capitalizeFirstLetter(values.get("admin")));

        if (targetFaction != myFaction && !permAny) {
            TLmsg(TL.CMD_ADMIN_NOT_MEMBER, values);
            return;
        }

        if (fme != null && fme.getRole() != Role.ADMIN && !permAny) {
            TLmsg(TL.CMD_ADMIN_NOT_ADMIN, values);
            return;
        }

        if (fyou == fme && !permAny) {
            TLmsg(TL.CMD_ADMIN_TARGET_SELF, values);
            return;
        }

        // only perform a FPlayerJoinEvent when newLeader isn't actually in the faction
        if (fyou.getFaction() != targetFaction) {
            FPlayerJoinEvent event = new FPlayerJoinEvent(FPlayers.i.get(me), targetFaction, FPlayerJoinEvent.PlayerJoinReason.LEADER);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }

        FPlayer admin = targetFaction.getFPlayerAdmin();

        // if target player is currently admin, demote and replace him
        if (fyou == admin) {
            targetFaction.promoteNewLeader();
            TLmsg(TL.CMD_ADMIN_DEMOTE_SELFMSG, values);
            fyou.TLmsg(TL.CMD_ADMIN_DEMOTE_OTHERMSG, values);
            return;
        }

        // promote target player, and demote existing admin if one exists
        if (admin != null) {
            admin.setRole(Role.MODERATOR);
        }
        fyou.setRole(Role.ADMIN);
        TLmsg(TL.CMD_ADMIN_PROMOTE, values);

        // Inform all players
        for (FPlayer fplayer : FPlayers.i.getOnline()) {
        	values.put("targetfaction", targetFaction.describeTo(fplayer));
            fplayer.TLmsg(TL.CMD_ADMIN_LEADERSHIP, values);
        }
    }

}
