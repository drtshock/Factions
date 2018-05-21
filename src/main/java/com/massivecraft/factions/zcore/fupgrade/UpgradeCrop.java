package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.HashMap;
import java.util.Random;

public class UpgradeCrop extends FUpgrade {

    private HashMap<Integer, Double> rateMap = new HashMap<>();

    @Override
    public String id() {
        return "CROP";
    }
    @Override
    public String translation() {
        return TL.UPGRADE_CROP.toString();
    }


    UpgradeCrop(FUpgradeRoot factionUpgrades) {
        super(factionUpgrades);
        registerRates();
    }

    private void registerRates() {
        ConfigurationSection attributeSection = configSection.getConfigurationSection("attributes");
        for (String key : attributeSection.getKeys(false)) {
            rateMap.put(Integer.parseInt(key), attributeSection.getDouble(key + ".rate"));
        }
    }

    private double getRate(Integer level) {
        return rateMap.get(level);
    }

    @EventHandler
    public void onCropGrowth(BlockGrowEvent event) {
        FLocation factionLoc = new FLocation(event.getBlock().getLocation());
        Faction factionAt = Board.getInstance().getFactionAt(factionLoc);
        int level = factionAt.getUpgradeLevel(id());

        int random = new Random().nextInt(100);
        int rateChance = (int) getRate(level) * 100;

        if (rateChance >= random) {
            // TODO: We have to double grow...
            P.p.log("We have to double grow!");
        }
    }

}
