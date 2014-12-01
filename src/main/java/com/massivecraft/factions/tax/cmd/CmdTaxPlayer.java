package com.massivecraft.factions.tax.cmd;

import java.util.LinkedHashMap;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.tax.TaxPlayer;
import com.massivecraft.factions.tax.format.FormatUtil;
import com.massivecraft.factions.tax.format.TimeDiffUtil;
import com.massivecraft.factions.tax.format.TimeUnit;

public class CmdTaxPlayer extends FCommand {
    
    public CmdTaxPlayer() {
        aliases.add("p");
		aliases.add("player");
		
		optionalArgs.put("player", "you");
		
		setHelpShort("player tax info");
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
		senderMustBeModerator = false;
		senderMustBeAdmin = false;
    }
    
	@Override
	public void perform() {
	    FPlayer player = fme;
	    if (argIsSet(0)) {
	        player = argAsFPlayer(0);
	    }
	    TaxPlayer taxPlayer = TaxPlayer.getTaxPlayer(player);
	    
	    msg(p.txt.titleize("Player Tax " + player.describeTo(fme, true)));
	    msg("<a>Faction: %s", player.getFaction().describeTo(fme, true));
	    msg("<a>Your Tax: %s", describeTax(taxPlayer));
	    msg("<a>Can Afford: %s", describeAffordTime(taxPlayer));
	}
    
    public static String describeTax(TaxPlayer taxPlayer) {
		return FormatUtil.parse("<h>%s<i> / <h>%s<i> in your account.", FormatUtil.formatMoney(taxPlayer.getTax()), FormatUtil.formatMoney(taxPlayer.getBalance()));
	}
	
	public static String describeAffordTime(TaxPlayer taxPlayer) {
		StringBuilder builder = new StringBuilder();
		if (taxPlayer.canAffordTax()) {
			builder.append(FormatUtil.YES);
		} else {
			builder.append(FormatUtil.NO);
		}
		builder.append(FormatUtil.parse("<i> "));
		if (taxPlayer.getTax() == 0) {
			builder.append("because there is no tax.");
		} else if (taxPlayer.getTax() < 0) {
			builder.append("because your faction is paying you.");
		} else if (taxPlayer.canAffordTax()) {
			builder.append("for the next ");
			builder.append(toStringAffordTime(taxPlayer.getAffordableTime()));
			builder.append(FormatUtil.parse("<i>."));
		} else {
			double owed = taxPlayer.getTax() - taxPlayer.getBalance();
			builder.append(FormatUtil.parse("<b>you owe <h>"));
			builder.append(owed);
			builder.append("!");
		}
		return builder.toString();
	}
	
	public static String toStringAffordTime(long affordableMill) {
		//Lets hope this works
	    LinkedHashMap<TimeUnit, Long> unitCounts = TimeDiffUtil.unitcounts(affordableMill, TimeUnit.getAllButMillis());
	    unitCounts = TimeDiffUtil.limit(unitCounts, 2);
		return TimeDiffUtil.formatedVerboose(unitCounts);
	}
}
