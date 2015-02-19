
package com.massivecraft.factions.tax.cmd;

import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCommand;
import com.massivecraft.factions.tax.FactionsTax;

public class CmdTax extends FCommand {
	public CmdTaxFaction taxFaction = new CmdTaxFaction();
	public CmdTaxInfo taxInfo = new CmdTaxInfo();
	public CmdTaxPlayer taxPlayer = new CmdTaxPlayer();
	public CmdTaxSet taxSet = new CmdTaxSet();
	
	
	public CmdTax() {
		this.aliases.add("tax");
		
		this.setHelpShort("Faction tax commands");
		this.helpLong.add(p.txt.parseTags("<i>The Faction Tax Commands."));
		
		senderMustBePlayer = false;
		senderMustBeMember = false;
		senderMustBeModerator = false;
		senderMustBeAdmin = false;

		this.addSubCommand(taxFaction);
		this.addSubCommand(taxInfo);
		this.addSubCommand(taxPlayer);
		this.addSubCommand(taxSet);
	}
	
	@Override
	public boolean isEnabled() {
		if (!super.isEnabled()) return false;
		if (!FactionsTax.getInstance().isEnabled()) {
			msg("<b>Factions Tax Is Not Enabled");
			return false;
		}
		return true;
	}
	
	
	@Override
	public void perform() {
		commandChain.add(this);
		P.p.cmdAutoHelp.execute(sender, args, commandChain);
	}

}
