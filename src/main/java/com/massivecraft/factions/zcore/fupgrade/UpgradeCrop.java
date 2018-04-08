package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

public class UpgradeCrop extends FUpgrade {

    private HashMap<Integer, Double> rateMap = new HashMap<>();

    @Override
    public String name() {
        return "CROP";
    }

    @Override
    public String translation() {
        return TL.UPGRADE_CROP.toString();
    }

    UpgradeCrop() {
        super();
        maxLevel = configSection.getInt("max-level", 3);
        registerRates();
    }

    private void registerRates() {
        ConfigurationSection attributeSection = configSection.getConfigurationSection("attributes");
        for (String key : attributeSection.getKeys(false)) {
            rateMap.put(Integer.parseInt(key), attributeSection.getDouble(key + ".rate"));
        }
    }

    public double getRate(Integer level) {
        return rateMap.get(level);
    }

}
