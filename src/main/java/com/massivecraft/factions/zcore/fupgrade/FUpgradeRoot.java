package com.massivecraft.factions.zcore.fupgrade;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class FUpgradeRoot {

    private HashSet<FUpgrade> upgrades = new HashSet<>();

    public FUpgradeRoot() {
        register(new UpgradeCrop());
    }

    // Using this method to allow for more flexibility in the future
    public void register(FUpgrade factionUpgrade) {
        upgrades.add(factionUpgrade);
    }

    public Set<? extends FUpgrade> getUpgrades() {
        return Collections.unmodifiableSet(upgrades);
    }

    public <T extends FUpgrade> T getUpgrade(Class<T> upgradeClass) {
        for (FUpgrade upgrade : upgrades) {
            if (upgradeClass.isInstance(upgrade)) {
                return upgradeClass.cast(upgrade);
            }
        }
        return null;
    }

    public FUpgrade fromString(String name) {
        for (FUpgrade upgrade : upgrades) {
            if (upgrade.name().equalsIgnoreCase(name)) {
                return upgrade;
            }
        }
        return null;
    }

}
