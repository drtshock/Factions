package com.massivecraft.factions.util.material.adapter;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.massivecraft.factions.util.material.FactionMaterial;

public class FactionMaterialAdapter extends TypeAdapter<FactionMaterial> {

    @Override
    public void write(JsonWriter out, FactionMaterial value) throws IOException {
        out.value(value.name());
    }

    @Override
    public FactionMaterial read(JsonReader in) throws IOException {
        return FactionMaterial.from(in.nextString());
    }

}
