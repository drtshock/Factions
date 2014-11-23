package com.massivecraft.factions.tax;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.Conf;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.integration.Econ;

import lombok.*;

@Getter
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public class TaxFaction {
	private final Faction faction;
	
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
