package com.massivecraft.factions.cmd;

import org.bukkit.Bukkit;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.integration.dynmap.EngineDynmap;
import com.massivecraft.factions.struct.Permission;

public class CmdReload extends FCommand {

    public CmdReload() {
        super();
        this.aliases.add("reload");

        //this.requiredArgs.add("");
        this.optionalArgs.put("file", "all");

        this.permission = Permission.RELOAD.node;
        this.disableOnLock = false;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        long timeInitStart = System.currentTimeMillis();
        Conf.load();
        P.p.reloadConfig();

        long timeReload = (System.currentTimeMillis() - timeInitStart);

        msg("<i>Reloaded <h>conf.json <i>from disk, took <h>%dms<i>.", timeReload);
    }
}
