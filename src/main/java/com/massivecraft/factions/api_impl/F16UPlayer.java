package com.massivecraft.factions.api_impl;

import java.lang.ref.WeakRefrence;

import net.techcable.factionsapi.IFPlayer;
import net.techcable.factionsapi.Rank;
import net.techcable.factionsapi.Relation;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Role;

public class F16UPlayer implements IFPlayer {
    public F16UPlayer(FPlayer player) {
        this.fplayer = new WeakRefrence(player);
    }
    
    private final WeakRefrence<FPlayer> fplayer; 
    public FPlayer getBacking() {
        FPlayer player = fplayer.get();
        if (player == null) throw new RuntimeException("If FPlayer has been GCd we should have been too");
        return player;
    }
    
    /**
     * Get this players uuid
     * 
     * @return this players uuid
     */
    public UUID getId() {
        return UUID.fromString(getBacking().getId());
    }

    public Relation getRelation(IFPlayer rawPlayer) {
        F16UPlayer player = (F16UPlayer) rawPlayer;
        return StructUtil.toAPI(getBacking().getRelationTo(player.getBacking()));
    }

    public Rank getRank() {
        return StructUtil.toAPI(getBacking().getRole());
    }

    public F16UFaction getFaction() {
        Faction faction = getBacking().getFaction();
        return Factions16UPlugin.getInstance().getFaction(faction);
    }

    public boolean canFight(IFPlayer rawPlayer) {
        F16UPlayer player = (F16UPlayer) rawPlayer;
        Relation r = player.getRelation(this);
        switch (r) {
            case SAME :
            case ALLY :
            case TRUCE :
                return false;
            default :
                return true;
        }
    }
}