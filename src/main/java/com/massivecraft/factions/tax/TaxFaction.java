package com.massivecraft.factions.tax;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.integration.Econ;

public class TaxFaction {
	public TaxFaction(Faction faction) {
		this.faction = faction;
	}
	private final Faction faction;
	public Faction getFaction() {
		return faction;
	}
	public boolean canAfford(int upkeepPeriods) {
		double owedUpkeep = getUpkeep() * upkeepPeriods;
		return owedUpkeep < getBalance();
	}
	
	public double getBalance() {
		return Econ.getBalance(getFaction().getAccountId());
	}
	
	public double getUpkeep() {
		double chunkUpkeep = getNumberOfChunks() * Conf.upkeepPerChunk;
		return chunkUpkeep + Conf.baseUpkeep;
	}
	
	public int getNumberOfChunks() {
		return Board.getInstance().getFactionCoordCount(getFaction());
	}
	
	public static TaxFaction getTaxFaction(Faction faction) {
		return new TaxFaction(faction);
	}
}
