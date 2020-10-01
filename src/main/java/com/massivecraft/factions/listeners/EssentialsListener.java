package com.massivecraft.factions.listeners;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.event.FPlayerLeaveEvent;
import net.ess3.api.InvalidWorldException;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.logging.Level;

public class EssentialsListener implements Listener {

    private final IEssentials ess;

    public EssentialsListener(IEssentials essentials) {
        this.ess = essentials;
    }

    @EventHandler
    public void onLeave(FPlayerLeaveEvent event) throws Exception {
        // Get the USER from their UUID.
        Faction faction = event.getFaction();
        User user = ess.getUser(event.getfPlayer().getUniqueId());
        if (user == null) {
            FactionsPlugin.getInstance().log(Level.WARNING, "Attempted to remove Essentials homes for " + event.getfPlayer().getName() + " " +
                    "but no Essentials data at all was found for this user. " +
                    "This may be a bug in Essentials, or may be that the player only played prior to adding Essentials to the server");
            return;
        }

        List<String> homes = user.getHomes();
        if (homes == null || homes.isEmpty()) {
            return;
        }

        // Not a great way to do this on essential's side.
        for (String homeName : user.getHomes()) {

            Location loc;
            try {
                loc = user.getHome(homeName);
            } catch (InvalidWorldException e) {
                // This can throw an exception for some reason.
                FactionsPlugin.getInstance().getLogger().warning("Tried to check on home \"" + homeName + "\" for user \"" + event.getfPlayer().getName() + "\" but Essentials said world \"" + e.getWorld() + "\" does not exist. Skipping it.");
                continue;
            }
            FLocation floc = new FLocation(loc);

            Faction factionAt = Board.getInstance().getFactionAt(floc);
            // We're only going to remove homes in territory that belongs to THEIR faction.
            if (factionAt.equals(faction) && factionAt.isNormal()) {
                user.delHome(homeName);
                FactionsPlugin.getInstance().log(Level.INFO, "FactionLeaveEvent: Removing home %s, player %s, in territory of %s",
                        homeName, event.getfPlayer().getName(), faction.getTag());
            }
        }
    }
}
