package com.massivecraft.factions.api_impl;

import net.techcable.factionsapi.flags.FactionSettings;

import com.massivecraft.factions.Conf;

public class Faction16USettings implements FactionSettings {
    public Faction16USettings(F16UFaction faction) {
        this.faction = faction;
    }
    
    private final F16UFaction faction;
    
    public boolean isOpen() {
        return faction.getBacking().getOpen();
    }
    
    public boolean canMonstersSpawn() {
        return faction.getBacking().noMonstersInTerritory();
    }

    public boolean isPvpAllowed() {
        return !faction.getBacking().noPvPInTerritory();
    }

    public boolean isLoosePowerOnDeath() {
        return !faction.getBacking().isPowerFrozen();
    }

    public boolean isFriendlyFireAllowed() {
        return faction.isWarzone() && Conf.warZoneFriendlyFire;
    }

    public boolean isExplosionsAllowed() {
        return faction.getBacking().noExplosionsInTerritory();
    }

    public boolean isOfflineExplosions() {
        return !Conf.territoryBlockTNTWhenOffline;
    }

    public boolean isEndermanGrief() {
        return !Conf.territoryDenyEndermanBlocks;
    }


    public boolean isPermanent();

    public boolean isPeaceful();

    public boolean hasInfinitePower();
}