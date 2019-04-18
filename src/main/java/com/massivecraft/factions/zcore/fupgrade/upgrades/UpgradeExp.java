package com.massivecraft.factions.zcore.fupgrade.upgrades;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.zcore.fupgrade.FUpgrade;
import com.massivecraft.factions.zcore.fupgrade.FUpgradeRoot;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.HashMap;
import java.util.Map;

public class UpgradeExp extends FUpgrade {

    private HashMap<Integer, Double> expRate = new HashMap<>();

    @Override
    public String id() {
        return "EXP";
    }
    @Override
    public String translation() {
        return TL.UPGRADE_EXP.toString();
    }

    public UpgradeExp(FUpgradeRoot root) {
        super(root);
    }

    @Override
    protected void register() {
        for (Map.Entry<Integer, ConfigurationSection> entry : levels.entrySet()) {
            expRate.put(entry.getKey(), entry.getValue().getDouble("rate", 1));
        }
    }

    @EventHandler
    public void onExp(PlayerExpChangeEvent event) {
        FPlayer fme = FPlayers.getInstance().getByPlayer(event.getPlayer());
        Faction faction = fme.getFaction();
        // Lets apply rate only if player is in a claim
        if (fme.isInOwnTerritory()) {
            int level = faction.getUpgradeLevel(id());

            int xp = (int) (event.getAmount() * expRate.get(level));
            event.setAmount(xp);
        }
    }

}
