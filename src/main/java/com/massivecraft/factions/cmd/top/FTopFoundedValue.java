package com.massivecraft.factions.cmd.top;

import com.massivecraft.factions.util.TL;

public class FTopFoundedValue extends FTopGTNumberValue<FTopFoundedValue, Long> {
    public FTopFoundedValue(Long value) {
        super(value);
    }

    @Override
    public String getDisplayString() {
        return TL.sdf.format(value);
    }
}
