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
        String id = json.getAsString();

        FactionEntity entity;
        if (id.contains("-")) {
            // Because only player UUID's contain hyphens we are sure that it is a player
            entity = FPlayers.getInstance().getById(id);
        } else {
            entity = Factions.getInstance().getFactionById(id);
        }

        return entity;
    }

    @Override
    public JsonElement serialize(FactionEntity src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getId());
    }
}
