package com.massivecraft.factions.cmd.tabcomplete;

import org.bukkit.entity.Player;

public interface FactionTabCompleter {

    TabCompleteProvider onTabComplete(Player player, String[] args);

}
