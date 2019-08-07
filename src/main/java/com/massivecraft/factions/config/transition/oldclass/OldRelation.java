package com.massivecraft.factions.config.transition.oldclass;

import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.zcore.util.TL;


public enum OldRelation implements OldPermissable {
    MEMBER(4, TL.RELATION_MEMBER_SINGULAR.toString(), Relation.MEMBER),
    ALLY(3, TL.RELATION_ALLY_SINGULAR.toString(), Relation.ALLY),
    TRUCE(2, TL.RELATION_TRUCE_SINGULAR.toString(), Relation.TRUCE),
    NEUTRAL(1, TL.RELATION_NEUTRAL_SINGULAR.toString(), Relation.NEUTRAL),
    ENEMY(0, TL.RELATION_ENEMY_SINGULAR.toString(), Relation.ENEMY);

    public final int value;
    public final String nicename;
    public final Relation replacement;

    OldRelation(final int value, final String nicename, final Relation replacement) {
        this.value = value;
        this.nicename = nicename;
        this.replacement = replacement;
    }

    public Permissible newPermissible() {
        return this.replacement;
    }

    public static OldRelation fromString(String s) {
        // Because Java 6 doesn't allow String switches :(
        if (s.equalsIgnoreCase(MEMBER.nicename)) {
            return MEMBER;
        } else if (s.equalsIgnoreCase(ALLY.nicename)) {
            return ALLY;
        } else if (s.equalsIgnoreCase(TRUCE.nicename)) {
            return TRUCE;
        } else if (s.equalsIgnoreCase(ENEMY.nicename)) {
            return ENEMY;
        } else {
            return NEUTRAL; // If they somehow mess things up, go back to default behavior.
        }
    }

    @Override
    public String toString() {
        return this.nicename;
    }
}
