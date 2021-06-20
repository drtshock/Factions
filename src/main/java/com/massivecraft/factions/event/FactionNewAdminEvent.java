package com.massivecraft.factions.event;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;

public class FactionNewAdminEvent extends FactionPlayerEvent {

    public FactionNewAdminEvent(FPlayer p, Faction f) {
        super(f, p);
    }

}
