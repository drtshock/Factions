package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;

public class CmdFly extends FCommand {

    public CmdFly() {
        super();

        this.optionalArgs.put("on/off", "on/off");

        this.permission = Permission.FLIGHT.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        if (!Conf.enableFactionFlight) {
            // Flight is disabled
            return;
        }

        if (args.size() == 0) {
            setAutoFly(!fme.isAutoFFlying());
            return;
        }

        setAutoFly(argAsBool(0));
    }

    private void setAutoFly(boolean toggle) {
        fme.setAutoFFlying(toggle);
        fme.msg(TL.COMMAND_AUTOFLIGHT_CHANGE, toggle ? "enabled" : "disabled");
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY_DESCRIPTION;
    }

}
