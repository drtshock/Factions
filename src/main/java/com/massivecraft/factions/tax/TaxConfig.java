package com.massivecraft.factions.tax;

import java.io.File;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.tax.format.TimeDiffUtil;

public class TaxConfig {
	private YamlConfiguration config;
	public YamlConfiguration getConfig() {
		if (config == null) {
			YamlConfiguration.loadConfiguration(new File(P.p.getDataFolder(), "config.yml"));	
		}
		return config;
	}
	public ConfigurationSection getTaxSection() {
		return getConfig().getConfigurationSection("tax");
	}
	public ConfigurationSection getUpkeepSection() {
		return getTaxSection().getConfigurationSection("upkeep");
	}
	public ConfigurationSection getUpkeepFailSection() {
		return getUpkeepSection().getConfigurationSection("upkeepFail");
	}
	public boolean isTaxEnabled() {
		return getTaxSection().getBoolean("enabled", false);
	}
	public double getMaximumTax() {
		return getTaxSection().getDouble("maxTax", 10);
	}
	public double getMinimumTax() {
		return getTaxSection().getDouble("minTax", -10);
	}
	public double getBaseUpkeep() {
		return getUpkeepSection().getDouble("baseUpkeep", 0);
	}
	public double getUpkeepPerChunk() {
		return getUpkeepSection().getDouble("upkeepPerChunk", 0.1);
	}
	public boolean isDisbandOnUpkeepFail() {
		return getUpkeepFailSection().getBoolean("disband", false);
	}
	public boolean isUnclainAllOnUpkeepFail() {
		return getUpkeepFailSection().getBoolean("upkeepFail", false);
	}
	public long getTaxPeriod() {
		return getTaxSection().getLong("taxPeriod", 86400) * 60000;
	}
	public long getInactiveTime() {
		return getTaxSection().getLong("inactiveTime", 259200) * 60000;
	}
	public long getGracePeriod() {
		return getTaxSection().getLong("gracePeriod", 604800) * 60000;
	}
	public boolean isGraceMessage() {
		return getTaxSection().getBoolean("graceMessage", true);
	}
	public int getTaxPeriodsTillWarning() {
		return getTaxSection().getInt("taxPeriodsTillWarning", 7);
	}
	public int getUpkeepPeriodsTillWarning() {
		return getTaxSection().getInt("upkeepPeriodsTillWarning", 7);
	}
	
}
