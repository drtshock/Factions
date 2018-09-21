package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class FUpgrade implements Listener {

    protected FUpgradeRoot factionUpgrades;

    protected int maxLevel;
    protected ConfigurationSection configSection;
    protected HashMap<Integer, FUpgradeCost> cost = new HashMap<>();
    protected HashMap<Integer, ConfigurationSection> levelConfigs = new HashMap<>();

    // Upgrade information
    public abstract String id();
    public abstract String translation();


    public FUpgrade(FUpgradeRoot factionUpgrades) {
        this.factionUpgrades = factionUpgrades;
        configSection = P.p.getConfig().getConfigurationSection("upgrades.options." + id().toLowerCase());
        if (configSection == null || !configSection.getBoolean("enable", false)) {
            disable(false);
            return;
        }
        maxLevel = configSection.getInt("max-level", 3);

        for (String levelStr : configSection.getConfigurationSection("attributes").getKeys(false)) {
            Integer level;
            try {
                level = Integer.parseInt(levelStr);
            } catch (NumberFormatException e) {
                // Failed to convert key to Integer, skipping it
                continue;
            }
            levelConfigs.put(level, configSection.getConfigurationSection("attributes." + levelStr));
        }

        // Make sure all attributes have loaded correctly if not disable this upgrade
        for (int i = 1; i <= maxLevel; i++) {
            if (!levelConfigs.containsKey(i)) {
                P.p.log(Level.WARNING, "Missing attributes for Upgrade: " + id());
                disable(true);
            }
        }

        // Now lets load the costs
        for (Map.Entry<Integer, ConfigurationSection> attrs : levelConfigs.entrySet()) {
            ConfigurationSection levelCost = attrs.getValue().getConfigurationSection("cost");

            if (levelCost == null) {
                cost.put(attrs.getKey(), new FUpgradeCost());
                continue;
            }

            FUpgradeCost fUpgradeCost = new FUpgradeCost();
            for (String key : levelCost.getKeys(false)) {
                FUpgradeCost.CostType costType = FUpgradeCost.CostType.fromString(key);
                double value = levelCost.getDouble(key);

                if (costType == null) {
                    continue;
                }

                fUpgradeCost.put(costType, value);
            }
            cost.put(attrs.getKey(), fUpgradeCost);
        }
        // Everything is ready, let the upgrade handle the rest
        registerAttributes();
    }

    protected abstract void registerAttributes();

    public boolean pay(int level, FPlayer fme) {
        FUpgradeCost levelCost = cost.get(level);

        // Didn't build price correctly lets return true
        if (levelCost == null) {
            return true;
        // Else make 'em pay
        } else {
            return levelCost.pay(fme);
        }
    }

    public void disable(boolean log) {
        if (log) {
            P.p.log("Disabling Upgrade: " + id());
        }
        factionUpgrades.unregister(this);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

}
