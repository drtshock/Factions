package com.massivecraft.factions.zcore.fperms;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Permissable {

    public ItemStack buildItem(String displayName, List<String> displayLore);

    public String replacePlaceholders(String string);

}
