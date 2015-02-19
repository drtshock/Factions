package com.massivecraft.factions.tax;

import org.bukkit.Bukkit;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.P;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import com.massivecraft.factions.event.FPlayerLeaveEvent.PlayerLeaveReason;
import com.massivecraft.factions.event.FactionDisbandEvent;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.util.EasyCache;

public class TaxFaction {
	public TaxFaction(Faction faction) {
		this.faction = faction;
	}
	private final Faction faction;
	public Faction getFaction() {
		return faction;
	}
	public boolean canAffordUpkeep(int upkeepPeriods) {
		double owedUpkeep = getUpkeep() * upkeepPeriods;
		return canAfford(owedUpkeep);
	}
	
	public boolean canAffordUpkeep() {
		return canAffordUpkeep(1);
	}
	
	public long getAffordableTime() {
		return getAffordablePeriods() * TaxConfig.getTaxPeriod();
	}
	
	public int getAffordablePeriods() {
		return (int) (getBalance() / getUpkeep());
	}
	public double getBalance() {
		return Econ.getBalance(getFaction().getAccountId());
	}
	
	public double getUpkeep() {
		if (!shouldPayUpkeep()) return 0;
		double chunkUpkeep = getNumberOfChunks() * TaxConfig.getUpkeepPerChunk();
		return chunkUpkeep + TaxConfig.getBaseUpkeep();
	}
	
	public boolean isKickNotPaying() {
		return getTaxRules().isKickNotPaying();
	}
	
	public int getNumberOfChunks() {
		return Board.getInstance().getFactionCoordCount(getFaction());
	}
	
	private static EasyCache<Faction, TaxFaction> factionCache = new EasyCache<Faction, TaxFaction>(new EasyCache.Loader<Faction, TaxFaction>() {

		@Override
		public TaxFaction load(Faction key) {
			return new TaxFaction(key);
		}
	});
	public static TaxFaction getTaxFaction(Faction faction) {
		return factionCache.get(faction);
	}
	
	public TaxRules getTaxRules() {
		return getFaction().getTaxRules();
	}
	
	public boolean shouldPayUpkeep() {
		return getFaction().getTaxRules().isPayUpkeepIfEnabled() && TaxConfig.getBaseUpkeep() != 0 && TaxConfig.getUpkeepPerChunk() == 0;
	}
	
	public void punish() {
		if (TaxConfig.isDisbandOnUpkeepFail()) {
			msgAll("Your faction has been disbanded because you can't pay your upkeep.");
			FactionDisbandEvent disbandEvent = new FactionDisbandEvent(null, getFaction().getId());
			Bukkit.getPluginManager().callEvent(disbandEvent);
			if (disbandEvent.isCancelled()) { //Must respect isCancelled()
				return;
			}
			for (FPlayer player : getFaction().getFPlayers()) {
				FPlayerLeaveEvent event = new FPlayerLeaveEvent(player, getFaction(), PlayerLeaveReason.DISBAND);
				Bukkit.getPluginManager().callEvent(event);
			}
		} else if (TaxConfig.isUnclaimAllOnUpkeepFail()) {
			msgAll("All %s chunks of your faction have been unclaimed because you can't pay your upkeep.", getNumberOfChunks());
			getFaction().clearAllClaimOwnership(); 
		} else {
			msgAll("You can't pay your upkeep but punishment is disabled.");
		}
	}
	
	public void msgAll(String msg, Object... args) {
		getFaction().msg(msg, args);
	}
	
	public void debug(String msg) {
		P.p.debug(msg);
	}
	
	public void deposit(double amount) {
		if (amount < 0) charge(-amount);
		Econ.deposit(getFaction().getAccountId(), amount);
	}
	
	public boolean charge(double amount) {
		if (amount < 0) {
			deposit(-amount);
			return true;
		} else if (!canAfford(amount)) {
			return false;
		} else {
			return Econ.withdraw(getFaction().getAccountId(), amount);
		}
	}
	
	public boolean canAfford(double amount) {
		return amount < getBalance();
	}
	
	
} 
