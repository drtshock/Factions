package com.massivecraft.factions.util.types;

import com.google.gson.*;
import com.massivecraft.factions.FLocation;

import java.lang.reflect.Type;

public class ChunkLocationTypeAdapter implements JsonDeserializer<FLocation>, JsonSerializer<FLocation> {
    @Override
    public FLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return FLocation.fromString(json.getAsString());
    }

    @Override
    public JsonElement serialize(FLocation src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getWorldName() + "," + src.getCoordString());
    }
}
