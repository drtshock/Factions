package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.zcore.util.TL;

public class CmdRelationNeutral extends FRelationCommand {

    public CmdRelationNeutral() {
        aliases.add("neutral");
        targetRelation = Relation.NEUTRAL;
    }
    
    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RELATIONS_DESCRIPTION;
    }
}
