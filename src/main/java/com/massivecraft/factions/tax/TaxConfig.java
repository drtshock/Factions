package com.massivecraft.factions.tax;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.tax.format.TimeDiffUtil;

public class TaxConfig {
	private TaxConfig() {}
	
	private static YamlConfiguration config;
	public static YamlConfiguration getConfig() {
		if (config == null) {
			YamlConfiguration.loadConfiguration(new File(P.p.getDataFolder(), "config.yml"));	
		}
		return config;
	}
	public static boolean isTaxEnabled() {
		return getConfig().getBoolean("tax.enabled", false);
	}
	public static double getMaximumTax() {
		return getConfig().getDouble("tax.maxTax", 10);
	}
	public static double getMinimumTax() {
		return getConfig().getDouble("tax.minTax", -10);
	}
	public static double getBaseUpkeep() {
		return getConfig().getDouble("tax.upkeep.baseUpkeep", 0);
	}
	public static double getUpkeepPerChunk() {
		return getConfig().getDouble("tax.upkeep.upkeepPerChunk", 0.1);
	}
	public static boolean isDisbandOnUpkeepFail() {
		return getConfig().getBoolean("tax.upkeep.upkeepFail.disband", false);
	}
	public static boolean isUnclaimAllOnUpkeepFail() {
		return getConfig().getBoolean("tax.upkeep.upkeepFail.unclaimAll", false);
	}
	public static long getTaxPeriod() {
		return getConfig().getLong("tax.taxPeriod", 86400) * 60000;
	}
	public static long getInactiveTime() {
		return getConfig().getLong("tax.inactiveTime", 259200) * 60000;
	}
	public static long getGracePeriod() {
		return getConfig().getLong("tax.gracePeriod", 604800) * 60000;
	}
	public static boolean isGraceMessage() {
		return getConfig().getBoolean("tax.graceMessage", true);
	}
	public static int getTaxPeriodsTillWarning() {
		return getConfig().getInt("tax.taxPeriodsTillWarning", 7);
	}
	public static int getUpkeepPeriodsTillWarning() {
		return getConfig().getInt("tax.upkeepPeriodsTillWarning", 7);
	}
	public static long getTaxFirstStarted() {
		return getConfig().getLong("tax.taxFirstStarted", 0);
	}
	public static long getLastTax() {
		return getConfig().getLong("tax.lastTax", 0);
	}
	
	public static void setLastTax(long value) {
		getConfig().set("tax.lastTax", value);
	}
	
	public static void setTaxFirstStarted(long value) {
		getConfig().set("tax.taxFirstStarted", value);
	}
}
