package com.massivecraft.factions.zcore.ui.items;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;

/**
 *  DynamicItem uses ItemUI merges to handle
 *  specific states in some UI menus, for example
 *
 *  PermissableActionUI handles individual actions
 *  that have states related to the user
 *  these can change depending on what the UI and the config
 *  provide to this class
 */
public class DynamicItem extends ItemUI {

    // May contain incomplete ItemUI but will be used to be merged into this main one
    private HashMap<String, ItemUI> states = new HashMap<>();

    public DynamicItem(Builder builder, ConfigurationSection section) {
        super(builder);
        for (String key : section.getKeys(false)) {
            // Build a map with all sub states, might be invalid but we won't access them anyways
            states.put(key.toLowerCase(), fromConfig(section.getConfigurationSection(key)));
        }
    }

    public DynamicItem(ItemUI itemUI) {
        super(itemUI);
        if (itemUI instanceof DynamicItem) {
            this.states = ((DynamicItem) itemUI).states;
        }
    }

    // Merges the ItemUI from the state into a clone of this (Clone or it might leave some data from other states)
    public ItemUI get(String stage) {
        ItemUI clone = new ItemUI(this);
        if (states.containsKey(stage)) {
            clone.merge(states.get(stage));
        }
        return clone;
    }

}
