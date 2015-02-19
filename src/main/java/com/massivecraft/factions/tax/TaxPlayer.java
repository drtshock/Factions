package com.massivecraft.factions.tax;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent.PlayerLeaveReason;
import com.massivecraft.factions.integration.Econ;

public class TaxPlayer {
	public TaxPlayer(FPlayer player) {
		this.player = player;
		this.faction = TaxFaction.getTaxFaction(player.getFaction());
	}
	private final FPlayer player;
	private final TaxFaction faction;
	
	public FPlayer getPlayer() {
		return player;
	}
	public TaxFaction getFaction() {
		return faction;
	}
	
	public long getAffordableTime() {
	    return getAffordablePeriods() * TaxConfig.getTaxPeriod();
	}
	
	public int getAffordablePeriods() {
		return (int) (getBalance() / getTax());
	}
	
	public static TaxPlayer getTaxPlayer(FPlayer player) {
		return player.getTaxPlayer();
	}
	
	public static TaxPlayer getTaxPlayer(OfflinePlayer player) {
		return getTaxPlayer(FPlayers.getInstance().getByOfflinePlayer(player));
	}
	
	public double getTax() {
		TaxRules taxRules = getFaction().getTaxRules();
		if (taxRules.getPlayerTaxMap().containsKey(getPlayer().getPlayer().getUniqueId())) {
			return taxRules.getPlayerTaxMap().get(getPlayer().getPlayer().getUniqueId());
		} else if (taxRules.getRoleTaxMap().containsKey(getPlayer().getRole())) {
			return taxRules.getRoleTaxMap().get(getPlayer().getRole());
		} else {
			return taxRules.getDefaultTax();
		}
	}
	
	public boolean canAffordTax(int taxPeriods) {
		double owedTax = getTax() * taxPeriods;
		return canAfford(owedTax);
	}
	
	public boolean canAffordTax() {
		return canAffordTax(1);
	}
	
	public double getBalance() {
		return Econ.getBalance(getPlayer().getAccountId());
	}
	
	public void msg(String msg, Object... args) {
		getPlayer().msg(msg, args);
	}
	
	public boolean canAfford(double amount) {
		return amount < getBalance();
	}
	
	public void deposit(double amount) {
		if (amount < 0) {
			charge(-amount); 
		} else {
			Econ.deposit(getPlayer().getAccountId(), amount);
		}
	}
	
	public void punish() {
		if (getFaction().getTaxRules().isKickNotPaying()) {
			FPlayerLeaveEvent event = new FPlayerLeaveEvent(getPlayer(), getFaction().getFaction(), PlayerLeaveReason.KICKED);
			Bukkit.getPluginManager().callEvent(event);
			msg("You were kicked because you can't pay your taxes!");
		} else {
			msg("You can't pay your taxes but your faction let you stay.");
		}
	}
	
	public boolean charge(double amount) {
		if (amount < 0) {
			deposit(-amount);
			return true;
		} else if (!canAfford(amount)) {
			return false;
		} else {
			return Econ.withdraw(getPlayer().getAccountId(), amount);
		}
	}
	
	public void msg(String[] msgs) {
		for (String msg : msgs) {
			msg(msg);
		}
	}
	
	public boolean isInactive() {
		if (TaxConfig.getInactiveTime() < 0) return false;
		return (System.currentTimeMillis() - getPlayer().getPlayer().getLastPlayed()) > TaxConfig.getInactiveTime();
	}
}
