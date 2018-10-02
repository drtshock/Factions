package com.massivecraft.factions.cmd.tabcomplete.providers;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.tabcomplete.TabCompleteProvider;

import java.util.Collections;
import java.util.List;

public class ProviderFactionWarps implements TabCompleteProvider {

    private Faction faction;

    public ProviderFactionWarps(Faction faction) {
        this.faction = faction;
    }

    @Override
    public List<String> get() {
        return Collections.list(faction.getWarps().keys());
    }
}
