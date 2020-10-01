package com.massivecraft.factions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.massivecraft.factions.cmd.FCmdRoot;
import com.massivecraft.factions.config.ConfigManager;
import com.massivecraft.factions.config.file.MainConfig;
import com.massivecraft.factions.data.SaveTask;
import com.massivecraft.factions.event.FactionCreateEvent;
import com.massivecraft.factions.event.FactionEvent;
import com.massivecraft.factions.event.FactionRelationEvent;
import com.massivecraft.factions.integration.ClipPlaceholderAPIManager;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.integration.Essentials;
import com.massivecraft.factions.integration.IWorldguard;
import com.massivecraft.factions.integration.IntegrationManager;
import com.massivecraft.factions.integration.LWC;
import com.massivecraft.factions.integration.LuckPerms;
import com.massivecraft.factions.integration.dynmap.EngineDynmap;
import com.massivecraft.factions.integration.permcontext.ContextManager;
import com.massivecraft.factions.landraidcontrol.LandRaidControl;
import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.listeners.FactionsChatListener;
import com.massivecraft.factions.listeners.FactionsEntityListener;
import com.massivecraft.factions.listeners.FactionsExploitListener;
import com.massivecraft.factions.listeners.FactionsPlayerListener;
import com.massivecraft.factions.listeners.OneEightPlusListener;
import com.massivecraft.factions.listeners.versionspecific.PortalHandler;
import com.massivecraft.factions.listeners.versionspecific.PortalListenerLegacy;
import com.massivecraft.factions.listeners.versionspecific.PortalListener_114;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.PermissionsMapTypeAdapter;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.util.AutoLeaveTask;
import com.massivecraft.factions.util.EnumTypeAdapter;
import com.massivecraft.factions.util.FlightUtil;
import com.massivecraft.factions.util.LazyLocation;
import com.massivecraft.factions.util.MapFLocToStringSetTypeAdapter;
import com.massivecraft.factions.util.Metrics;
import com.massivecraft.factions.util.MyLocationTypeAdapter;
import com.massivecraft.factions.util.PermUtil;
import com.massivecraft.factions.util.Persist;
import com.massivecraft.factions.util.SeeChunkUtil;
import com.massivecraft.factions.util.TL;
import com.massivecraft.factions.util.TextUtil;
import com.massivecraft.factions.util.TitleAPI;
import com.massivecraft.factions.util.WorldUtil;
import com.massivecraft.factions.util.material.MaterialDb;
import com.massivecraft.factions.util.particle.BukkitParticleProvider;
import com.massivecraft.factions.util.particle.PacketParticleProvider;
import com.massivecraft.factions.util.particle.ParticleProvider;
import io.papermc.lib.PaperLib;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FactionsPlugin extends JavaPlugin implements FactionsAPI {

    // Our single plugin instance.
    // Single 4 life.
    private static FactionsPlugin instance;
    private Permission perms = null;
    private static int mcVersion;

    public static FactionsPlugin getInstance() {
        return instance;
    }

    public static int getMCVersion() {
        return mcVersion;
    }

    private final ConfigManager configManager = new ConfigManager(this);

    private Integer saveTask = null;
    private boolean autoSave = true;
    private boolean loadSuccessful = false;

    // Some utils
    private Persist persist;
    private TextUtil txt;
    private WorldUtil worldUtil;

    public TextUtil txt() {
        return txt;
    }

    public WorldUtil worldUtil() {
        return worldUtil;
    }

    public void grumpException(RuntimeException e) {
        this.grumpyExceptions.add(e);
    }

    private PermUtil permUtil;

    // Persist related
    private Gson gson;

    // holds f stuck start times
    private final Map<UUID, Long> timers = new HashMap<>();

    //holds f stuck taskids
    private final Map<UUID, Integer> stuckMap = new HashMap<>();

    // Persistence related
    private boolean locked = false;

    private Integer autoLeaveTask = null;

    private ClipPlaceholderAPIManager clipPlaceholderAPIManager;
    private boolean mvdwPlaceholderAPIManager = false;
    private final Set<String> pluginsHandlingChat = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private SeeChunkUtil<?> seeChunkUtil;
    private ParticleProvider<?> particleProvider;
    private IWorldguard worldguard;
    private LandRaidControl landRaidControl;
    private boolean luckPermsSetup;
    private IntegrationManager integrationManager;

    private Metrics metrics;
    private final Pattern factionsVersionPattern = Pattern.compile("b(\\d{1,4})");
    private String updateMessage;
    private int buildNumber = -1;
    private UUID serverUUID;
    private String startupLog;
    private String startupExceptionLog;
    private final List<RuntimeException> grumpyExceptions = new ArrayList<>();

    public FactionsPlugin() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.loadSuccessful = false;
        StringBuilder startupBuilder = new StringBuilder();
        StringBuilder startupExceptionBuilder = new StringBuilder();
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (record.getMessage() != null && record.getMessage().contains("Loaded class {0}")) {
                    return;
                }
                startupBuilder.append('[').append(record.getLevel().getName()).append("] ").append(record.getMessage()).append('\n');
                if (record.getThrown() != null) {
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(stringWriter);
                    record.getThrown().printStackTrace(printWriter);
                    startupExceptionBuilder.append('[').append(record.getLevel().getName()).append("] ").append(record.getMessage()).append('\n')
                            .append(stringWriter.toString()).append('\n');
                }
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
        getLogger().addHandler(handler);
        getLogger().info("=== Starting up! ===");
        long timeEnableStart = System.currentTimeMillis();

        if (!this.grumpyExceptions.isEmpty()) {
            this.grumpyExceptions.forEach(e -> getLogger().log(Level.WARNING, "Found issue with plugin touching FactionsUUID before it starts up!", e));
        }

        // Ensure basefolder exists!
        this.getDataFolder().mkdirs();

        byte[] m = Bukkit.getMotd().getBytes(StandardCharsets.UTF_8);
        if (m.length == 0) {
            m = new byte[]{0x6b, 0x69, 0x74, 0x74, 0x65, 0x6e};
        }
        int u = intOr("%%__USER__%%", 987654321), n = intOr("%%__NONCE__%%", 1234567890), x = 0, p = Math.min(Bukkit.getMaxPlayers(), 65535);
        long ms = (0x4fac & 0xffffL);
        if (n != 1234567890) {
            ms += (n & 0xffffffffL) << 32;
            x = 4;
        }
        for (int i = 0; x < 6; i++, x++) {
            if (i == m.length) {
                i = 0;
            }
            ms += ((m[i] & 0xFFL) << (8 + (8 * (6 - x))));
        }
        this.serverUUID = new UUID(ms, ((0xaf & 0xffL) << 56) + ((0xac & 0xffL) << 48) + (u & 0xffffffffL) + ((p & 0xffffL) << 32));

        // Version party
        Pattern versionPattern = Pattern.compile("1\\.(\\d{1,2})(?:\\.(\\d{1,2}))?");
        Matcher versionMatcher = versionPattern.matcher(this.getServer().getVersion());
        getLogger().info("");
        getLogger().info("Factions UUID!");
        getLogger().info("Version " + this.getDescription().getVersion());
        getLogger().info("");
        getLogger().info("Need support? https://factions.support/help/");
        getLogger().info("");
        Integer versionInteger = null;
        if (versionMatcher.find()) {
            try {
                int minor = Integer.parseInt(versionMatcher.group(1));
                String patchS = versionMatcher.group(2);
                int patch = (patchS == null || patchS.isEmpty()) ? 0 : Integer.parseInt(patchS);
                versionInteger = (minor * 100) + patch;
                getLogger().info("Detected Minecraft " + versionMatcher.group());
            } catch (NumberFormatException ignored) {
            }
        }
        if (versionInteger == null) {
            getLogger().warning("");
            getLogger().warning("Could not identify version. Going with least supported version, 1.7.10.");
            getLogger().warning("Please visit our support live chat for help - https://factions.support/help/");
            getLogger().warning("");
            versionInteger = 710;
        }
        mcVersion = versionInteger;
        if (mcVersion < 808) {
            getLogger().info("");
            getLogger().warning("FactionsUUID works better with at least Minecraft 1.8.8");
        }
        getLogger().info("");
        this.buildNumber = this.getBuildNumber(this.getDescription().getVersion());

        this.getLogger().info("Server UUID " + this.serverUUID);

        loadLang();
        this.gson = this.getGsonBuilder().create();

        // Load Conf from disk
        this.configManager.startup();

        if (this.conf().data().json().useEfficientStorage()) {
            getLogger().info("Using space efficient (less readable) storage.");
        }

        this.landRaidControl = LandRaidControl.getByName(this.conf().factions().landRaidControl().getSystem());

        File dataFolder = new File(this.getDataFolder(), "data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        // Load Material database
        MaterialDb.load();

        // Create Utility Instances
        this.permUtil = new PermUtil(this);
        this.persist = new Persist(this);
        this.worldUtil = new WorldUtil(this);

        this.txt = new TextUtil();
        initTXT();

        // attempt to get first command defined in plugin.yml as reference command, if any commands are defined in there
        // reference command will be used to prevent "unknown command" console messages
        String refCommand = "";
        try {
            Map<String, Map<String, Object>> refCmd = this.getDescription().getCommands();
            if (refCmd != null && !refCmd.isEmpty()) {
                refCommand = (String) (refCmd.keySet().toArray()[0]);
            }
        } catch (ClassCastException ignored) {
        }

        // Register recurring tasks
        if (saveTask == null && this.conf().factions().other().getSaveToFileEveryXMinutes() > 0.0) {
            long saveTicks = (long) (20 * 60 * this.conf().factions().other().getSaveToFileEveryXMinutes()); // Approximately every 30 min by default
            saveTask = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new SaveTask(this), saveTicks, saveTicks);
        }

        int loadedPlayers = FPlayers.getInstance().load();
        int loadedFactions = Factions.getInstance().load();
        for (FPlayer fPlayer : FPlayers.getInstance().getAllFPlayers()) {
            Faction faction = Factions.getInstance().getFactionById(fPlayer.getFactionId());
            if (faction == null) {
                log("Invalid faction id on " + fPlayer.getName() + ":" + fPlayer.getFactionId());
                fPlayer.resetFactionData(false);
                continue;
            }
            faction.addFPlayer(fPlayer);
        }
        int loadedClaims = Board.getInstance().load();
        Board.getInstance().clean();
        FactionsPlugin.getInstance().getLogger().info("Loaded " + loadedPlayers + " players in " + loadedFactions + " factions with " + loadedClaims + " claims");

        // Add Base Commands
        FCmdRoot cmdBase = new FCmdRoot();

        ContextManager.init(this);
        if (getServer().getPluginManager().getPlugin("PermissionsEx") != null) {
            if (getServer().getPluginManager().getPlugin("PermissionsEx").getDescription().getVersion().startsWith("1")) {
                getLogger().info(" ");
                getLogger().warning("Notice: PermissionsEx version 1.x is dead. We suggest using LuckPerms (or PEX 2.0 when available). https://luckperms.net/");
                getLogger().info(" ");
            }
        }
        if (getServer().getPluginManager().getPlugin("GroupManager") != null) {
            getLogger().info(" ");
            getLogger().warning("Notice: GroupManager died in 2014. We suggest using LuckPerms instead. https://luckperms.net/");
            getLogger().info(" ");
        }
        Plugin lwc = getServer().getPluginManager().getPlugin("LWC");
        if (lwc != null && lwc.getDescription().getWebsite() != null && !lwc.getDescription().getWebsite().contains("extended")) {
            getLogger().info(" ");
            getLogger().warning("Notice: LWC Extended is the updated, and best supported, continuation of LWC. https://www.spigotmc.org/resources/lwc-extended.69551/");
            getLogger().info(" ");
        }

        // start up task which runs the autoLeaveAfterDaysOfInactivity routine
        startAutoLeaveTask(false);

        // Run before initializing listeners to handle reloads properly.
        if (mcVersion < 1300) { // Before 1.13
            particleProvider = this.particleProvider(new PacketParticleProvider());
        } else {
            particleProvider = this.particleProvider(new BukkitParticleProvider());
        }
        getLogger().info(txt.parse("Using %1s as a particle provider", particleProvider.name()));

        // End run before registering event handlers.

        // Register Event Handlers
        getServer().getPluginManager().registerEvents(new FactionsPlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new FactionsChatListener(this), this);
        getServer().getPluginManager().registerEvents(new FactionsEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new FactionsExploitListener(this), this);
        getServer().getPluginManager().registerEvents(new FactionsBlockListener(this), this);
        if (mcVersion >= 800) {
            getServer().getPluginManager().registerEvents(new OneEightPlusListener(this), this);
        }

        // Version specific portal listener check.
        if (mcVersion >= 1400) { // Starting with 1.14
            getServer().getPluginManager().registerEvents(new PortalListener_114(this), this);
        } else {
            getServer().getPluginManager().registerEvents(new PortalListenerLegacy(new PortalHandler()), this);
        }

        // since some other plugins execute commands directly through this command interface, provide it
        this.getCommand(refCommand).setExecutor(cmdBase);

        new TitleAPI();

        if (ChatColor.stripColor(TL.NOFACTION_PREFIX.toString()).equals("[4-]")) {
            getLogger().warning("Looks like you have an old, mistaken 'nofactions-prefix' in your lang.yml. It currently displays [4-] which is... strange.");
        }

        // Integration time
        getServer().getPluginManager().registerEvents(integrationManager = new IntegrationManager(this), this);

        new BukkitRunnable() {
            @Override
            public void run() {
                Econ.setup();
                setupPermissions();
                if (perms != null) {
                    getLogger().info("Using Vault with permissions plugin " + perms.getName());
                }
                cmdBase.done();
                // Grand metrics adventure!
                setupMetrics();
                getLogger().removeHandler(handler);
                startupLog = startupBuilder.toString();
                startupExceptionLog = startupExceptionBuilder.toString();
            }
        }.runTask(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (checkForUpdates()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Bukkit.broadcast(updateMessage, com.massivecraft.factions.struct.Permission.UPDATES.toString());
                        }
                    }.runTask(FactionsPlugin.this);
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(this, 0, 20 * 60 * 10); // ten minutes

        getLogger().info("=== Ready to go after " + (System.currentTimeMillis() - timeEnableStart) + "ms! ===");
        this.loadSuccessful = true;
    }

    private <E> ParticleProvider<E> particleProvider(ParticleProvider<E> particleProvider) {
        if (conf().commands().seeChunk().isParticles()) {
            double delay = Math.floor(conf().commands().seeChunk().getParticleUpdateTime() * 20);
            seeChunkUtil = new SeeChunkUtil<>(particleProvider);
            seeChunkUtil.runTaskTimer(this, 0, (long) delay);
        }
        if (conf().commands().fly().isEnable()) {
            FlightUtil.start(particleProvider);
        }
        return particleProvider;
    }

    private int intOr(String in, int or) {
        try {
            return Integer.parseInt(in);
        } catch (NumberFormatException ignored) {
            return or;
        }
    }

    private void setupMetrics() {
        this.metrics = new Metrics(this);

        // Version
        String verString = this.getDescription().getVersion().replace("${build.number}", "selfbuilt");
        Pattern verPattern = Pattern.compile("U([\\d.]+)-b(.*)");
        Matcher matcher = verPattern.matcher(verString);
        final String fuuidVersion;
        final String fuuidBuild;
        if (matcher.find()) {
            fuuidVersion = matcher.group(1);
            fuuidBuild = matcher.group(2);
        } else {
            fuuidVersion = "Unknown";
            fuuidBuild = verString;
        }
        this.metricsDrillPie("fuuid_version", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(fuuidBuild, 1);
            map.put(fuuidVersion, entry);
            return map;
        });

        // Essentials
        Plugin ess = Essentials.getEssentials();
        this.metricsDrillPie("essentials", () -> this.metricsPluginInfo(ess));
        if (ess != null) {
            this.metricsSimplePie("essentials_delete_homes", () -> "" + conf().factions().other().isDeleteEssentialsHomes());
            this.metricsSimplePie("essentials_home_teleport", () -> "" + this.conf().factions().homes().isTeleportCommandEssentialsIntegration());
        }

        // LWC
        Plugin lwc = LWC.getLWC();
        this.metricsDrillPie("lwc", () -> this.metricsPluginInfo(lwc));
        if (lwc != null) {
            boolean enabled = conf().lwc().isEnabled();
            this.metricsSimplePie("lwc_integration", () -> "" + enabled);
            if (enabled) {
                this.metricsSimplePie("lwc_reset_locks_unclaim", () -> "" + conf().lwc().isResetLocksOnUnclaim());
                this.metricsSimplePie("lwc_reset_locks_capture", () -> "" + conf().lwc().isResetLocksOnCapture());
            }
        }

        // Vault
        Plugin vault = Bukkit.getServer().getPluginManager().getPlugin("Vault");
        this.metricsDrillPie("vault", () -> this.metricsPluginInfo(vault));
        if (vault != null) {
            this.metricsDrillPie("vault_perms", () -> this.metricsInfo(perms, perms::getName));
            this.metricsDrillPie("vault_econ", () -> {
                Map<String, Map<String, Integer>> map = new HashMap<>();
                Map<String, Integer> entry = new HashMap<>();
                entry.put(Econ.getEcon() == null ? "none" : Econ.getEcon().getName(), 1);
                map.put((this.conf().economy().isEnabled() && Econ.getEcon() != null) ? "enabled" : "disabled", entry);
                return map;
            });
        }

        // WorldGuard
        IWorldguard wg = this.getWorldguard();
        String wgVersion = wg == null ? "nope" : wg.getVersion();
        this.metricsDrillPie("worldguard", () -> this.metricsInfo(wg, () -> wgVersion));

        // Dynmap
        String dynmapVersion = EngineDynmap.getInstance().getVersion();
        boolean dynmapEnabled = EngineDynmap.getInstance().isRunning();
        this.metricsDrillPie("dynmap", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            Map<String, Integer> entry = new HashMap<>();
            entry.put(dynmapVersion == null ? "none" : dynmapVersion, 1);
            map.put(dynmapEnabled ? "enabled" : "disabled", entry);
            return map;
        });

        // Clip Placeholder
        Plugin clipPlugin = getServer().getPluginManager().getPlugin("PlaceholderAPI");
        this.metricsDrillPie("clipplaceholder", () -> this.metricsPluginInfo(clipPlugin));

        // MVdW Placeholder
        Plugin mvdw = getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI");
        this.metricsDrillPie("mvdwplaceholder", () -> this.metricsPluginInfo(mvdw));

        // Overall stats
        this.metricsLine("factions", () -> Factions.getInstance().getAllFactions().size() - 3);
        this.metricsSimplePie("scoreboard", () -> "" + conf().scoreboard().constant().isEnabled());

        // Event listeners
        this.metricsDrillPie("event_listeners", () -> {
            Set<Plugin> pluginsListening = this.getPlugins(FactionEvent.getHandlerList(), FactionCreateEvent.getHandlerList(), FactionRelationEvent.getHandlerList());
            Map<String, Map<String, Integer>> map = new HashMap<>();
            for (Plugin plugin : pluginsListening) {
                if (plugin.getName().equalsIgnoreCase("factions")) {
                    continue;
                }
                Map<String, Integer> entry = new HashMap<>();
                entry.put(plugin.getDescription().getVersion(), 1);
                map.put(plugin.getName(), entry);
            }
            return map;
        });
    }

    private Set<Plugin> getPlugins(HandlerList... handlerLists) {
        Set<Plugin> plugins = new HashSet<>();
        for (HandlerList handlerList : handlerLists) {
            plugins.addAll(this.getPlugins(handlerList));
        }
        return plugins;
    }

    private Set<Plugin> getPlugins(HandlerList handlerList) {
        return Arrays.stream(handlerList.getRegisteredListeners()).map(RegisteredListener::getPlugin).collect(Collectors.toSet());
    }

    private void metricsLine(String name, Callable<Integer> callable) {
        this.metrics.addCustomChart(new Metrics.SingleLineChart(name, callable));
    }

    private void metricsDrillPie(String name, Callable<Map<String, Map<String, Integer>>> callable) {
        this.metrics.addCustomChart(new Metrics.DrilldownPie(name, callable));
    }

    private void metricsSimplePie(String name, Callable<String> callable) {
        this.metrics.addCustomChart(new Metrics.SimplePie(name, callable));
    }

    private Map<String, Map<String, Integer>> metricsPluginInfo(Plugin plugin) {
        return this.metricsInfo(plugin, () -> plugin.getDescription().getVersion());
    }

    private Map<String, Map<String, Integer>> metricsInfo(Object plugin, Supplier<String> versionGetter) {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        Map<String, Integer> entry = new HashMap<>();
        entry.put(plugin == null ? "nope" : versionGetter.get(), 1);
        map.put(plugin == null ? "absent" : "present", entry);
        return map;
    }

    public void setWorldGuard(IWorldguard wg) {
        this.worldguard = wg;
    }

    public void loadLang() {
        File lang = new File(getDataFolder(), "lang.yml");
        OutputStream out = null;
        InputStream defLangStream = this.getResource("lang.yml");
        if (!lang.exists()) {
            try {
                getDataFolder().mkdir();
                lang.createNewFile();
                if (defLangStream != null) {
                    out = new FileOutputStream(lang);
                    int read;
                    byte[] bytes = new byte[1024];

                    while ((read = defLangStream.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new BufferedReader(new InputStreamReader(defLangStream)));
                    TL.setFile(defConfig);
                }
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "[Factions] Couldn't create language file.", e);
                getLogger().severe("[Factions] This is a fatal error. Now disabling");
                this.setEnabled(false); // Without it loaded, we can't send them messages
            } finally {
                if (defLangStream != null) {
                    try {
                        defLangStream.close();
                    } catch (IOException e) {
                        FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to close resource", e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to close output", e);
                    }
                }
            }
        }

        YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
        for (TL item : TL.values()) {
            if (conf.getString(item.getPath()) == null) {
                conf.set(item.getPath(), item.getDefault());
            }
        }

        // Remove this here because I'm sick of dealing with bug reports due to bad decisions on my part.
        if (conf.getString(TL.COMMAND_SHOW_POWER.getPath(), "").contains("%5$s")) {
            conf.set(TL.COMMAND_SHOW_POWER.getPath(), TL.COMMAND_SHOW_POWER.getDefault());
            log(Level.INFO, "Removed errant format specifier from f show power.");
        }

        TL.setFile(conf);
        try {
            conf.save(lang);
        } catch (IOException e) {
            getLogger().log(Level.WARNING, "Factions: Report this stack trace to drtshock.");
            FactionsPlugin.getInstance().getLogger().log(Level.SEVERE, "Failed to save lang.yml", e);
        }
    }

    private int getBuildNumber(String version) {
        Matcher factionsVersionMatcher = factionsVersionPattern.matcher(version);
        if (factionsVersionMatcher.find()) {
            try {
                return Integer.parseInt(factionsVersionMatcher.group(1));
            } catch (NumberFormatException ignored) { // HOW
            }
        }
        return -1;
    }

    private boolean checkForUpdates() {
        try {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=1035");
            HttpURLConnection con = ((HttpURLConnection) url.openConnection());
            int status = con.getResponseCode();
            if (status == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.startsWith("1.6.9.5-U") && !this.getDescription().getVersion().equals(line)) {
                            if (this.buildNumber > 0) {
                                int build = this.getBuildNumber(line);
                                if (build > 0 && build <= this.buildNumber) {
                                    return false;
                                }
                            }
                            this.updateMessage = ChatColor.GREEN + "New version of " + ChatColor.DARK_AQUA + "Factions" + ChatColor.GREEN + " available: " + ChatColor.DARK_AQUA + line;
                            return true;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public UUID getServerUUID() {
        return this.serverUUID;
    }

    public String getStartupLog() {
        return this.startupLog;
    }

    public String getStartupExceptionLog() {
        return this.startupExceptionLog;
    }

    public void updatesOnJoin(Player player) {
        if (this.updateMessage != null && player.hasPermission(com.massivecraft.factions.struct.Permission.UPDATES.toString())) {
            player.sendMessage(this.updateMessage);
            player.sendMessage(ChatColor.GREEN + "Get it at " + ChatColor.DARK_AQUA + "https://www.spigotmc.org/resources/factionsuuid.1035/");
        }
    }

    public PermUtil getPermUtil() {
        return permUtil;
    }

    public Gson getGson() {
        return gson;
    }

    public SeeChunkUtil<?> getSeeChunkUtil() {
        return seeChunkUtil;
    }

    public ParticleProvider<?> getParticleProvider() {
        return particleProvider;
    }

    // -------------------------------------------- //
    // LANG AND TAGS
    // -------------------------------------------- //

    // These are not supposed to be used directly.
    // They are loaded and used through the TextUtil instance for the plugin.
    private final Map<String, String> rawTags = new LinkedHashMap<>();

    private void addRawTags() {
        this.rawTags.put("l", "<green>"); // logo
        this.rawTags.put("a", "<gold>"); // art
        this.rawTags.put("n", "<silver>"); // notice
        this.rawTags.put("i", "<yellow>"); // info
        this.rawTags.put("g", "<lime>"); // good
        this.rawTags.put("b", "<rose>"); // bad
        this.rawTags.put("h", "<pink>"); // highligh
        this.rawTags.put("c", "<aqua>"); // command
        this.rawTags.put("p", "<teal>"); // parameter
    }

    private void initTXT() {
        this.addRawTags();

        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        Map<String, String> tagsFromFile = this.persist.load(type, "tags");
        if (tagsFromFile != null) {
            this.rawTags.putAll(tagsFromFile);
        }
        this.persist.save(this.rawTags, "tags");

        for (Map.Entry<String, String> rawTag : this.rawTags.entrySet()) {
            this.txt.tags.put(rawTag.getKey(), TextUtil.parseColor(rawTag.getValue()));
        }
    }

    public Map<UUID, Integer> getStuckMap() {
        return this.stuckMap;
    }

    public Map<UUID, Long> getTimers() {
        return this.timers;
    }

    // -------------------------------------------- //
    // LOGGING
    // -------------------------------------------- //
    public void log(String msg) {
        log(Level.INFO, msg);
    }

    public void log(String str, Object... args) {
        log(Level.INFO, this.txt.parse(str, args));
    }

    public void log(Level level, String str, Object... args) {
        log(level, this.txt.parse(str, args));
    }

    public void log(Level level, String msg) {
        this.getLogger().log(level, msg);
    }

    public boolean getLocked() {
        return this.locked;
    }

    public void setLocked(boolean val) {
        this.locked = val;
        this.setAutoSave(val);
    }

    public boolean getAutoSave() {
        return this.autoSave;
    }

    public void setAutoSave(boolean val) {
        this.autoSave = val;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    public MainConfig conf() {
        return this.configManager.getMainConfig();
    }

    public LandRaidControl getLandRaidControl() {
        return this.landRaidControl;
    }

    public IWorldguard getWorldguard() {
        return this.worldguard;
    }

    public void setupPlaceholderAPI() {
        this.clipPlaceholderAPIManager = new ClipPlaceholderAPIManager();
        if (this.clipPlaceholderAPIManager.register()) {
            getLogger().info("Successfully registered placeholders with PlaceholderAPI.");
        }
    }

    public void setupOtherPlaceholderAPI() {
        this.mvdwPlaceholderAPIManager = true;
        getLogger().info("Found MVdWPlaceholderAPI.");
    }

    public boolean isClipPlaceholderAPIHooked() {
        return this.clipPlaceholderAPIManager != null;
    }

    public boolean isMVdWPlaceholderAPIHooked() {
        return this.mvdwPlaceholderAPIManager;
    }

    private boolean setupPermissions() {
        try {
            RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
            if (rsp != null) {
                perms = rsp.getProvider();
            }
        } catch (NoClassDefFoundError ex) {
            return false;
        }
        return perms != null;
    }

    private GsonBuilder getGsonBuilder() {
        Type mapFLocToStringSetType = new TypeToken<Map<FLocation, Set<String>>>() {
        }.getType();

        Type accessType = new TypeToken<Map<Permissible, Map<PermissibleAction, Boolean>>>() {
        }.getType();

        GsonBuilder builder = new GsonBuilder();

        if (!this.conf().data().json().useEfficientStorage()) {
            builder.setPrettyPrinting();
        }

        return builder
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
                .registerTypeAdapter(accessType, new PermissionsMapTypeAdapter())
                .registerTypeAdapter(LazyLocation.class, new MyLocationTypeAdapter())
                .registerTypeAdapter(mapFLocToStringSetType, new MapFLocToStringSetTypeAdapter())
                .registerTypeAdapterFactory(EnumTypeAdapter.ENUM_FACTORY);
    }

    @Override
    public void onDisable() {
        if (autoLeaveTask != null) {
            this.getServer().getScheduler().cancelTask(autoLeaveTask);
            autoLeaveTask = null;
        }

        if (saveTask != null) {
            this.getServer().getScheduler().cancelTask(saveTask);
            saveTask = null;
        }
        // only save data if plugin actually loaded successfully
        if (loadSuccessful) {
            Factions.getInstance().forceSave();
            FPlayers.getInstance().forceSave();
            Board.getInstance().forceSave();
        }
        if (this.luckPermsSetup) {
            LuckPerms.shutdown(this);
        }
        ContextManager.shutdown();
        log("Disabled");
    }

    public void startAutoLeaveTask(boolean restartIfRunning) {
        if (autoLeaveTask != null) {
            if (!restartIfRunning) {
                return;
            }
            this.getServer().getScheduler().cancelTask(autoLeaveTask);
        }

        if (this.conf().factions().other().getAutoLeaveRoutineRunsEveryXMinutes() > 0.0) {
            long ticks = (long) (20 * 60 * this.conf().factions().other().getAutoLeaveRoutineRunsEveryXMinutes());
            autoLeaveTask = getServer().getScheduler().scheduleSyncRepeatingTask(this, new AutoLeaveTask(), ticks, ticks);
        }
    }

    public boolean logPlayerCommands() {
        return this.conf().logging().isPlayerCommands();
    }

    // -------------------------------------------- //
    // Functions for other plugins to hook into
    // -------------------------------------------- //

    // This value will be updated whenever new hooks are added
    @Override
    public int getAPIVersion() {
        // Updated from 4 to 5 for version 0.5.0
        return 4;
    }

    // If another plugin is handling insertion of chat tags, this should be used to notify Factions
    @Override
    public void setHandlingChat(Plugin plugin, boolean handling) {
        if (plugin == null) {
            throw new IllegalArgumentException("Null plugin!");
        }
        if (plugin == this) {
            throw new IllegalArgumentException("Nice try, but this plugin isn't going to register itself!");
        }
        if (handling) {
            this.pluginsHandlingChat.add(plugin.getName());
        } else {
            this.pluginsHandlingChat.remove(plugin.getName());
        }
    }

    @Override
    public boolean isAnotherPluginHandlingChat() {
        return this.conf().factions().chat().isTagHandledByAnotherPlugin() || !this.pluginsHandlingChat.isEmpty();
    }

    // Simply put, should this chat event be left for Factions to handle? For now, that means players with Faction Chat
    // enabled or use of the Factions f command without a slash; combination of isPlayerFactionChatting() and isFactionsCommand()

    @Override
    public boolean shouldLetFactionsHandleThisChat(AsyncPlayerChatEvent event) {
        return event != null && (isPlayerFactionChatting(event.getPlayer()) || isFactionsCommand(event.getMessage()));
    }

    // Does player have Faction Chat enabled? If so, chat plugins should preferably not do channels,
    // local chat, or anything else which targets individual recipients, so Faction Chat can be done
    @Override
    public boolean isPlayerFactionChatting(Player player) {
        if (player == null) {
            return false;
        }
        FPlayer me = FPlayers.getInstance().getByPlayer(player);

        return me != null && me.getChatMode().isAtLeast(ChatMode.ALLIANCE);
    }

    // Is this chat message actually a Factions command, and thus should be left alone by other plugins?

    // TODO: GET THIS BACK AND WORKING

    public boolean isFactionsCommand(String check) {
        return !(check == null || check.isEmpty()); //&& this.handleCommand(null, check, true);
    }

    // Get a player's faction tag (faction name), mainly for usage by chat plugins for local/channel chat
    @Override
    public String getPlayerFactionTag(Player player) {
        return getPlayerFactionTagRelation(player, null);
    }

    // Same as above, but with relation (enemy/neutral/ally) coloring potentially added to the tag
    @Override
    public String getPlayerFactionTagRelation(Player speaker, Player listener) {
        String tag = "~";

        if (speaker == null) {
            return tag;
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(speaker);
        if (me == null) {
            return tag;
        }

        // if listener isn't set, or config option is disabled, give back uncolored tag
        if (listener == null || !this.conf().factions().chat().isTagRelationColored()) {
            tag = me.getChatTag().trim();
        } else {
            FPlayer you = FPlayers.getInstance().getByPlayer(listener);
            if (you == null) {
                tag = me.getChatTag().trim();
            } else  // everything checks out, give the colored tag
            {
                tag = me.getChatTag(you).trim();
            }
        }
        if (tag.isEmpty()) {
            tag = "~";
        }

        return tag;
    }

    // Get a player's title within their faction, mainly for usage by chat plugins for local/channel chat
    @Override
    public String getPlayerTitle(Player player) {
        if (player == null) {
            return "";
        }

        FPlayer me = FPlayers.getInstance().getByPlayer(player);
        if (me == null) {
            return "";
        }

        return me.getTitle().trim();
    }

    // Get a list of all faction tags (names)
    @Override
    public Set<String> getFactionTags() {
        return Factions.getInstance().getFactionTags();
    }

    // Get a list of all players in the specified faction
    @Override
    public Set<String> getPlayersInFaction(String factionTag) {
        Set<String> players = new HashSet<>();
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if (faction != null) {
            for (FPlayer fplayer : faction.getFPlayers()) {
                players.add(fplayer.getName());
            }
        }
        return players;
    }

    // Get a list of all online players in the specified faction
    @Override
    public Set<String> getOnlinePlayersInFaction(String factionTag) {
        Set<String> players = new HashSet<>();
        Faction faction = Factions.getInstance().getByTag(factionTag);
        if (faction != null) {
            for (FPlayer fplayer : faction.getFPlayersWhereOnline(true)) {
                players.add(fplayer.getName());
            }
        }
        return players;
    }

    public String getPrimaryGroup(OfflinePlayer player) {
        return perms == null || !perms.hasGroupSupport() ? " " : perms.getPrimaryGroup(Bukkit.getWorlds().get(0).toString(), player);
    }

    public void debug(Level level, String s) {
        if (conf().getaVeryFriendlyFactionsConfig().isDebug()) {
            getLogger().log(level, s);
        }
    }

    public void debug(String s) {
        debug(Level.INFO, s);
    }

    public void luckpermsEnabled() {
        this.luckPermsSetup = true;
    }

    public CompletableFuture<Boolean> teleport(Player player, Location location) {
        if (this.conf().paper().isAsyncTeleport()) {
            return PaperLib.teleportAsync(player, location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        } else {
            return CompletableFuture.completedFuture(player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN));
        }
    }
}
