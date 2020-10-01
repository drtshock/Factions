package com.massivecraft.factions;

import com.massivecraft.factions.data.json.JSONFPlayers;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;

public abstract class FPlayers {
    protected static FPlayers instance = getFPlayersImpl();

    public abstract void clean();

    public static FPlayers getInstance() {
        return instance;
    }

    private static FPlayers getFPlayersImpl() {
        // TODO switch on configuration backend
        return new JSONFPlayers();
    }

    public abstract Collection<FPlayer> getOnlinePlayers();

    public abstract FPlayer getByPlayer(Player player);

    public abstract Collection<FPlayer> getAllFPlayers();

    public abstract void forceSave();

    public abstract void forceSave(boolean sync);

    public abstract FPlayer getByOfflinePlayer(OfflinePlayer player);

    @Deprecated
    public abstract FPlayer getById(String string);

    public abstract FPlayer getById(UUID uuid);

    public abstract int load();

    public abstract void remove(FPlayer player);
}
