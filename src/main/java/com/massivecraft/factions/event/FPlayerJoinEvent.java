package com.massivecraft.factions.event;

import org.bukkit.event.Cancellable;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;

/**
 * Event called when an FPlayer joins a Faction.
 */
public class FPlayerJoinEvent extends FactionPlayerEvent implements Cancellable {

    PlayerJoinReason reason;
    boolean cancelled = false;

    public enum PlayerJoinReason {
        CREATE, LEADER, COMMAND
    }

    public FPlayerJoinEvent(FPlayer fp, Faction f, PlayerJoinReason r) {
        super(f, fp);
        reason = r;
    }

    /**
     * Get the reason the player joined the faction.
     *
     * @return reason player joined the faction.
     */
    public PlayerJoinReason getReason() {
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