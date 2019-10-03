package com.massivecraft.factions.cmd.top;

import com.massivecraft.factions.integration.Econ;

public class FTopBalanceValue extends FTopGTNumberValue<FTopBalanceValue, Double> {
    public FTopBalanceValue(Double value) {
        super(value);
    }

    @Override
    public String getDisplayString() {
        return Econ.moneyString(value);
    }
}
