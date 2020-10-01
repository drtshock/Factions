package com.massivecraft.factions.data;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

public abstract class MemoryFPlayers extends FPlayers {
    protected Map<UUID, FPlayer> fPlayers = new ConcurrentSkipListMap<>();

    public void clean() {
        for (FPlayer fplayer : this.fPlayers.values()) {
            if (!Factions.getInstance().isValidFactionId(fplayer.getFactionId())) {
                FactionsPlugin.getInstance().log("Reset faction data (invalid faction:" + fplayer.getFactionId() + ") for player " + fplayer.getName());
                fplayer.resetFactionData(false);
            }
        }
    }

    public Collection<FPlayer> getOnlinePlayers() {
        Set<FPlayer> entities = new HashSet<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            entities.add(this.getByPlayer(player));
        }
        return entities;
    }

    @Override
    public FPlayer getByPlayer(Player player) {
        return getById(player.getUniqueId());
    }

    @Override
    public List<FPlayer> getAllFPlayers() {
        return new ArrayList<>(fPlayers.values());
    }

    @Override
    public FPlayer getByOfflinePlayer(OfflinePlayer player) {
        return getById(player.getUniqueId());
    }

    @Deprecated
    @Override
    public FPlayer getById(String id) {
        return getById(UUID.fromString(id));
    }

    @Override
    public FPlayer getById(UUID id) {
        FPlayer player = fPlayers.get(id);
        if (player == null) {
            player = generateFPlayer(id);
        }
        return player;
    }

    @Override
    public void remove(FPlayer player) {
        this.fPlayers.remove(player.getUniqueId());
    }

    protected abstract FPlayer generateFPlayer(UUID id);

    public abstract void convertFrom(MemoryFPlayers old);
}
