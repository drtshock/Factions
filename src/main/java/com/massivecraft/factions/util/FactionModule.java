package com.massivecraft.factions.util;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.FCmdRoot;
import com.massivecraft.factions.config.FactionConfig;

public class FactionModule extends AbstractModule {

    private final P plugin;

    public FactionModule(P plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        FactionConfig config = new FactionConfig();
        config.load();

        bind(FactionConfig.class).toInstance(config);
    }

}
