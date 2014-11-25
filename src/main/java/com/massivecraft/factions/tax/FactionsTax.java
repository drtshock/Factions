package com.massivecraft.factions.tax;

import java.util.LinkedHashMap;

import org.bukkit.Bukkit;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.tax.format.TimeDiffUtil;
import com.massivecraft.factions.tax.format.TimeUnit;

public class FactionsTax {
	private boolean gracePeriod;
	
	public boolean isGracePeriod() {
		return gracePeriod;
	}
	public void setGracePeriod(boolean gracePeriod) {
		this.gracePeriod = gracePeriod;
	}
	public void enable() {
		if (!Conf.taxEnabled) return;
		if (Conf.taxFirstStartedMill == 0)  { //Haven't started taxes before
			Conf.taxFirstStartedMill = System.currentTimeMillis();
			setGracePeriod(true);
		} else if (System.currentTimeMillis() - Conf.taxFirstStartedMill > Conf.taxFirstStartedGraceMill) {
			setGracePeriod(true);
		} else {
			setGracePeriod(false);
		}
		
	}
	
	private FactionsTaxPlayerListener playerListener = new FactionsTaxPlayerListener(this);
	public void registerListeners() {
		Bukkit.getPluginManager().registerEvents(playerListener, P.p);
	}
	

}
