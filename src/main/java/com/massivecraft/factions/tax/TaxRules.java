package com.massivecraft.factions.tax;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Role;

public class TaxRules {
	private double defaultTax;
	
	public double getDefaultTax() {
		return defaultTax;
	}
	private Map<Role, Double> roleTaxMap = new HashMap<Role, Double>();
	private Map<FPlayer, Double> playerTaxMap = new HashMap<FPlayer, Double>();
	
	public double getTaxForRole(Role role) {
		if (roleTaxMap.containsKey(role)) {
			return roleTaxMap.get(role);
		} else {
			return getDefaultTax();
		}
	}
	
	public double getTaxForPlayer(FPlayer player) {
		if (playerTaxMap.containsKey(player)) {
			return roleTaxMap.get(player);
		} else {
			return getTaxForRole(player.getRole());
		}
	}
}
