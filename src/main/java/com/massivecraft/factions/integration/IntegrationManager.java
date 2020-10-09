package com.massivecraft.factions.integration;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.integration.dynmap.EngineDynmap;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;

public class IntegrationManager implements Listener {
    private enum Integration {
        DYNMAP("dynmap", EngineDynmap.getInstance()::init),
        ESS("Essentials", Essentials::setup),
        LUCKPERMS("LuckPerms", (plugin) -> {
            String[] version = plugin.getDescription().getVersion().split("\\.");
            boolean notSupported = true;
            try {
                int major = Integer.parseInt(version[0]);
                int minor = Integer.parseInt(version[1]);
                if ((major == 5 && minor > 0) || major > 5) {
                    notSupported = false;
                }
            } catch (NumberFormatException ignored) {
            }
            if (notSupported) {
                FactionsPlugin.getInstance().log("Found an outdated LuckPerms. With LuckPerms 5.1.0 and above, FactionsUUID supports permission contexts!");
            } else {
                if (LuckPerms.init(FactionsPlugin.getInstance())) {
                    FactionsPlugin.getInstance().luckpermsEnabled();
                }
            }
        }),
        LWC("LWC", com.massivecraft.factions.integration.LWC::setup),
        PLACEHOLDERAPI("PlaceholderAPI", (p) -> FactionsPlugin.getInstance().setupPlaceholderAPI()),
        PLACEHOLDERAPI_OTHER("MVdWPlaceholderAPI", (p) -> FactionsPlugin.getInstance().setupOtherPlaceholderAPI()),
        SENTINEL("Sentinel", (p) -> Sentinel.init(p)),
        WORLDGUARD("WorldGuard", (plugin) -> {
            FactionsPlugin f = FactionsPlugin.getInstance();
            if (!f.conf().worldGuard().isChecking() && !f.conf().worldGuard().isBuildPriority()) {
                return;
            }

            String version = plugin.getDescription().getVersion();
            if (version.startsWith("6")) {
                f.setWorldGuard(new Worldguard6(plugin));
                f.getLogger().info("Found support for WorldGuard version " + version);
            } else if (version.startsWith("7")) {
                f.setWorldGuard(new Worldguard7());
                f.getLogger().info("Found support for WorldGuard version " + version);
            } else {
                f.log(Level.WARNING, "Found WorldGuard but couldn't support this version: " + version);
            }
        });

        private static final Map<String, Consumer<Plugin>> STARTUP_MAP = new HashMap<>();

        static {
            for (Integration integration : values()) {
                STARTUP_MAP.put(integration.pluginName, integration.startup);
            }
        }

        static Consumer<Plugin> getStartup(String pluginName) {
            return STARTUP_MAP.getOrDefault(pluginName, Integration::omNomNom);
        }

        private static void omNomNom(Plugin plugin) {
            // NOOP
        }

        private final String pluginName;
        private final Consumer<Plugin> startup;

        Integration(String pluginName, Consumer<Plugin> startup) {
            this.pluginName = pluginName;
            this.startup = startup;
        }
    }

    public IntegrationManager(FactionsPlugin plugin) {
        try {
            Field depGraph = SimplePluginManager.class.getDeclaredField("dependencyGraph");
            depGraph.setAccessible(true);
            Object graph = depGraph.get(plugin.getServer().getPluginManager());
            Method putEdge = graph.getClass().getDeclaredMethod("putEdge", Object.class, Object.class);
            putEdge.setAccessible(true);
            for (String depend : Integration.STARTUP_MAP.keySet()) {
                putEdge.invoke(graph, plugin.getDescription().getName(), depend);
            }
        } catch (Exception ignored) {
        }
        for (Integration integration : Integration.values()) {
            Plugin plug = plugin.getServer().getPluginManager().getPlugin(integration.pluginName);
            if (plug != null && plug.isEnabled()) {
                try {
                    integration.startup.accept(plug);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Failed to start " + integration.pluginName + " integration", e);
                }
            }
        }
    }

    @EventHandler
    public void onPluginEnabled(PluginEnableEvent event) {
        Integration.getStartup(event.getPlugin().getName()).accept(event.getPlugin());
    }
}
