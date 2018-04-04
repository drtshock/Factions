package com.massivecraft.factions.util;

import com.google.gson.*;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fupgrade.FUpgrade;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class UpgradeMapTypeAdapter implements JsonSerializer<HashMap<FUpgrade, Integer>>, JsonDeserializer<HashMap<FUpgrade, Integer>> {

    @Override
    public HashMap<FUpgrade, Integer> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            JsonObject obj = json.getAsJsonObject();
            HashMap<FUpgrade, Integer> upgrades = new HashMap<>();

            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                FUpgrade upgrade = P.p.factionUpgrades.fromString(entry.getKey());
                int level = entry.getValue().getAsInt();
                upgrades.put(upgrade, level);
            }

            return upgrades;
        } catch (Exception ex) {
            ex.printStackTrace();
            P.p.log(Level.WARNING, "Error encountered while deserializing Faction Upgrades.");
            return null;
        }
    }

    @Override
    public JsonElement serialize(HashMap<FUpgrade, Integer> upgrades, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj = new JsonObject();

        try {
            for (Map.Entry<FUpgrade, Integer> entry : upgrades.entrySet()) {
                obj.addProperty(entry.getKey().name(), entry.getValue());
            }

            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            P.p.log(Level.WARNING, "Error encountered while serializing Faction Upgrades.");
            return obj;
        }
    }

}
