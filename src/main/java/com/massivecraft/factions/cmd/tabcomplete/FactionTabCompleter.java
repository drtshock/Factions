package com.massivecraft.factions.cmd.tabcomplete;

import com.massivecraft.factions.cmd.CommandContext;

public interface FactionTabCompleter {

    TabCompleteProvider onTabComplete(CommandContext context, String[] args);

}
