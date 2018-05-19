package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.P;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class FUpgradeRoot {

    private P p;
    private HashSet<FUpgrade> upgrades = new HashSet<>();

    public FUpgradeRoot(P p) {
        this.p = p;
        register(p, new UpgradeCrop());
    }

    // Using this method to allow for more flexibility in the future
    public void register(JavaPlugin plugin, FUpgrade factionUpgrade) {
        upgrades.add(factionUpgrade);
        getServer().getPluginManager().registerEvents(factionUpgrade, plugin);
    }

    public Set<? extends FUpgrade> getUpgrades() {
        return Collections.unmodifiableSet(upgrades);
    }


    public FUpgrade getUpgrade(String upgradeId) {
        for (FUpgrade upgrade : upgrades) {
            if (upgrade.id().equals(upgradeId)) {
                return upgrade;
            }
        }
        return null;
    }

    public FUpgrade fromString(String name) {
        for (FUpgrade upgrade : upgrades) {
            if (upgrade.id().equals(name)) {
                return upgrade;
            }
        }
        return null;
    }

}
