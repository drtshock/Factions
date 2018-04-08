package com.massivecraft.factions.listeners;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fupgrade.UpgradeCrop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Random;

public class FactionsUpgradeListener implements Listener {

    @EventHandler
    public void onCropGrowth(BlockGrowEvent event) {
        UpgradeCrop upgradeCrop = P.p.factionUpgrades.getUpgrade(UpgradeCrop.class);
        FLocation factionLoc = new FLocation(event.getBlock().getLocation());
        Faction factionAt = Board.getInstance().getFactionAt(factionLoc);
        int level = factionAt.getUpgradeLevel(UpgradeCrop.class);

        int random = new Random().nextInt(100);
        int rateChance = (int) upgradeCrop.getRate(level) * 100;

        if (rateChance >= random) {
            // TODO: We have to double grow...
        }
    }

}
