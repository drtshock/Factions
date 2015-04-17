package com.massivecraft.factions.api_impl;

import net.techcable.factionsapi.IFaction;
import net.techcable.factionsapi.IFPlayer;
import net.techcable.factionsapi.Rank;
import net.techcable.factionsapi.Relation;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Role;

public class F16UFaction implements IFaction {
    public F16UFaction(Faction faction) {
        this.faction = new WeakRefrence(faction);
    }
    
    private final WeakRefrence<FPlayer> faction; 
    public Faction getBacking() {
        FPlayer faction = this.faction.get();
        if (faction == null) throw new RuntimeException("If Faction has been GCd we should have been too");
        return faction;
    }
    
    public String getId() {
        return getBacking().getId();
    }
    
    public String getName() {
        return getBacking().getTag();
    }
    
    public String getDescription() {
        return getBacking().getDescription();
    }
    
    public boolean isSafezone() {
        return getBacking().isSafeZone();
    }
    
    public boolean isWarzone() {
        return getBacking().isWarZone();
    }
    
    public boolean isNone() {
        return getBacking().isNone();
    }
    
    public F16UPlayer getOwner() {
        FPlayer owner = getBacking().getFPlayerAdmin();
        return Factions16UPlugin.getInstance().getFPlayer(owner);
    }
    
    public ImmutableCollection<F16UPlayer> getMembers(Rank rank) {
        Role role = StructUtil.fromAPI(rank);
        ImmutableList.Builder<F16UPlayer> members = ImmutableList.builder();
        for (FPlayer raw : getBacking().getFPlayersWhereRole(role)) {
            members.add(Factions16UPlugin.getInstance().getFPlayer(raw));
        }
        return members.build();
    }
    
    public ImmutableCollection<F16UPlayer> getMembers() {
        ImmutableList.Builder<F16UPlayer> members = ImmutableList.builder();
        for (FPlayer raw : getBacking().getFPlayers()) {
            members.add(Factions16UPlugin.getInstance().getFPlayer(raw));
        }
        return members.build();
    }
    
    public ImmutableCollection<F16UFaction> getFactions(Relation rawRelation) {
        com.massivecraft.factions.struct.Relation relation = StructUtil.fromAPI(rawRelation);
        ImmutableList.Builder<F16UFaction> factions = ImmutableList.builder();
        for (Faction raw : Factions.getInstance().getAllFactions()) {
            if (!getBacking().getRelationTo(raw).equals(relation)) continue; //Not the relation we want
            F16UFaction faction = Factions16UPlugin.getInstance().getFaction(raw);
            factions.add(faction);
        }
        return factions.build();
    }
    
    public Relation getRelation(IFaction raw) {
        F16UFaction faction = (F16UFaction) raw;
        return StructUtil.toAPI(getBacking().getRelationTo(faction.getBacking()));
    }

    public Relation getRelation(IFPlayer raw) {
        F16UPlayer player = (F16UPlayer) raw;
        return StructUtil.toAPI(getBacking().getRelationTo(player.getBacking()));
    }

    public boolean isOffline() {
        return getBacking().getOnlinePlayers().size() == 0;
    }

    public double getPower() {
        return getBacking().getPower();
    }

    public double getBalance() {
        if (!Econ.shouldBeUsed()) return 0;
        return Econ.getBalance(getBacking().getAccountId());
    }

    public void setBalance(double balance) {
        if (!Econ.shouldBeUsed()) return;
        Econ.setBalance(getBacking().getAccountId(), balance);
    }
    private Faction16USettings settings;
    @Override
    public Faction16USettings getSettings() {
        if (settings == null) {
            settings = new Faction16USettings(this);
        }
        return settings;
    }
}