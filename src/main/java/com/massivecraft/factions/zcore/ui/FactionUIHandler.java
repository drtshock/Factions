package com.massivecraft.factions.zcore.ui;

import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.ui.items.DynamicItem;
import com.massivecraft.factions.zcore.ui.items.ItemUI;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

/**
 *  Loads and caches config values as ItemUI
 *  instances, and then provides them to UI menus
 */
public class FactionUIHandler {

    private static FactionUIHandler uiHandler;

    private HashMap<String, ItemUI> global = new HashMap<>();
    private HashMap<String, ItemUI> dummies = new HashMap<>();

    public static void start() {
        uiHandler = new FactionUIHandler();
        uiHandler.build();
    }

    public static FactionUIHandler instance() {
        return uiHandler;
    }

    public void build() {
        // Globals
        ConfigurationSection globalSection = P.p.getConfig().getConfigurationSection("ui.global");
        if (globalSection != null) {
            for (String key : globalSection.getKeys(false)) {
                ConfigurationSection section = globalSection.getConfigurationSection(key);
                ItemUI itemUI = ItemUI.fromConfig(section);
                if (itemUI != null) {
                    global.put(key, itemUI);
                }
            }
        }

        // Dummies
        ConfigurationSection dummiesSection = P.p.getConfig().getConfigurationSection("ui.dummies");
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
            if (itemUI instanceof DynamicItem) {
                return new DynamicItem(itemUI);
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
