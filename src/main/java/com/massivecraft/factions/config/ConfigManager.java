package com.massivecraft.factions.config;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.config.file.DefaultOfflinePermissionsConfig;
import com.massivecraft.factions.config.file.DefaultPermissionsConfig;
import com.massivecraft.factions.config.file.DynmapConfig;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.config.transition.Transitioner;

import java.io.IOException;

public class ConfigManager {
    private final FactionsPlugin plugin;
    private final DefaultPermissionsConfig permissionsConfig = new DefaultPermissionsConfig();
    private static final String permissionsConfigComment = "Default permissions";
    private final DefaultOfflinePermissionsConfig offlinePermissionsConfig = new DefaultOfflinePermissionsConfig();
    private static final String offlinePermissionsConfigComment = "Default permissions when offline, if enabled";
    private final MainConfig mainConfig = new MainConfig();
    private static final String mainConfigComment = "# FactionsUUID by drtshock\n" +
            "# Report issues https://github.com/drtshock/Factions/issues?state=open\n" +
            "# Live support http://factions-support.cf\n" +
            "# Website https://www.spigotmc.org/resources/factionsuuid.1035/\n" +
            "\n" +
            "# Made with love <3";
    private final DynmapConfig dynmapConfig = new DynmapConfig();
    private static final String dynmapConfigComment = "Default permissions for Dynmap";

    public ConfigManager(FactionsPlugin plugin) {
        this.plugin = plugin;
    }

    public void startup() {
        if (!this.plugin.getDataFolder().toPath().resolve("config").toFile().exists()) {
            Transitioner transitioner = new Transitioner(this.plugin);
            transitioner.checkTransition();
        }
        this.loadConfigs();
    }

    public void loadConfigs() {
        try {
            Loader.load("default_permissions", this.permissionsConfig, permissionsConfigComment);
            Loader.load("default_permissions_offline", this.offlinePermissionsConfig, offlinePermissionsConfigComment);
            Loader.load("main", this.mainConfig, mainConfigComment);
            Loader.load("dynmap", this.dynmapConfig, dynmapConfigComment);
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public DefaultPermissionsConfig getPermissionsConfig() {
        return this.permissionsConfig;
    }

    public DefaultOfflinePermissionsConfig getOfflinePermissionsConfig() {
        return this.offlinePermissionsConfig;
    }

    public MainConfig getMainConfig() {
        return this.mainConfig;
    }

    public DynmapConfig getDynmapConfig() {
        return dynmapConfig;
    }
}
