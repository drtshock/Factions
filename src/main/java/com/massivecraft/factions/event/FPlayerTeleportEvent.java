package com.massivecraft.factions.event;

import com.massivecraft.factions.FPlayer;
import org.bukkit.event.Cancellable;

public class FPlayerTeleportEvent extends FactionPlayerEvent implements Cancellable {

    private FPlayerTeleportEvent.PlayerTeleportReason reason;
    boolean cancelled = false;

    public enum PlayerTeleportReason {
        HOME, AHOME, WARP, STUCK
    }

    public FPlayerTeleportEvent(FPlayer p, FPlayerTeleportEvent.PlayerTeleportReason r) {
        super(p.getFaction(), p);
        reason = r;
    }

    /**
     * Get the reason the player is teleporting.
     *
     * @return reason player is teleporting.
     */
    public FPlayerTeleportEvent.PlayerTeleportReason getReason() {
        return reason;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean c) {
        cancelled = c;
    }

}
