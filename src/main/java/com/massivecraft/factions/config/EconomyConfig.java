package com.massivecraft.factions.config;

import com.massivecraft.factions.config.annotation.ConfigFile;
import com.massivecraft.factions.config.annotation.Node;

@Node(legacy = "econ")
@ConfigFile("economy.yml")
public class EconomyConfig {

    @Node public boolean enabled;
    @Node public String universeAccount = "";

    /* Claim Costs */
    @Node public double costClaimWilderness = 30;
    @Node public double costClaimFromFactionBonues = 30;
    @Node public double overclaimRewardMultiplier = 0;
    @Node public double claimAdditionalMultiplier = 0.5;
    @Node public double claimRefundMultiplier = 0.7;
    @Node public double claimUnconnectedFee = 0;
    
    /* Command Costs */
    @Node public Cost cost = new Cost();

    @Node(legacy = "econCost", path = "cost")
    public class Cost extends Config {
        @Node public double create = 100;
        @Node public double owner = 15;
        @Node public double sethome = 30;
        @Node public double join = 0;
        @Node public double leave = 0;
        @Node public double kick = 0;
        @Node public double invite = 0;
        @Node public double home = 0;
        @Node public double tag = 0;
        @Node public double desc = 0;
        @Node public double list = 0;
        @Node public double map = 0;
        @Node public double power = 0;
        @Node public double show = 0;
        @Node public double stuck = 0;
        @Node public double open = 0;
        @Node public double ally = 0;
        @Node public double truce = 0;
        @Node public double enemy = 0;
        @Node public double neutral = 0;
        @Node public double noBoom = 0;
    }

}
