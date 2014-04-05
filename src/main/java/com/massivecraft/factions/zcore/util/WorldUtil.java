package com.massivecraft.factions.zcore.util;

import org.bukkit.Bukkit;

import java.io.File;

public class WorldUtil {
    // Previously We had crappy support for multiworld management.
    // This should however be handled by an external plugin!
    /*public static boolean load(String name) {
        if (isWorldLoaded(name)) {
			return true;
		}
		
		if ( ! doesWorldExist(name)) {
			return false;
		}
		
		Environment env = WorldEnv.get(name);
		if (env == null) {
			FactionsPlugin.log(Level.WARNING, "Failed to load world. Environment was unknown.");
			return false;
		}
		
		FactionsPlugin.plugin.getServer().createWorld(name, env);
		return true;
	}*/

    public static boolean isWorldLoaded(String name) {
        return Bukkit.getServer().getWorld(name) != null;
    }

    public static boolean doesWorldExist(String name) {
        return new File(name, "level.dat").exists();
    }
}
