package com.massivecraft.factions.tax.cmd;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.tax.TaxFaction;
import com.massivecraft.factions.tax.TaxRules;
import com.massivecraft.factions.tax.format.FormatUtil;
import com.massivecraft.factions.tax.format.TextUtil;
import com.massivecraft.factions.tax.format.TimeDiffUtil;
import com.massivecraft.factions.tax.format.TimeUnit;
import com.massivecraft.factions.zcore.util.UUIDFetcher;

public class CmdTaxFaction extends FCommand {

	public CmdTaxFaction() {
		aliases.add("f");
		aliases.add("faction");
	
		optionalArgs.put("faction", "yours");
		
		setHelpShort("faction tax info");
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
		senderMustBeModerator = false;
		senderMustBeAdmin = false;
	}
	
	@Override
	public void perform() {
		Faction faction = myFaction;
		if (argIsSet(0)) {
			faction = argAsFaction(0);
		}
		
		if (faction == null) return;

		TaxFaction taxFaction = TaxFaction.getTaxFaction(faction);
		msg(p.txt.titleize("Faction Tax " + faction.describeTo(fme, true)));
		msg("<a>Upkeep Tax: <i>%s", describeUpkeepTax(taxFaction));
		msg("<a>Can Afford: %s", describeAffordTime(taxFaction));
		msg("<a>Player Kick: %s", describePlayerKick(taxFaction));
		msg("<a>Faction Tax Rules:");
		String[] describedRules = describeTaxRules(taxFaction);
		for (String describedRule : describedRules) {
			msg(describedRule);
		}
	}
	
	
	public static String describeUpkeepTax(TaxFaction taxFaction) {
		return FormatUtil.parse("<h>%s<i> / <h>%s<i> in faction bank.", FormatUtil.formatMoney(taxFaction.getUpkeep()), FormatUtil.formatMoney(taxFaction.getBalance()));
	}
	
	public static String describeAffordTime(TaxFaction taxFaction) {
		StringBuilder builder = new StringBuilder();
		if (taxFaction.canAffordUpkeep()) {
			builder.append(FormatUtil.YES);
		} else {
			builder.append(FormatUtil.NO);
		}
		builder.append(FormatUtil.parse("<i> "));
		if (taxFaction.getUpkeep() == 0) {
			builder.append("because there is no upkeep.");
		} else if (taxFaction.getUpkeep() < 0) {
			builder.append("because the server is paying you.");
		} else if (taxFaction.canAffordUpkeep()) {
			builder.append("for the next ");
			builder.append(toStringAffordTime(taxFaction.getAffordableTime()));
			builder.append(FormatUtil.parse("<i>."));
		} else {
			double owed = taxFaction.getUpkeep() - taxFaction.getBalance();
			builder.append(FormatUtil.parse("<b>missing <h>"));
			builder.append(owed);
			builder.append("!");
		}
		return builder.toString();
	}
	
	public static String describePlayerKick(TaxFaction taxFaction) {
		if (taxFaction.isKickNotPaying()) {
			return FormatUtil.parse("%s <i>Players who can't afford the tax will remain in the faction.", FormatUtil.YES);
		} else {
			return FormatUtil.parse("%s <I>Players who can't afford the tax will be kicked from the faction.", FormatUtil.NO);
		}
	}
	
	public static String toStringAffordTime(long affordableMill) {
		//Lets hope this works
	    LinkedHashMap<TimeUnit, Long> unitCounts = TimeDiffUtil.unitcounts(affordableMill, TimeUnit.getAllButMillis());
	    unitCounts = TimeDiffUtil.limit(unitCounts, 2);
		return TimeDiffUtil.formatedVerboose(unitCounts);
	}
	

	public static String[] describeTaxRules(TaxFaction taxFaction) {
		TaxRules rules = taxFaction.getTaxRules();
		String[] describedRules = new String[1 + rules.getRoleTaxMap().size() + rules.getPlayerTaxMap().size()];
		int currentlyDescribed = 0;
		describedRules[currentlyDescribed] = describeRule(rules.getDefaultTax(), "Default");
		for (Entry<Role, Double> rule : rules.getRoleTaxMap().entrySet()) {
			describedRules[currentlyDescribed] = describeRule(rule.getValue(), rule.getKey().nicename);
			currentlyDescribed++;
		}
		for (Entry<UUID, Double> rule : rules.getPlayerTaxMap().entrySet()) {
			describedRules[currentlyDescribed] = describeRule(rule.getValue(), getName(rule.getKey()));
		}
		return describedRules;
	}
	
	public static String describeRule(double tax, String ruleName) {
		ruleName = FormatUtil.upperCaseFirst(ruleName);
		return FormatUtil.parse("<h>%s <p>%s", tax, ruleName);
	}
	
	public static String getName(UUID player) {
		return Bukkit.getOfflinePlayer(player).getName();
	}
}
