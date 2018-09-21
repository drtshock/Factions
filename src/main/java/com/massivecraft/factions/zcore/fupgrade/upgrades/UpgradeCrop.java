package com.massivecraft.factions.zcore.fupgrade.upgrades;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fupgrade.FUpgrade;
import com.massivecraft.factions.zcore.fupgrade.FUpgradeRoot;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.material.Crops;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

public class UpgradeCrop extends FUpgrade {

    private HashMap<Integer, Double> rateMap;

    @Override
    public String id() {
        return "CROP";
    }
    @Override
    public String translation() {
        return TL.UPGRADE_CROP.toString();
    }

    public UpgradeCrop(FUpgradeRoot root) {
        super(root);
    }

    protected void registerAttributes() {
        rateMap = new HashMap<>();
        for (Map.Entry<Integer, ConfigurationSection> entry : levelConfigs.entrySet()) {
            rateMap.put(entry.getKey(), entry.getValue().getDouble("rate", 1));
        }
    }

    @EventHandler
    public void onCropGrowth(BlockGrowEvent event) {
        // Works by getting the rate remove one from it, the chance of it double growing
        FLocation factionLoc = new FLocation(event.getBlock().getLocation());
        Faction factionAt = Board.getInstance().getFactionAt(factionLoc);
        int level = factionAt.getUpgradeLevel(id());

        double random = new Random().nextDouble();
        double rateChance = rateMap.get(level) - 1;

        if (rateChance >= random) {
            if (event.getBlock().getType() == Material.CROPS) {
                Crops now = (Crops) event.getNewState().getData();
                CropState newState;
                // Shift the stage by one
                switch (now.getState()) {
                    case SEEDED:
                    case GERMINATED:
                        newState = CropState.GERMINATED;
                        break;
                    case VERY_SMALL:
                        newState = CropState.SMALL;
                        break;
                    case SMALL:
                        newState = CropState.MEDIUM;
                        break;
                    case MEDIUM:
                        newState = CropState.TALL;
                        break;
                    case TALL:
                        newState = CropState.VERY_TALL;
                        break;
                    case VERY_TALL:
                    default:
                        newState = CropState.RIPE;
                }

                event.getNewState().setData(new Crops(newState));
                event.getNewState().update();
            }
        }
    }

}
