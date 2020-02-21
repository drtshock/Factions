package com.massivecraft.factions.integration;

import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SuperVanish {

    private static boolean isSetup = false;

    public static boolean isSetup() {
        return isSetup;
    }

    public static boolean isVanished(Player player) {
        if (!isSetup) return false;
        return VanishAPI.isInvisible(player);
    }

    public static List<UUID> getVanishedPlayers() {
        if (!isSetup) return new ArrayList<>();
        return VanishAPI.getInvisiblePlayers();
    }

    public static void setup() {
        if (Bukkit.getServer().getPluginManager().getPlugin("SuperVanish") != null || Bukkit.getServer().getPluginManager().getPlugin("PremiumVanish") != null) isSetup = true;
    }
}
