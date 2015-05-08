package com.massivecraft.factions.api_impl;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.techcable.factionsapi.FactionsAPI;
import net.techcable.factionsapi.IFactionsPlugin;

import com.massivecraft.factions.Factions;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FPlayer;

public class Factions16UPlugin implements IFactionsPlugin {
    
    private final Map<FPlayer, F16UPlayer> playerMap = new WeakHashMap<FPlayer, F16UPlayer>();
    
    public F16UPlayer getFPlayer(FPlayer player) {
        if (player == null) return null;
        if (!playerMap.containsKey(player)) {
            playerMap.put(player, new F16UPlayer(player));
        }
        return playerMap.get(player);
    }
    
    public F16UPlayer getFPlayer(UUID id) {
        FPlayer player = FPlayers.getInstance().getById(id.toString());
        return getFPlayer(id);
    }

    public F16UPlayer getFPlayer(OfflinePlayer player) {
        return getFPlayer(player.getUniqueId());
    }

    public F16UPlayer getFPlayer(Player player) {
        return getFPlayer((OfflinePlayer)player);
    }

    private final Map<Faction, F16UFaction> factionMap = new WeakHashMap<Faction, F16UFaction>();
    public F16UFaction getFaction(Faction faction) {
        if (faction == null) return null;
        if (!factionMap.containsKey(faction)) {
            factionMap.put(faction, new F16UFaction(faction));
        }
        return factionMap.get(faction);
    }

    public F16UFaction getFaction(String id) {
        Faction faction = Factions.getInstance().getFactionById(id);
        return getFaction(faction);
    }
    
    public F16UFaction getFactionByTag(String tag) {
        Faction faction = Factions.getInstance().getByTag(tag);
        return getFaction(faction);
    }

    public F16UFaction getSafezone() {
        return getFaction(Factions.getInstance().getSafeZone());
    }

    public F16UFaction getWarzone() {
        return getFaction(Factions.getInstance().getWarZone());
    }

    public F16UFaction getNone() {
        return getFaction(Factions.getInstance().getNone());
    }

    public ICustomFlag getOrCreateCustomFlag(String name) {
        throw new UnsupportedOperationException("FactionsUUID doesn't support custom flags");
    }

    public ICustomFlag getCustomFlag(String name) {
        throw new UnsupportedOperationException("FactionsUUID doesn't support custom flags");
    }
    
    public static Factions16UPlugin getInstance() {
        return (Factions16UPlugin) FactionsAPI.getImplemenetation();
    }
}