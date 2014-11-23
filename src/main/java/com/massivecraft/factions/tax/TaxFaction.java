package com.massivecraft.factions.tax;

import java.util.LinkedHashMap;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.tax.format.TimeDiffUtil;
import com.massivecraft.factions.tax.format.TimeUnit;
import com.massivecraft.factions.zcore.util.TextUtil;

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
	
	public int getAffordablePeriods() {
		return (int) (getBalance() / getUpkeep());
	}
	
	public String getTimeCanAfford() {
		//Lets hope this works
		long affordableMill = Conf.taxPeriodMill * getAffordablePeriods();
	    LinkedHashMap<TimeUnit, Long> unitCounts = TimeDiffUtil.unitcounts(affordableMill, TimeUnit.getAllButMillis());
	    unitCounts = TimeDiffUtil.limit(unitCounts, 2);
		return TimeDiffUtil.formatedVerboose(unitCounts);
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
