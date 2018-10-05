package com.massivecraft.factions.cmd;

import com.mojang.brigadier.builder.ArgumentBuilder;

public interface BrigadierProvider {

    @SuppressWarnings("unchecked")
    ArgumentBuilder<Object, ?> get(ArgumentBuilder<Object, ?> parent);

}
