package com.massivecraft.factions.zcore.ui;

import com.massivecraft.factions.P;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.logging.Level;

public class FactionUIHandler {

    private P p;

    private HashMap<String, ItemUI> global = new HashMap<>();
    private HashMap<String, ItemUI> dummies = new HashMap<>();

    // Is in charge of loading in the global items and dummy items into a memory map
    public FactionUIHandler(P p) {
        this.p = p;
        build();
    }

    public void build() {
        // Globals
        ConfigurationSection globalSection = p.getConfig().getConfigurationSection("ui.global");
        if (globalSection != null) {
            for (String key : globalSection.getKeys(false)) {
                ConfigurationSection section = globalSection.getConfigurationSection(key);
                ItemUI itemUI = ItemUI.fromConfigSection(section);
                if (itemUI != null) {
                    global.put(key, itemUI);
                }
            }
        }

        ConfigurationSection dummiesSection = p.getConfig().getConfigurationSection("ui.dummies");
        if (dummiesSection != null) {
            for (String key : dummiesSection.getKeys(false)) {
                ConfigurationSection section = dummiesSection.getConfigurationSection(key);
                ItemUI itemUI = ItemUI.fromConfigSection(section);
                if (itemUI != null) {
                    dummies.put(key, itemUI);
                }
            }
        }
    }

    public ItemUI mergeBase(String id, ConfigurationSection object) {
        ItemUI base = global.get(id);
        return merge(base, object);
    }

    public ItemUI mergeDummyBase(String id, ConfigurationSection object) {
        ItemUI base = dummies.get(id);
        return merge(base, object);
    }

    private ItemUI merge(ItemUI base, ConfigurationSection object) {
        if (base == null) {
            return null;
        }
        ItemUI merge = new ItemUI(base);
        if (object.isString("name")) {
            merge.setName(object.getString("name"));
        }
        if (object.isList("lore")) {
            merge.setLore(object.getStringList("lore"));
        }
        if (object.isString("material")) {
            Material material = Material.matchMaterial(object.getString("material"));
            if (material != null) {
                merge.setMaterial(material);
            }
        }
        if (object.isString("color")) {
            String colorName = object.getString("color");
            try {
                merge.setColor(DyeColor.valueOf(colorName));
            } catch (IllegalArgumentException e) {
                P.p.log(Level.WARNING, "Invalid Color: " + colorName);
            }
        }

        return merge;
    }

    public ItemUI getBaseItem(String id) {
        if (global.get(id) == null) {
            return null;
        } else {
            return new ItemUI(global.get(id));
        }
    }

    public ItemUI getDummyItem(String id) {
        if (dummies.get(id) == null) {
            return null;
        } else {
            return new ItemUI(dummies.get(id));
        }
    }

}
