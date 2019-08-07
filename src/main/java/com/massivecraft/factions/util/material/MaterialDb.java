package com.massivecraft.factions.util.material;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.P;
import org.bukkit.Material;

public class MaterialDb {

    /*

    This utility has no concept of block metadata, converts if necessary 1.13
    material names to < 1.12 materials, or keeps 1.13 materials.

    Useful as we don't really need extra metadata for stuff like territory block breaking checking.

        "ACACIA_BOAT": {
            "material": "ACACIA_BOAT",
            "legacy": "BOAT_ACACIA"
        }

     */

    private static MaterialDb instance;

    public boolean legacy = true;
    public MaterialProvider provider;

    private MaterialDb() {
    }

    public Material get(String name) {
        return provider.resolve(name);
    }

    public static void load() {
        instance = new MaterialDb();
        instance.legacy = P.getVersion() < 1300; // Before 1.13
        P.p.getLogger().info(String.format("Using legacy support for materials: %s", instance.legacy));

        InputStreamReader reader = new InputStreamReader(P.p.getResource("materials.json"));
        Type typeToken = new TypeToken<HashMap<String, MaterialProvider.MaterialData>>() {
        }.getType();
        HashMap<String, MaterialProvider.MaterialData> materialData = P.p.gson.fromJson(reader, typeToken);
        P.p.getLogger().info(String.format("Found %s material mappings in the materials.json file.", materialData.keySet().size()));
        instance.provider = new MaterialProvider(materialData);
    }

    public void test() {
        // TODO: Do some Material tests
    }

    public static MaterialDb getInstance() {
        return instance;
    }

}
