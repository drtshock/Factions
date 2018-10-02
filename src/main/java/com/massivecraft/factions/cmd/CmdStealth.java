package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdStealth extends FCommand {

    public CmdStealth() {
        super();
        this.aliases.add("stealth");

        this.optionalArgs.put("on/off", "flip");

        this.permission = Permission.FLY_STEALTH.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        if (args.size() == 0) {
            fme.setStealth(!fme.isInStealth());
        } else if (args.size() == 1) {
            fme.setStealth(argAsBool(0, true));
        }
        fme.msg(TL.COMMAND_STEALTH_CHANGE, fme.isInStealth() ? "enabled" : "disabled");
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_STEALTH_DESCRIPTION;
    }

}
