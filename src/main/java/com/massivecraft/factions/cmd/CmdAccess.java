package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.struct.FactionEntity;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;

import javax.swing.text.html.parser.Entity;

public class CmdAccess extends FCommand {

    public CmdAccess() {
        super();

        this.aliases.add("access");

        this.requiredArgs.add("add/remove");
        this.requiredArgs.add("p/f/player/faction");
        this.requiredArgs.add("name");

        this.requirements = new CommandRequirements.Builder(Permission.ACCESS)
                .memberOnly()
                .withRole(Role.MODERATOR)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        FLocation loc = context.fPlayer.getLastStoodAt();
        if (Board.getInstance().getFactionAt(loc) != context.faction) {
            context.msg(TL.COMMAND_ACCESS_NOT_OWN);
            return;
        }

        FactionEntity entity = getEntity(context);
        if (entity == null) {
            return;
        }

        if (context.argAsString(0).equalsIgnoreCase("add")) {
            context.faction.addChunkAccessAt(loc, entity);
            context.faction.msg(TL.COMMAND_ACCESS_GRANTED, loc.getX(), loc.getZ(), entity.getName());
        } else if (context.argAsString(0).equalsIgnoreCase("remove")) {
            context.faction.removeChunkAccessAt(loc, entity);
            context.faction.msg(TL.COMMAND_ACCESS_REVOKED, loc.getX(), loc.getZ(), entity.getName());
        } else {
            context.msg(TL.COMMAND_ACCESS_INVALID_ACTION);
        }
    }

    private FactionEntity getEntity(CommandContext context) {
        String type = context.argAsString(1);
        FactionEntity entity = null;
        if (type.equalsIgnoreCase("f") || type.equalsIgnoreCase("faction")) {
            entity = context.argAsFaction(2);
            if (entity == null) {
                context.msg(TL.COMMAND_ACCESS_NOT_FOUND, "faction", context.argAsString(1));
            }
        } else if (type.equalsIgnoreCase("p") || type.equalsIgnoreCase("player")) {
            entity = context.argAsBestFPlayerMatch(2);
            if (entity == null) {
                context.msg(TL.COMMAND_ACCESS_NOT_FOUND, "player", context.argAsString(1));
            }
        } else {
            context.msg(TL.COMMAND_ACCESS_INVALID_TYPE);
        }
        return entity;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_ACCESS_DESCRIPTION;
    }
}
