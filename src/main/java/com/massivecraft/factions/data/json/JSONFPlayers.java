package com.massivecraft.factions.data.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFPlayer;
import com.massivecraft.factions.data.MemoryFPlayers;
import com.massivecraft.factions.util.DiscUtil;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

public class JSONFPlayers extends MemoryFPlayers {
    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    @Deprecated
    public void setGson(Gson gson) {
        // NOOP
    }

    private final File file;

    public JSONFPlayers() {
        if (FactionsPlugin.getInstance().getServerUUID() == null) {
            FactionsPlugin.getInstance().grumpException(new RuntimeException());
        }
        file = new File(FactionsPlugin.getInstance().getDataFolder(), "data/players.json");
    }

    public void convertFrom(MemoryFPlayers old) {
        old.getAllFPlayers().forEach((player) -> this.fPlayers.put(player.getUniqueId(), new JSONFPlayer((MemoryFPlayer) player)));
        forceSave();
        FPlayers.instance = this;
    }

    public void forceSave() {
        forceSave(true);
    }

    public void forceSave(boolean sync) {
        final Map<UUID, JSONFPlayer> entitiesThatShouldBeSaved = new HashMap<>();
        for (FPlayer entity : this.fPlayers.values()) {
            if (((MemoryFPlayer) entity).shouldBeSaved()) {
                entitiesThatShouldBeSaved.put(entity.getUniqueId(), (JSONFPlayer) entity);
            }
        }

        saveCore(file, entitiesThatShouldBeSaved, sync);
    }

    private boolean saveCore(File target, Map<UUID, JSONFPlayer> data, boolean sync) {
        return DiscUtil.writeCatch(target, FactionsPlugin.getInstance().getGson().toJson(data), sync);
    }

    public int load() {
        Map<UUID, JSONFPlayer> fplayers = this.loadCore();
        if (fplayers == null) {
            return 0;
        }
        this.fPlayers.clear();
        this.fPlayers.putAll(fplayers);
        return fPlayers.size();
    }

    private Map<UUID, JSONFPlayer> loadCore() {
        if (!this.file.exists()) {
            return new HashMap<>();
        }

        String content = DiscUtil.readCatch(this.file);
        if (content == null) {
            return null;
        }

        Map<UUID, JSONFPlayer> data = FactionsPlugin.getInstance().getGson().fromJson(content, new TypeToken<Map<UUID, JSONFPlayer>>() {
        }.getType());
        Set<String> list = new HashSet<>();
        Set<String> invalidList = new HashSet<>();
        for (Entry<UUID, JSONFPlayer> entry : data.entrySet()) {
            UUID key = entry.getKey();
            entry.getValue().setUniqueId(key);
        }

        return data;
    }

    private boolean doesKeyNeedMigration(String key) {
        if (!key.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            // Not a valid UUID..
            // Valid playername, we'll mark this as one for conversion
            // to UUID
            return key.matches("[a-zA-Z0-9_]{2,16}");
        }
        return false;
    }

    private boolean isKeyInvalid(String key) {
        return !key.matches("[a-zA-Z0-9_]{2,16}");
    }

    @Override
    public FPlayer generateFPlayer(UUID id) {
        FPlayer player = new JSONFPlayer(id);
        this.fPlayers.put(id, player);
        return player;
    }
}
