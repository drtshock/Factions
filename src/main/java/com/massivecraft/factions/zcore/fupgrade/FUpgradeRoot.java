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

    private P p;
    private HashMap<String, FUpgrade> upgrades = new HashMap<>();
    private HashSet<FUpgradeCost> costs = new HashSet<>();

    public FUpgradeRoot(P p) {
        this.p = p;
        p.log("Loading Faction Upgrades");

        // Load default upgrades
        register(p, new UpgradeCrop(this));
        register(p, new UpgradeSpawner(this));
        register(p, new UpgradeExp(this));

        costs.add(new FUpgradeCost.PlayerEcon());
        costs.add(new FUpgradeCost.PlayerExp());
        costs.add(new FUpgradeCost.FactionEcon());

        p.log("Finished loading Faction Upgrades");
    }

    // Register the Upgrade into the Set and register the listener
    public void register(JavaPlugin plugin, FUpgrade factionUpgrade) {
        upgrades.put(factionUpgrade.id(), factionUpgrade);
        getServer().getPluginManager().registerEvents(factionUpgrade, plugin);
    }

    public void unregister(FUpgrade factionUpgrade) {
        upgrades.remove(factionUpgrade);
        HandlerList.unregisterAll(factionUpgrade);
    }

    public Map<String, FUpgrade> getUpgrades() {
        return Collections.unmodifiableMap(upgrades);
    }

    public FUpgrade getUpgrade(String upgradeId) {
        return upgrades.get(upgradeId);
    }

    public Set<FUpgradeCost> getCosts() {
        return Collections.unmodifiableSet(costs);
    }

    public FUpgradeCost getCost(String costId) {
        for (FUpgradeCost cost : costs) {
            if (cost.id().equalsIgnoreCase(costId)) {
                return cost;
            }
        }
        return null;
    }

}
