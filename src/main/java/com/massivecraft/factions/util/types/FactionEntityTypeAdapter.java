package com.massivecraft.factions.util.types;

import com.google.gson.*;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.FactionEntity;

import java.lang.reflect.Type;

public class FactionEntityTypeAdapter implements JsonDeserializer<FactionEntity>, JsonSerializer<FactionEntity> {

    @Override
    public FactionEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        String type = obj.get("type").getAsString();
        String id = obj.get("id").getAsString();

        FactionEntity entity;
        if (type.equalsIgnoreCase("f")) {
            entity = Factions.getInstance().getFactionById(id);
        } else if (type.equalsIgnoreCase("p")) {
            entity = FPlayers.getInstance().getById(id);
        } else {
            entity = null;
            P.p.log("Invalid FactionEntity type");
        }

        return entity;
    }

    @Override
    public JsonElement serialize(FactionEntity src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();

        if (src instanceof Faction) {
            obj.addProperty("type", "f");
        } else {
            obj.addProperty("type", "p");
        }
        obj.addProperty("id", src.getId());

        return obj;
    }
}
