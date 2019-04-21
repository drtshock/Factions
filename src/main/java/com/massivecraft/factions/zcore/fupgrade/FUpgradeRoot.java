package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fupgrade.cost.FUpgradeCost;
import com.massivecraft.factions.zcore.fupgrade.upgrades.UpgradeCrop;
import com.massivecraft.factions.zcore.fupgrade.upgrades.UpgradeExp;
import com.massivecraft.factions.zcore.fupgrade.upgrades.UpgradeSpawner;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static org.bukkit.Bukkit.getServer;

public class FUpgradeRoot {

    private HashMap<String, FUpgrade> upgrades = new HashMap<>();
    private HashMap<String, FUpgradeCost> costs = new HashMap<>();

    public FUpgradeRoot(P p) {
        p.log("Loading Faction Upgrades");

        // Load default upgrades
        register(p, new UpgradeCrop(this));
        register(p, new UpgradeSpawner(this));
        register(p, new UpgradeExp(this));

        register(new FUpgradeCost.PlayerEcon());
        register(new FUpgradeCost.PlayerExp());
        register(new FUpgradeCost.FactionEcon());

        p.log("Finished loading Faction Upgrades");
        p.log(upgrades.values());
    }

    public void register(JavaPlugin plugin, FUpgrade factionUpgrade) {
        upgrades.put(factionUpgrade.id(), factionUpgrade);
        getServer().getPluginManager().registerEvents(factionUpgrade, plugin);
    }

    public void unregister(FUpgrade factionUpgrade) {
        upgrades.remove(factionUpgrade);
        HandlerList.unregisterAll(factionUpgrade);
    }

    public void register(FUpgradeCost cost) {
        costs.put(cost.id(), cost);
    }

    public Map<String, FUpgrade> getUpgrades() {
        return Collections.unmodifiableMap(upgrades);
    }

    public FUpgrade getUpgrade(String upgradeId) {
        return upgrades.get(upgradeId);
    }

    public Map<String, FUpgradeCost> getCosts() {
        return Collections.unmodifiableMap(costs);
    }

    public FUpgradeCost getCost(String costId) {
        return costs.get(costId);
    }

}
