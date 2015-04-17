package com.massivecraft.factions.api_impl;

import net.techcable.factionsapi.Rank;

import com.massivecraft.factions.struct.Role;

public class StructUtil {
    private StructUtil() {}
    
    public static Rank toAPI(Role role) {
        switch (role) {
            case ADMIN :
                return Rank.OWNER;
            case MODERATOR :
                return Rank.MODERATOR;
            default :
                return Rank.MEMBER;
        }
    }
    
    public static Role fromAPI(Rank role) {
        switch (role) {
            case OWNER :
                return Role.ADMIN;
            case MODERATOR :
                return Role.MODERATOR;
            default :
                return Role.NORMAL;
        }
    }
    
    public static Relation toAPI(com.massivecraft.factions.struct.Relation massive) {
        switch (relaiton) {
            case MEMBER :
                return Relation.SAME;
            case ALLY :
                return Relation.ALLY;
            case TRUCE :
                return Relation.TRUCE;
            case ENEMY :
                return Relation.ENEMY;
            default :
                return Relation.NEUTRAL;
        }
    }
    
    public static Relation fromAPI(Relation apiRelation) {
        switch (apiRelation) {
            case SAME :
                return com.massivecraft.factions.struct.Relation.MEMBER;
            case ALLY :
                return com.massivecraft.factions.struct.Relation.ALLY;
            case TRUCE :
                return com.massivecraft.factions.struct.Relation.TRUCE;
            case ENEMY :
                return com.massivecraft.factions.struct.Relation.ENEMY;
            default :
                return com.massivecraft.factions.struct.Relation.NEUTRAL;
        }
    }
}