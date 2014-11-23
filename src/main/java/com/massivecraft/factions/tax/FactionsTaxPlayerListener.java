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
		Player player = event.getPlayer();
		TaxPlayer taxPlayer = TaxPlayer.getTaxPlayer(player);
		TaxFaction taxFaction = taxPlayer.getFaction();
		if (tax.isGracePeriod()) {
			player.sendMessage(Conf.graceMsgTitle);
			player.sendMessage(Conf.graceMsgBody);
		} else {
			if (!taxPlayer.canAfford(Conf.safeTaxPeriodsTillWarning)) {
				player.sendMessage(Conf.taxCantAffordMsg);
			}
			if (!taxFaction.canAfford(Conf.safeUpkeepPeriodsTillWarning)) {
				player.sendMessage(Conf.upkeepCantAffordMsg);
			}
		}
	}
}

