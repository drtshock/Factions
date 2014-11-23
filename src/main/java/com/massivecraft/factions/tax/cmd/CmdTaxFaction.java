package com.massivecraft.factions.tax.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.tax.TaxFaction;
import com.massivecraft.factions.tax.format.FormatUtil;

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
		msg(p.txt.titleize("Faction Tax " + faction.getTag()));
		msg("<a>Upkeep Tax: " + describeUpkeepTax(taxFaction));
		
	}
	
	public static String describeUpkeepTax(TaxFaction taxFaction) {
		return FormatUtil.parse("<h>%s<i> / <h>%s<i> in faction bank.", FormatUtil.formatMoney(taxFaction.getUpkeep()), FormatUtil.formatMoney(taxFaction.getBalance()));
	}

}
