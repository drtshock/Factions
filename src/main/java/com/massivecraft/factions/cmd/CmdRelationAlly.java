package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.zcore.util.TL;

public class CmdRelationAlly extends FRelationCommand {

    public CmdRelationAlly() {
        aliases.add("ally");
        targetRelation = Relation.ALLY;
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RELATIONS_DESCRIPTION;
    }
}
