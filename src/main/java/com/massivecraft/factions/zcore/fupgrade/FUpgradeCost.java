package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.integration.Econ;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FUpgradeCost {

    private HashMap<CostType, Double> cost = new HashMap<>();

    public void put(CostType costType, double amount) {
        cost.put(costType, amount);
    }

    public boolean pay(FPlayer fme) {
        // Check if they have enough to pay for everything
        for (Map.Entry<CostType, Double> entry : cost.entrySet()) {
            boolean transaction = entry.getKey().transact(entry.getValue(), fme, false);
            if (!transaction) {
                return false;
            }
        }

        // Now that we know they have enough for everything make 'em pay
        for (Map.Entry<CostType, Double> entry : cost.entrySet()) {
            entry.getKey().transact(entry.getValue(), fme, true);
        }
        return true;
    }

    public enum CostType {
        PLAYER_MONEY,
        PLAYER_EXP,
        FACTION_MONEY,
        ;

        public boolean transact(double amount, FPlayer fme, boolean transact) {
            switch (this) {
                case PLAYER_MONEY:
                    // Check if Economy should be used if not return true
                    if (!Econ.shouldBeUsed()) {
                        return true;
                        // Check if the player has atleast the amount
                    } else if (!Econ.hasAtLeast(fme, amount, null)) {
                        return false;
                    }
                    // If Transact = true lets modify the monies
                    if (transact) {
                        Econ.modifyMoney(fme, -amount, null, null);
                    }
                case PLAYER_EXP:
                    Player player = fme.getPlayer();
                    if (player.getLevel() >= amount) {
                        if (transact) {
                            player.setLevel(player.getLevel() - (int) amount);
                        }
                        return true;
                    } else {
                        return false;
                    }
                case FACTION_MONEY:
                    // Check if Economy should be used if not return true
                    if (!Econ.shouldBeUsed() || !Conf.bankFactionPaysCosts) {
                        return true;
                        // Check if the player has atleast the amount
                    } else if (!Econ.hasAtLeast(fme.getFaction(), amount, null)) {
                        return false;
                    }
                    // If Transact = true lets modify the monies
                    if (transact) {
                        Econ.modifyMoney(fme.getFaction(), -amount, null, null);
                    }
                default:
                    return false;
            }
        }

        public static CostType fromString(String string) {
            for (CostType type : values()) {
                if (string.equalsIgnoreCase(type.name())) {
                    return type;
                }
            }
            return null;
        }
    }

}
