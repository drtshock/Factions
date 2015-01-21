package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.zcore.util.TL;

public class CmdRelationEnemy extends FRelationCommand {

    public CmdRelationEnemy() {
        aliases.add("enemy");
        targetRelation = Relation.ENEMY;
    }
    
    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_RELATIONS_DESCRIPTION;
    }
}
