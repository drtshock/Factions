package com.massivecraft.factions.cmd;

import com.google.common.base.Charsets;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.kitteh.pastegg.PasteBuilder;
import org.kitteh.pastegg.PasteContent;
import org.kitteh.pastegg.PasteFile;
import org.kitteh.pastegg.Visibility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CmdDebug extends FCommand {
    public CmdDebug() {
        super();
        this.aliases.add("debug");

        this.requirements = new CommandRequirements.Builder(Permission.DEBUG).build();
    }

    @Override
    public void perform(CommandContext context) {
        StringBuilder mainInfo = new StringBuilder();
        mainInfo.append(Bukkit.getName()).append(" version: ").append(Bukkit.getServer().getVersion()).append('\n');
        mainInfo.append("Plugin version: ").append(FactionsPlugin.getInstance().getDescription().getVersion()).append('\n');
        mainInfo.append("Java version: ").append(System.getProperty("java.version")).append('\n');
        mainInfo.append("Server ID: ").append(FactionsPlugin.getInstance().getServerUUID()).append('\n');
        mainInfo.append('\n');
        mainInfo.append("Plugins:\n");
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            mainInfo.append(' ').append(plugin.getName()).append(" - ").append(plugin.getDescription().getVersion()).append('\n');
            mainInfo.append("  ").append(plugin.getDescription().getAuthors()).append('\n');
        }

        StringBuilder permInfo = new StringBuilder();
        for (Player player : Bukkit.getOnlinePlayers()) {
            permInfo.append(player.getName()).append('\n');
            for (Permission permission : Permission.values()) {
                permInfo.append(' ');
                if (player.hasPermission(permission.toString())) {
                    permInfo.append('\u2713');
                } else {
                    permInfo.append('\u2715');
                }
                permInfo.append(permission.toString()).append('\n');
            }
            permInfo.append('\n');
        }

        new BukkitRunnable() {
            private PasteBuilder builder = new PasteBuilder().name("FactionsUUID Debug")
                    .visibility(Visibility.UNLISTED)
                    .expires(ZonedDateTime.now(ZoneOffset.UTC).plusDays(3));

            private void add(String name, String content) {
                builder.addFile(new PasteFile(name, new PasteContent(PasteContent.ContentType.TEXT, content)));
            }

            private String getFile(Path file) {
                try {
                    return new String(Files.readAllBytes(file), Charsets.UTF_8);
                } catch (IOException e) {
                    return ExceptionUtils.getFullStackTrace(e);
                }
            }

            @Override
            public void run() {
                try {
                    Path dataPath = FactionsPlugin.getInstance().getDataFolder().toPath();
                    add("0info.txt", mainInfo.toString());
                    add("1server.properties", getFile(Paths.get("server.properties")).replaceAll("(?:(?:server-ip=)|(?:server-port=)|(?:rcon\\.port=)|(?:rcon\\.password=)|(?:query.port=))[^\n]*[\r\n]*", ""));
                    add("2main.conf", getFile(dataPath.resolve("config/main.conf")));
                    add("3config.yml", getFile(dataPath.resolve("config.yml")));
                    add("4spigot.yml", getFile(Paths.get("spigot.yml")));
                    if (permInfo.length() > 0) {
                        add("5perms.txt", permInfo.toString());
                    }
                    PasteBuilder.PasteResult result = builder.build();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (result.getPaste().isPresent()) {
                                String delKey = result.getPaste().get().getDeletionKey().orElse("No deletion key");
                                context.msg(TL.COMMAND_DEBUG_COMPLETE, "https://paste.gg/anonymous/" + result.getPaste().get().getId());
                                context.msg(TL.COMMAND_DEBUG_DELETIONKEY, delKey);
                                if (context.sender instanceof Player) {
                                    FactionsPlugin.getInstance().getLogger().info(TL.COMMAND_DEBUG_COMPLETE.format("https://paste.gg/anonymous/" + result.getPaste().get().getId()));
                                    FactionsPlugin.getInstance().getLogger().info(TL.COMMAND_DEBUG_DELETIONKEY.format(delKey));
                                }
                            } else {
                                context.msg(TL.COMMAND_DEBUG_FAIL);
                                FactionsPlugin.getInstance().getLogger().warning("Received: " + result.getMessage());
                            }
                        }
                    }.runTask(FactionsPlugin.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            context.msg(TL.COMMAND_DEBUG_FAIL);
                        }
                    }.runTask(FactionsPlugin.getInstance());
                }
            }
        }.runTaskAsynchronously(FactionsPlugin.getInstance());
        context.msg(TL.COMMAND_DEBUG_RUNNING);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_DEBUG_DESCRIPTION;
    }
}
