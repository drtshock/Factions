package com.massivecraft.factions.tax.cmd;

import java.util.UUID;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.tax.TaxConfig;
import com.massivecraft.factions.tax.TaxFaction;

public class CmdTaxSet extends FCommand {
	public CmdTaxSet() {
		aliases.add("s");
		aliases.add("set");
		requiredArgs.add("amount|none");
		optionalArgs.put("default|rank|player|all", "default");
		optionalArgs.put("faction", "yours");
		setHelpShort("Manage a faction's tax");
		//TODO full help
		senderMustBeAdmin = true;
	}
	@Override
	public void perform() {
		if (senderIsConsole && !argIsSet(3)) {
			msg("Console must specify faction");
			return;
		}
		Faction faction = myFaction;
		if (argIsSet(3)) {
			faction = argAsFaction(3);
		}
		double tax;
		if (argAsString(1).equalsIgnoreCase("none")) {
			tax = 0;
		} else if (argAsDouble(1) == null) {
			msg("%s is not a valid number", argAsString(1));
			msg("tax must be none or a number");
			return;
		} else {
			tax = argAsDouble(1);
		}
		
		if (tax < TaxConfig.getMinimumTax()) {
			msg("%s is less than minimum tax", tax);
			return;
		}
		
		if (tax < 0) msg("<b>WARNING: <i>This negative tax will work like a salary!");
		
		TaxFaction taxFaction = TaxFaction.getTaxFaction(faction);
		
		
		if (!argIsSet(2) || argAsString(2).equalsIgnoreCase("default")) {
			
		} else if (argAsFPlayer(2) != null) {
			FPlayer toSet = argAsFPlayer(2);
			taxFaction.getTaxRules().setPlayerTax(toSet, tax);
			msg("Set %s's tax to %s", toSet.getName(), tax);
		} else if (isValidRank(argAsString(2))){
			Role rank = asRank(argAsString(2));
			taxFaction.getTaxRules().setRankTax(rank, tax);
			msg("All %ss now pay %s", roleToString(rank), tax);
		} else if (argAsString(2).equalsIgnoreCase("all")) {
			taxFaction.getTaxRules().clear();
			taxFaction.getTaxRules().setDefaultTax(tax);
			msg("Everyone now pays %s in tax", tax);
		} else {
			msg("%s Must be a player, a rank, default, or all!", argAsString(2));
			return;
		}
	}
	
	public static boolean isValidRank(String rank) {
		return asRank(rank) != null;
	}
	
	public static Role asRank(String rank) {
		if (rank.equalsIgnoreCase("member")) {
			return Role.NORMAL;
		} else if (rank.equalsIgnoreCase("owner")) {
			return Role.ADMIN;
		} else if (rank.equalsIgnoreCase("mod") || rank.equalsIgnoreCase("moderator")) {
			return Role.MODERATOR;
		} else {
			return null;
		}
	}
	
	public static String roleToString(Role rank) {
		switch (rank) {
		case NORMAL : return "member";
		case ADMIN : return "owner";
		case MODERATOR : return "moderator";
		default : return "unknown";
		}
	}
}
