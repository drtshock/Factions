package com.massivecraft.factions.iface;

import java.util.HashMap;

import com.massivecraft.factions.zcore.util.TL;

public interface EconomyParticipator extends RelationParticipator {

    public String getAccountId();

    public void msg(String str, Object... args);
    
    public void TLmsg(TL str, HashMap<String,String> values);
}