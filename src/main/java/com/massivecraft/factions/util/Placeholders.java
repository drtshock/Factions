package com.massivecraft.factions.util;

import com.massivecraft.factions.P;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class Placeholders {

    private ClipPlaceholderAPIManager clipPlaceholderAPIManager;
    private boolean mvdwPlaceholderAPIManager = false;

    public Placeholders(P p) {
        Plugin clip = p.getServer().getPluginManager().getPlugin("PlaceholderAPI");
        if (clip != null && clip.isEnabled()) {
            this.clipPlaceholderAPIManager = new ClipPlaceholderAPIManager();
            if (this.clipPlaceholderAPIManager.register()) {
                p.log(Level.INFO, "Successfully registered placeholders with PlaceholderAPI.");
            }
        }

        Plugin mvdw = p.getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI");
        if (mvdw != null && mvdw.isEnabled()) {
            this.mvdwPlaceholderAPIManager = true;
            p.log(Level.INFO, "Found MVdWPlaceholderAPI. Adding hooks.");
        }
    }


    public boolean isClipPlaceholderAPIHooked() {
        return this.clipPlaceholderAPIManager != null;
    }

    public boolean isMVdWPlaceholderAPIHooked() {
        return this.mvdwPlaceholderAPIManager;
    }

}
