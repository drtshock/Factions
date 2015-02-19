package com.massivecraft.factions.tax;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.P;

public class FactionsTaxPlayerListener implements Listener {
	
	public static String GRACE_MESSAGE_TITLE = "FactionsTax Enabled";
	public static String[] GRACE_MESSAGE = new String[] {"FactionsTax was Just Enabled", "Type /f tax to see commands", "Type /f tax info for info", "This notice will dissapear once taxing starts"};
	public static String[] CANT_AFFORD_TAX_MESSAGE = new String[] {"You can't afford another week of taxes", "Type /f tax player for more info", "Earn More Money to pay your taxes"};
	public static String[] CANT_AFFORD_UPKEEP_MESSAGE = new String[] {"Your faction can't afford another week of upkeep", "Someone should type /f money deposit to deposit money in the faction bank"};
	
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
			player.msg(P.p.txt.titleize(GRACE_MESSAGE_TITLE));
			player.msg(GRACE_MESSAGE);
		} else {
			if (!player.canAffordTax(TaxConfig.getTaxPeriodsTillWarning())) {
				player.msg(CANT_AFFORD_TAX_MESSAGE);
			}
			if (!taxFaction.canAffordUpkeep(TaxConfig.getUpkeepPeriodsTillWarning())) {
				player.msg(CANT_AFFORD_UPKEEP_MESSAGE);
			}
		}
	}
}

