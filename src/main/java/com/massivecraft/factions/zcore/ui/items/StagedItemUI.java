package com.massivecraft.factions.zcore.ui.items;

import org.bukkit.configuration.ConfigurationSection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class StagedItemUI extends ItemUI {

    // May contain incomplete ItemUI but will be used to be merged into this main one
    private HashMap<String, ItemUI> stages = new HashMap<>();

    public StagedItemUI(Builder builder, StageType type, ConfigurationSection section) {
        super(builder);
        for (String key : section.getKeys(false)) {
            if (type.exists(key)) {
                stages.put(key.toLowerCase(), fromConfig(section.getConfigurationSection(key)));
            }
        }
    }

    public StagedItemUI(ItemUI itemUI) {
        super(itemUI);
        if (itemUI instanceof StagedItemUI) {
            this.stages = ((StagedItemUI) itemUI).stages;
        }
    }

    // Merges the ItemUI from the stage into a clone of this (Clone or it might leave some data from other stages)
    public ItemUI get(String stage) {
        ItemUI clone = new ItemUI(this);
        if (stages.containsKey(stage)) {
            clone.merge(stages.get(stage));
        }
        return clone;
    }

    public enum StageType {
        WARP("exist", "non_exist", "password"),
        ACCESS("allow", "deny", "undefined")
        ;

        private List<String> stages;
        StageType(String... stages) {
            this.stages = Arrays.asList(stages);
        }

        public List<String> getStages() {
            return stages;
        }

        public boolean exists(String check) {
            for (String stage : getStages()) {
                if (check.equalsIgnoreCase(stage)) {
                    return true;
                }
            }
            return false;
        }

        public static StageType fromString(String string) {
            for (StageType type : StageType.values()) {
                if (type.name().equalsIgnoreCase(string)) {
                    return type;
                }
            }
            return null;
        }

    }

}
