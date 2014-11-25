package com.massivecraft.factions.tax;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayers;

public class FactionsTaxPlayerListener implements Listener {
	
	public FactionsTaxPlayerListener(FactionsTax tax) {
		this.tax = tax;
	}
	
	private final FactionsTax tax;

	public FactionsTax getTax() {
		return tax;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		TaxPlayer player = TaxPlayer.getTaxPlayer(event.getPlayer());
		TaxFaction taxFaction = player.getFaction();
		if (tax.isGracePeriod()) {
			player.msg(Conf.graceMsgTitle);
			player.msg(Conf.graceMsgBody);
		} else {
			if (!player.canAffordTax(Conf.safeTaxPeriodsTillWarning)) {
				player.msg(Conf.taxCantAffordMsg);
			}
			if (!taxFaction.canAffordUpkeep(Conf.safeUpkeepPeriodsTillWarning)) {
				player.msg(Conf.upkeepCantAffordMsg);
			}
		}
	}
}

