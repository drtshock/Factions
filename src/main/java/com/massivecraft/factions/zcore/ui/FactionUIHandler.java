package com.massivecraft.factions.zcore.ui;

import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.ui.items.ItemUI;
import com.massivecraft.factions.zcore.ui.items.StagedItemUI;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

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
                ItemUI itemUI = ItemUI.fromConfig(section);
                if (itemUI != null) {
                    global.put(key, itemUI);
                }
            }
        }

        ConfigurationSection dummiesSection = p.getConfig().getConfigurationSection("ui.dummies");
        if (dummiesSection != null) {
            for (String key : dummiesSection.getKeys(false)) {
                ConfigurationSection section = dummiesSection.getConfigurationSection(key);
                ItemUI itemUI = ItemUI.fromConfig(section);
                if (itemUI != null && itemUI.isValid()) {
                    dummies.put(key, itemUI);
                }
            }
        }
    }

    public ItemUI mergeBase(String id, ConfigurationSection object) {
        ItemUI base = getBaseItem(id);
        if (base == null) {
            return null;
        }
        base.merge(ItemUI.fromConfig(object));
        return merge(base, object);
    }

    public ItemUI mergeDummyBase(String id, ConfigurationSection object) {
        ItemUI base = getDummyItem(id);
        return merge(base, object);
    }

    private ItemUI merge(ItemUI base, ConfigurationSection object) {
        if (base == null) {
            return null;
        }
        base.merge(ItemUI.fromConfig(object));
        return base;
    }

    public ItemUI getBaseItem(String id) {
        if (global.get(id) == null) {
            return null;
        } else {
            ItemUI itemUI = global.get(id);
            if (itemUI instanceof StagedItemUI) {
                return new StagedItemUI(itemUI);
            }
            return new ItemUI(itemUI);
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
