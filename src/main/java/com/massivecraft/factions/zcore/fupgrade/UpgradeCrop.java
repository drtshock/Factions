package com.massivecraft.factions.zcore.fupgrade;

import com.massivecraft.factions.zcore.util.TL;

public class UpgradeCrop extends FUpgrade {

    @Override
    public String name() {
        return "CROP";
    }

    @Override
    public String translation() {
        return TL.UPGRADE_CROP.toString();
    }

    UpgradeCrop() {
        super();
        maxLevel = configSection.getInt("max-level", 3);
    }

}
