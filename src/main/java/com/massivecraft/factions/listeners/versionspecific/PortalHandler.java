package com.massivecraft.factions.listeners.versionspecific;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Relation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PortalHandler implements PortalListenerBase {
    public boolean shouldCancel(Location location, Player player) {
        if (!P.p.getConfig().getBoolean("portals.limit", false)) {
            return false; // Don't do anything if they don't want us to.
        }
        FLocation loc = new FLocation(location);
        Faction faction = Board.getInstance().getFactionAt(loc);
        if (faction.isWilderness()) {
            return false; // We don't care about wilderness.
        } else if (!faction.isNormal() && !player.isOp()) {
            // Don't let non ops make portals in safezone or warzone.
            return true;
        }

        FPlayer fp = FPlayers.getInstance().getByPlayer(player);
        String mininumRelation = P.p.getConfig().getString("portals.minimum-relation", "MEMBER"); // Defaults to Neutral if typed wrong.
        return !fp.getFaction().getRelationTo(faction).isAtLeast(Relation.fromString(mininumRelation));
    }
}
