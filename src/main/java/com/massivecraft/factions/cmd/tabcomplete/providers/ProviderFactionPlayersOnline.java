package com.massivecraft.factions.cmd.tabcomplete.providers;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.tabcomplete.TabCompleteProvider;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ProviderFactionPlayersOnline implements TabCompleteProvider {

    private Faction faction;

    public ProviderFactionPlayersOnline(Faction faction) {
        this.faction = faction;
    }

    @Override
    public List<String> get() {
        List<String> players = new ArrayList<>();
        for (Player online : faction.getOnlinePlayers()) {
            players.add(online.getName());
        }
        return players;
    }

}
