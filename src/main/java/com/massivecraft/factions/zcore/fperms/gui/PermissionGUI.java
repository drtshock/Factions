package com.massivecraft.factions.zcore.fperms.gui;

public interface PermissionGUI {

    public void onClick(int slot);

    public enum SpecialItem {
        BACK,
        RELATION,
        ACTION;

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
