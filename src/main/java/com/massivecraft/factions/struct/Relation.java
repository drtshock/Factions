package com.massivecraft.factions.struct;

import com.massivecraft.factions.P;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;


public enum Relation implements Permissible {
    MEMBER(4, TL.RELATION_MEMBER_SINGULAR.toString()),
    ALLY(3, TL.RELATION_ALLY_SINGULAR.toString()),
    TRUCE(2, TL.RELATION_TRUCE_SINGULAR.toString()),
    NEUTRAL(1, TL.RELATION_NEUTRAL_SINGULAR.toString()),
    ENEMY(0, TL.RELATION_ENEMY_SINGULAR.toString());

    public final int value;
    public final String nicename;

    Relation(final int value, final String nicename) {
        this.value = value;
        this.nicename = nicename;
    }

    public static Relation fromString(String s) {
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

    public String getTranslation() {
        try {
            return TL.valueOf("RELATION_" + name() + "_SINGULAR").toString();
        } catch (IllegalArgumentException e) {
            return toString();
        }
    }

    public String getPluralTranslation() {
        for (TL t : TL.values()) {
            if (t.name().equalsIgnoreCase("RELATION_" + name() + "_PLURAL")) {
                return t.toString();
            }
        }
        return toString();
    }

    public boolean isMember() {
        return this == MEMBER;
    }

    public boolean isAlly() {
        return this == ALLY;
    }

    public boolean isTruce() {
        return this == TRUCE;
    }

    public boolean isNeutral() {
        return this == NEUTRAL;
    }

    public boolean isEnemy() {
        return this == ENEMY;
    }

    public boolean isAtLeast(Relation relation) {
        return this.value >= relation.value;
    }

    public boolean isAtMost(Relation relation) {
        return this.value <= relation.value;
    }

    public ChatColor getColor() {
        switch (this) {
            case MEMBER:
                return P.p.conf().colors().relations().getMember();
            case ALLY:
                return P.p.conf().colors().relations().getAlly();
            case NEUTRAL:
                return P.p.conf().colors().relations().getNeutral();
            case TRUCE:
                return P.p.conf().colors().relations().getTruce();
            default:
                return P.p.conf().colors().relations().getEnemy();
        }
    }

    public double getRelationCost() {
        if (isEnemy()) {
            return P.p.conf().economy().getCostEnemy();
        } else if (isAlly()) {
            return P.p.conf().economy().getCostAlly();
        } else if (isTruce()) {
            return P.p.conf().economy().getCostTruce();
        } else {
            return P.p.conf().economy().getCostNeutral();
        }
    }
}
