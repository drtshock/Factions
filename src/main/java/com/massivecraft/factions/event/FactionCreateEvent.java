package com.massivecraft.factions.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;

/**
 * Event called when a Faction is created.
 */
public class FactionCreateEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private String factionTag;
    private boolean cancelled;

    public FactionCreateEvent(Player sender, String tag) {
    	super(sender);
        this.factionTag = tag;
    }

    public FPlayer getFPlayer() {
        return FPlayers.getInstance().getByPlayer(getPlayer());
    }

    public String getFactionTag() {
        return factionTag;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}