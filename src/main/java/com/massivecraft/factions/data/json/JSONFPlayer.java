package com.massivecraft.factions.data.json;

import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.data.MemoryFPlayer;
import com.massivecraft.factions.landraidcontrol.PowerControl;

import java.util.UUID;

public class JSONFPlayer extends MemoryFPlayer {

    public JSONFPlayer(MemoryFPlayer arg0) {
        super(arg0);
    }

    @Deprecated
    public JSONFPlayer(String id) {
        super(id);
    }

    public JSONFPlayer(UUID id) {
        super(id);
    }

    @Override
    public void remove() {
        FPlayers.getInstance().remove(this);
    }

    public boolean shouldBeSaved() {
        return this.hasFaction() ||
                (FactionsPlugin.getInstance().getLandRaidControl() instanceof PowerControl &&
                        (this.getPowerRounded() != this.getPowerMaxRounded() &&
                                this.getPowerRounded() != (int) Math.round(FactionsPlugin.getInstance().conf().factions().landRaidControl().power().getPlayerStarting())));
    }
}
