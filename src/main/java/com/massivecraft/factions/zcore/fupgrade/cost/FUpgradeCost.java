package com.massivecraft.factions.zcore.fupgrade.cost;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.integration.Econ;
import org.bukkit.entity.Player;

public interface FUpgradeCost {

    String id();

    boolean transact(FPlayer fme, double amount, boolean verify);

    class PlayerEcon implements FUpgradeCost {

        @Override
        public String id() {
            return "PLAYER_MONEY";
        }

        @Override
        public boolean transact(FPlayer fme, double amount, boolean verify) {
            // Check if Economy should be used if not return true
            if (!Econ.shouldBeUsed()) {
                return true;
                // Check if the player has atleast the amount
            } else if (!Econ.hasAtLeast(fme, amount, null)) {
                return false;
            }
            // If transact = true lets modify the monies
            if (!verify) {
                Econ.modifyMoney(fme, -amount, null, null);
            }
            return true;
        }

    }

    class PlayerExp implements FUpgradeCost {

        @Override
        public String id() {
            return "PLAYER_EXP";
        }

        @Override
        public boolean transact(FPlayer fme, double amount, boolean verify) {
            Player player = fme.getPlayer();
            if (player.getLevel() >= amount) {
                if (!verify) {
                    player.setLevel(player.getLevel() - (int) amount);
                }
                return true;
            } else {
                return false;
            }
        }
    }

    class FactionEcon implements FUpgradeCost {

        @Override
        public String id() {
            return "FACTION_MONEY";
        }

        @Override
        public boolean transact(FPlayer fme, double amount, boolean verify) {
            // Check if Economy should be used if not return true
            if (!Econ.shouldBeUsed() || !Conf.bankFactionPaysCosts) {
                return true;
                // Check if the faction has atleast the amount
            } else if (!Econ.hasAtLeast(fme.getFaction(), amount, null)) {
                return false;
            }
            // If transact = true lets modify the monies
            if (!verify) {
                Econ.modifyMoney(fme.getFaction(), -amount, null, null);
            }
            return true;
        }
    }

}
