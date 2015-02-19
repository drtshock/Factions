package com.massivecraft.factions.tax.cmd;

import java.util.Map;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.tax.FactionsTax;
import com.massivecraft.factions.tax.TaxConfig;
import com.massivecraft.factions.tax.format.FormatUtil;
import com.massivecraft.factions.tax.format.TimeDiffUtil;
import com.massivecraft.factions.tax.format.TimeUnit;

public class CmdTaxInfo extends FCommand {
	
	public static final String TAXES_DISABLED = "<n>Taxes Are Currently Disabled!";	
	
	public CmdTaxInfo() {
		aliases.add("i");
		aliases.add("info");
	}
	
	@Override
	public void perform() {
		msg(FormatUtil.titleize("Tax Overview Information"));
		msg("<a>Taxation Period: <i>%s", describeTaxationPeriod());
		msg("<a>Next Taxation: <i>%s", describeNextTaxation());
		msg("<i>Players and factions are charged at the same time.");
		msg(describeUpkeep());
		msg("<i>If a faction can't afford its upkeep %s", describePunishment());
		msg(describeTaxRange());
		msg("<i>Leader sets if players who can't afford tax are kicked.");
		msg("<i>Players stop paying tax after being online for %s.", describeOfflineTillInactive());
	}
	
	public static String describeOfflineTillInactive() {
		Map<TimeUnit, Long> unitcounts = TimeDiffUtil.unitcounts(TaxConfig.getInactiveTime());
		return TimeDiffUtil.formatedVerboose(unitcounts);
	}
	
	public static String describeTaxRange() {
		return FormatUtil.parse("<i>Leader sets the faction tax. It can vary between %s and %s.", describeTax(TaxConfig.getMinimumTax()), describeTax(TaxConfig.getMaximumTax()));
	}
	
	public static String describeTax(double tax) {
		if (tax < 0) {
			return "getting paid " + FormatUtil.formatMoney(-tax);
		} else if (tax > 0) {
			return "paying " + FormatUtil.formatMoney(tax);
		} else {
			return "nothing";
		}
	}
	
	public static String describePunishment() {
		if (TaxConfig.isDisbandOnUpkeepFail()) {
			return FormatUtil.parse("<h>it will be disbanded<i>.");
		} else if (TaxConfig.isUnclaimAllOnUpkeepFail()) {
			return FormatUtil.parse("<h>all its land will be unclaimed<i>.");
		} else {
			return FormatUtil.parse("<i>nothing will happen.");
		}
	}
	
	public static String describeUpkeep() {
		StringBuilder builder = new StringBuilder();
		builder.append("<i>Faction Upkeep is <h>");
		boolean base = TaxConfig.getBaseUpkeep() != 0;
		boolean perChunk = TaxConfig.getUpkeepPerChunk() != 0;
		
		if (base) {
			builder.append(FormatUtil.formatMoney(TaxConfig.getBaseUpkeep()));
		}
		if (base && perChunk) {
			builder.append(" <i>and<h> ");
		}
		if (perChunk) {
			builder.append(FormatUtil.formatMoney(TaxConfig.getUpkeepPerChunk()));
			builder.append(" per chunk");
		}
		builder.append("<i>.");
		return builder.toString();
	}
	
	public static String describeTaxationPeriod() {
		if (!FactionsTax.getInstance().isEnabled()) {
			return TAXES_DISABLED;
		}
		return "Every " + TimeDiffUtil.formatedVerboose(TimeDiffUtil.unitcounts(TaxConfig.getTaxPeriod())) + FormatUtil.parse("<i>.");
	}
	
	public static String describeNextTaxation() {
		if (!FactionsTax.getInstance().isEnabled()) {
			return TAXES_DISABLED;
		}
		long timeTillNextTax = 0;
		FactionsTax.getInstance().checkGracePeriod();
		if (FactionsTax.getInstance().isGracePeriod()) {
			timeTillNextTax = TaxConfig.getTaxFirstStarted() + (TaxConfig.getGracePeriod() - System.currentTimeMillis());
		} else {
			timeTillNextTax = TaxConfig.getTaxPeriod() + (TaxConfig.getLastTax() - System.currentTimeMillis());
		}
		Map<TimeUnit, Long> unitcounts = TimeDiffUtil.unitcounts(timeTillNextTax);
		return TimeDiffUtil.formatedVerboose(unitcounts) + FormatUtil.parse("<i> from now.");
	}

}
