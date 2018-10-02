package com.massivecraft.factions.cmd.tabcomplete.providers;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.tabcomplete.TabCompleteProvider;

import java.util.ArrayList;
import java.util.List;

public class ProviderFactionPlayers implements TabCompleteProvider {

    private Faction faction;

    public ProviderFactionPlayers(Faction faction) {
        this.faction = faction;
    }

    @Override
    public List<String> get() {
        List<String> players = new ArrayList<>();
        for (FPlayer player : faction.getFPlayers()) {
            players.add(player.getName());
        }
        return players;
    }

}
