package com.massivecraft.factions.zcore.fperms.gui;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;

public interface PermissionGUI {

    public void onClick(int slot, ClickType action);

    public enum SpecialItem {
        BACK,
        RELATION;

        static boolean isSpecial(String string) {
            return fromString(string) != null;
        }

        static SpecialItem fromString(String string) {
            for (SpecialItem specialItem : SpecialItem.values()) {
                if (string.equalsIgnoreCase(specialItem.name())) {
                    return specialItem;
                }
            }
            return null;
        }
    }

}
