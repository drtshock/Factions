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
	
	public boolean isEnabled() {
		return TaxConfig.isTaxEnabled();
	}
	
	public void enable() {
		if (!isEnabled()) return;
		checkGracePeriod();
		scheduleTaxTask();
		registerListeners();
	}
	
	private FactionsTaxPlayerListener playerListener = new FactionsTaxPlayerListener(this);
	public void registerListeners() {
		Bukkit.getPluginManager().registerEvents(playerListener, P.p);
	}
	
	private TaxTask task;
	public void scheduleTaxTask() {
		if (task == null) task = new TaxTask(this);
		Bukkit.getScheduler().runTaskTimer(P.p, task, 0, task.getPeriod());
	}
	
	public void checkGracePeriod() {
		if (TaxConfig.getTaxFirstStarted() == 0)  { //Haven't started taxes before
			TaxConfig.setTaxFirstStarted(System.currentTimeMillis());
			setGracePeriod(true);
		} else if (System.currentTimeMillis() - TaxConfig.getTaxFirstStarted() > TaxConfig.getGracePeriod()) {
			setGracePeriod(true);
		} else {
			setGracePeriod(false);
		}
	}
	
	public static FactionsTax getInstance() {
		return P.p.getTax();
	}
}
