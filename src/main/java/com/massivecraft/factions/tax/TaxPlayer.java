package com.massivecraft.factions.tax;

import org.bukkit.OfflinePlayer;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.integration.Econ;

import lombok.*;

@Getter
public class TaxPlayer {
	public TaxPlayer(OfflinePlayer player) {
		this.player = player;
		this.faction = TaxFaction.getTaxFaction(FPlayers.getInstance().getByOfflinePlayer(player).getFaction());
	}
	private final OfflinePlayer player;
	private final TaxFaction faction;
	public static TaxPlayer getTaxPlayer(OfflinePlayer player) {
		//TODO cache
		return new TaxPlayer(player);
	}
	
	public double getTax() {
		return getFPlayer().getFaction().getTaxRules().getTaxForPlayer(getFPlayer());
	}
	
	public boolean canAfford(int taxPeriods) {
		double owedTax = getTax() * taxPeriods;
		return getBalance() > owedTax;
	}
	
	public double getBalance() {
		return Econ.getBalance(getFPlayer().getAccountId());
	}
	
	public FPlayer getFPlayer() {
		return FPlayers.getInstance().getByOfflinePlayer(getPlayer());
	}
}
