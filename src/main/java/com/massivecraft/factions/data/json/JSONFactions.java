package com.massivecraft.factions.data.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFaction;
import com.massivecraft.factions.data.MemoryFactions;
import com.massivecraft.factions.util.DiscUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JSONFactions extends MemoryFactions {
    public Gson getGson() {
        return FactionsPlugin.getInstance().getGson();
    }

    private final File file;

    public File getFile() {
        return file;
    }

    // -------------------------------------------- //
    // CONSTRUCTORS
    // -------------------------------------------- //

    public JSONFactions() {
        if (FactionsPlugin.getInstance().getServerUUID() == null) {
            FactionsPlugin.getInstance().grumpException(new RuntimeException());
        }
        this.file = new File(FactionsPlugin.getInstance().getDataFolder(), "data/factions.json");
        this.nextId = 1;
    }

    public void forceSave() {
        forceSave(true);
    }

    public void forceSave(boolean sync) {
        final Map<String, JSONFaction> entitiesThatShouldBeSaved = new HashMap<>();
        for (Faction entity : this.factions.values()) {
            entitiesThatShouldBeSaved.put(entity.getId(), (JSONFaction) entity);
        }

        saveCore(file, entitiesThatShouldBeSaved, sync);
    }

    private boolean saveCore(File target, Map<String, JSONFaction> entities, boolean sync) {
        return DiscUtil.writeCatch(target, FactionsPlugin.getInstance().getGson().toJson(entities), sync);
    }

    public int load() {
        Map<String, JSONFaction> factions = this.loadCore();
        if (factions == null) {
            return 0;
        }
        this.factions.putAll(factions);

        super.load();
        return factions.size();
    }

    private Map<String, JSONFaction> loadCore() {
        if (!this.file.exists()) {
            return new HashMap<>();
        }

        String content = DiscUtil.readCatch(this.file);
        if (content == null) {
            return null;
        }

        Map<String, JSONFaction> data = FactionsPlugin.getInstance().getGson().fromJson(content, new TypeToken<Map<String, JSONFaction>>() {
        }.getType());

        this.nextId = 1;
        // Do we have any names that need updating in claims or invites?

        for (Entry<String, JSONFaction> entry : data.entrySet()) {
            String id = entry.getKey();
            Faction f = entry.getValue();
            f.checkPerms();
            f.setId(id);
            this.updateNextIdForId(id);
        }
        return data;
    }

    // -------------------------------------------- //
    // ID MANAGEMENT
    // -------------------------------------------- //

    public String getNextId() {
        while (!isIdFree(this.nextId)) {
            this.nextId += 1;
        }
        return Integer.toString(this.nextId);
    }

    public boolean isIdFree(String id) {
        return !this.factions.containsKey(id);
    }

    public boolean isIdFree(int id) {
        return this.isIdFree(Integer.toString(id));
    }

    protected synchronized void updateNextIdForId(int id) {
        if (this.nextId < id) {
            this.nextId = id + 1;
        }
    }

    protected void updateNextIdForId(String id) {
        try {
            int idAsInt = Integer.parseInt(id);
            this.updateNextIdForId(idAsInt);
        } catch (Exception ignored) {
        }
    }

    @Override
    public Faction generateFactionObject() {
        String id = getNextId();
        Faction faction = new JSONFaction(id);
        updateNextIdForId(id);
        return faction;
    }

    @Override
    public Faction generateFactionObject(String id) {
        return new JSONFaction(id);
    }

    @Override
    public void convertFrom(MemoryFactions old) {
        old.factions.forEach((tag, faction) -> this.factions.put(tag, new JSONFaction((MemoryFaction) faction)));
        this.nextId = old.nextId;
        forceSave();
        Factions.instance = this;
    }
}
