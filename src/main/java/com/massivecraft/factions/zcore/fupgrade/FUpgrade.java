package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fupgrade.cost.FUpgradeCost;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public abstract class FUpgrade implements Listener {

    protected FUpgradeRoot root;

    protected int maxLevel;
    protected ConfigurationSection configSection;
    protected HashMap<Integer, HashMap<String, Double>> cost = new HashMap<>();
    protected HashMap<Integer, ConfigurationSection> levelConfigs = new HashMap<>();

    // Upgrade information
    public abstract String id();
    public abstract String translation();


    public FUpgrade(FUpgradeRoot root) {
        this.root = root;
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
            ConfigurationSection levelCostSection = attrs.getValue().getConfigurationSection("cost");
            HashMap<String, Double> levelCost = new HashMap<>();

            if (levelCostSection == null) {
                cost.put(attrs.getKey(), levelCost);
                continue;
            }


            for (String key : levelCostSection.getKeys(false)) {
                FUpgradeCost costType = root.getCost(key);
                double value = levelCostSection.getDouble(key);

                if (costType == null) {
                    continue;
                }

                levelCost.put(costType.id(), value);
            }
            cost.put(attrs.getKey(), levelCost);
        }
        // Everything is ready, let the upgrade handle the rest
        registerAttributes();
    }

    protected abstract void registerAttributes();

    public boolean pay(int level, FPlayer fme) {
        for (Map.Entry<String, Double> cost : cost.get(level).entrySet()) {
            // If one of the costs doesn't pass return false
            if (!root.getCost(cost.getKey()).transact(fme, cost.getValue(), true)) {
                return false;
            }
        }
        for (Map.Entry<String, Double> cost : cost.get(level).entrySet()) {
            root.getCost(cost.getKey()).transact(fme, cost.getValue(), false);
        }
        return true;
    }

    public void disable(boolean log) {
        if (log) {
            P.p.log("Disabling Upgrade: " + id());
        }
        root.unregister(this);
    }

    public int getMaxLevel() {
        return maxLevel;
    }

}
