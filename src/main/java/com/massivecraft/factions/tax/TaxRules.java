package com.massivecraft.factions.tax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Role;

public class TaxRules {
	private double defaultTax = 0;
	private Map<Role, Double> roleTaxMap = new HashMap<Role, Double>();
	private Map<UUID, Double> playerTaxMap = new HashMap<UUID, Double>();
	private boolean kickNotPaying = false; //Don't kick non paying members by default
	private boolean payUpkeepIfEnabled = true; //By default factions pay upkeep if enabled
	
	public double getDefaultTax() {
		return defaultTax;
	}
	
	public Map<Role, Double> getRoleTaxMap() {
		return roleTaxMap;
	}
	
	public Map<UUID, Double> getPlayerTaxMap() {
		return playerTaxMap;
	}
	
	public boolean isKickNotPaying() {
		return kickNotPaying;
	}
	
	public void setKickNotPaying(boolean kickNotPaying) {
		this.kickNotPaying = kickNotPaying;
	}
	
	public boolean isPayUpkeepIfEnabled() {
		return payUpkeepIfEnabled;
	}
	
	public void setPayUpkeepIfEnabled(boolean payUpkeepIfEnabled) {
		this.payUpkeepIfEnabled = payUpkeepIfEnabled;
	}
	
	public void setDefaultTax(double tax) {
		this.defaultTax = tax;
	}
	
	public void clear() {
		defaultTax = 0;
		roleTaxMap.clear();
		playerTaxMap.clear();
	}
	
	public void setPlayerTax(FPlayer player, double tax) {
		playerTaxMap.put(UUID.fromString(player.getId()), tax);
	}
	
	public void setRankTax(Role rank, double tax) {
		roleTaxMap.put(rank, tax);
	}
}