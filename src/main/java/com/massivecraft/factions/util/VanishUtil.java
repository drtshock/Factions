package com.massivecraft.factions.util;

import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.integration.SuperVanish;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public class VanishUtil {

    public static boolean isVanished(Player player) {
        if (player == null) return false;
        if (Essentials.getEssentials() != null && Essentials.isVanished(player)) return true;
        if (SuperVanish.isSetup() && SuperVanish.isVanished(player)) return true;
        return hasVanishedMetadata(player);
    }

    public static boolean isVanished(Player player, Player target) {
        if (isVanished(target)) return true;
        return (!player.canSee(target));
    }

    private static boolean hasVanishedMetadata(Player player) {
        if (player == null) return false;
        if (!player.hasMetadata("vanished")) {
            return false;
        }
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta == null) continue;
            if (meta.asBoolean()) {
                return true;
            }
        }
        return false;
    }

}


