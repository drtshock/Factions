package com.massivecraft.factions.zcore.fperms.gui;

public interface PermissionGUI {

    public void onClick(int slot);

    public enum SpecialItem {
        BACK,
        RELATION,
        ACTION;

        static boolean isSpecial(String string) {
            for (SpecialItem specialItem : SpecialItem.values()) {
                if (specialItem.name().equalsIgnoreCase(string)) {
                    return true;
                }
            }
            return false;
        }
    }

}
