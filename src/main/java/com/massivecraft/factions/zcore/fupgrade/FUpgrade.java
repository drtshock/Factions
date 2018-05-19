package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import java.util.HashMap;

public abstract class FUpgrade implements Listener {

    private int maxLevel;
    protected ConfigurationSection configSection;

    private HashMap<Integer, FUpgradeCost> cost = new HashMap<>();

    // Upgrade information
    public abstract String id();

    public abstract String translation();

    public int getMaxLevel() {
        return maxLevel;
    }


    FUpgrade() {
        registerSection();
        maxLevel = configSection.getInt("max-level", 3);
    }

    private void registerSection() {
        configSection = P.p.getConfig().getConfigurationSection("upgrades.options." + id().toLowerCase());
    }

    public boolean payFor(int level, FPlayer fme) {
        FUpgradeCost levelCost = cost.get(level);

        // Didn't build price correctly lets return true
        if (levelCost == null) {
            return true;
        // Else make 'em pay
        } else {
            return levelCost.pay(fme);
        }
    }


}
