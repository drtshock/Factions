package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public abstract class FUpgrade {

    int maxLevel;
    ConfigurationSection configSection;

    private HashMap<Integer, FUpgradeCost> cost = new HashMap<>();

    // Upgrade information
    public abstract String name();

    public abstract String translation();

    public int getMaxLevel() {
        return maxLevel;
    }


    FUpgrade() {
        registerSection();
    }

    public void registerSection() {
        configSection = P.p.getConfig().getConfigurationSection("upgrades.options." + name().toLowerCase());
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
