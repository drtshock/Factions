package com.massivecraft.factions.tax.cmd;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.cmd.FCommand;

public class CmdTaxSetKick extends FCommand {
	public CmdTaxSetKick() {
		aliases.add("k");
		aliases.add("setkick");
		senderMustBeAdmin = true;
		requiredArgs.add("on|off");
		optionalArgs.put("faction", "yours");
		setHelpShort("Set wether faction kick non paying members");
		helpLong.add("Set wether a faction kicks its non paying members");
	}
	@Override
	public void perform() {
		if (senderIsConsole && !argIsSet(2)) {
			msg("Console must specify faction");
			return;
		}
		Faction faction = myFaction;
		if (argIsSet(2)) {
			faction = argAsFaction(1);
		}
		boolean kick;
		if (argAsString(1).equalsIgnoreCase("on")) {
			kick = true;
		} else if (argAsString(1).equalsIgnoreCase("off")) {
			kick = false;
		} else {
			msg("%s isn't a valid option", argAsString(1));
			msg("Please specifiy on or off");
			return;
		}
		faction.getTaxRules().setKickNotPaying(kick);
		if (kick) {
			msg("Non paying members are now kicked");
		} else {
			msg("If you don't pay your taxes you stay in the faction");
		}
	}

}
