package com.massivecraft.factions.tax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.P;
import com.massivecraft.factions.tax.format.FormatUtil;
import com.massivecraft.factions.zcore.util.TextUtil;

/**
 * TaxTask checks whether or not it is time to tax (as specified in config) and if so starts taxing everyone
 * 
 * First it charges all players their taxes as specified by faction tax rules
 * If the player can't play it will kick them if the faction has that punishment set
 * Next it will deposit the taxes the player has paid into the faction account
 * 
 * After it charges the players taxes it will begin to charge the factions upkeep
 * First it charges all factions their upkeep unless they are exempt from upkeep
 * If the faction can't pay it will execute the punishment specified in the config file
 * 
 * @author Techcable
 *
 */
public class TaxTask extends BukkitRunnable {
	public static final long DEFAULT_PERIOD = 25; //1 and 1/4 tick
	private final FactionsTax tax;
	public TaxTask(FactionsTax tax) {
		this.tax = tax;
	}
	
	@Override
	public void run() {
		if (tax.isGracePeriod()) tax.checkGracePeriod(); //Update Grace period status
		if (!tax.isGracePeriod() && isTaxTime()) {
			long startMill = System.currentTimeMillis();
			int numCharged = 0;
			int numPunished = 0;
			for (TaxPlayer taxPlayer : getPlayersToChargeTax()) {
				if (taxPlayer.canAffordTax()) {
					taxPlayer.charge(taxPlayer.getTax());
					taxPlayer.msg(FormatUtil.parse("Your faction charged you %s in taxes.", FormatUtil.formatMoney(taxPlayer.getTax())));
					numCharged++;
					taxPlayer.getFaction().deposit(-taxPlayer.getTax());
					debug(FormatUtil.parse("%s charged %s %s in taxes", taxPlayer.getFaction().getFaction().getTag(), taxPlayer.getPlayer().getName(), FormatUtil.formatMoney(taxPlayer.getTax())));
				} else {
					taxPlayer.punish();
					numPunished++;
				}
			}
			debug(FormatUtil.parse("Finished charging %s factions upkeep.", numCharged));
			debug(FormatUtil.parse("%s factions were punished because they couldn't pay upkeep.", numPunished));
			numCharged = 0;//Start counting factions
			numPunished = 0;
			for (TaxFaction taxFaction : getFactionsToChargeUpkeep()) {
				if (taxFaction.canAffordUpkeep()) {
					taxFaction.charge(taxFaction.getUpkeep());
					taxFaction.msgAll("Charged %s in upkeep to your faction", FormatUtil.formatMoney(taxFaction.getUpkeep()));
					numCharged++;
					debug(FormatUtil.parse("Charged %s to faction %s in upkeep", FormatUtil.formatMoney(taxFaction.getUpkeep()), taxFaction.getFaction().getTag()));
				} else {
					taxFaction.punish();
					numPunished++;
				}
			}
			debug(FormatUtil.parse("Finished charging %s players taxes.", numCharged));
			debug(FormatUtil.parse("%s players were punished because they couldn't pay upkeep", numPunished));
			debug(FormatUtil.parse("Taxing finished in %s seconds", (System.currentTimeMillis() - startMill) / 1000));
			updateLastTax();
		}
	}
	
	public List<TaxFaction> getFactionsToChargeUpkeep() {
		List<TaxFaction> factionsToChargeUpkeep = new ArrayList<TaxFaction>();
		for (Faction faction : Factions.getInstance().getAllFactions()) {
			if (!faction.isNormal()) continue;
			TaxFaction taxFaction = TaxFaction.getTaxFaction(faction);
			if (taxFaction.shouldPayUpkeep()) {
				factionsToChargeUpkeep.add(taxFaction);
			} else {
				debug(FormatUtil.parse("%s has upkeep disabled so we aren't chargeing them upkeep", faction.getTag()));
			}
		}
		return factionsToChargeUpkeep;
	}
	
	public List<TaxPlayer> getPlayersToChargeTax() {
		List<TaxPlayer> playersToChargeTax = new ArrayList<TaxPlayer>();
		for (FPlayer player : FPlayers.getInstance().getAllFPlayers()) {
			TaxPlayer taxPlayer = TaxPlayer.getTaxPlayer(player);
			if (!taxPlayer.getPlayer().hasFaction()) continue;
			double tax = taxPlayer.getTax();
			if (tax == 0) continue;
			playersToChargeTax.add(taxPlayer);
		}
		return playersToChargeTax;
	}
	
	public long getPeriod() {
		return DEFAULT_PERIOD;
	}
	
	public void debug(String msg) {
		P.p.debug(msg);
	}
	
	public boolean isTaxTime() {
		return (Conf.lastTaxMill + Conf.taxPeriodMill) <= System.currentTimeMillis();
	}
	
	public void updateLastTax() {
		Conf.lastTaxMill = System.currentTimeMillis();
	}
}