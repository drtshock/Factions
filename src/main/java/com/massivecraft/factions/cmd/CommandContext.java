package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/*
    Object that contains information about a command being executed,
    args, player, faction
 */
public class CommandContext {

    public CommandSender sender;

    public Player player;
    public FPlayer fPlayer;
    public Faction faction;

    public List<String> args;
    public String alias;

    public CommandContext(CommandSender sender, List<String> args, String alias) {
        this.sender = sender;
        this.args = args;
        this.alias = alias;

        if (sender instanceof Player) {
            this.player = (Player) sender;
            this.fPlayer = FPlayers.getInstance().getByPlayer(player);
            this.faction = fPlayer.getFaction();
        }
    }

    // TODO: Clean this UP
    // -------------------------------------------- //
    // Argument Readers
    // -------------------------------------------- //

    // Is set? ======================
    public boolean argIsSet(int idx) {
        return this.args.size() >= idx + 1;
    }

    // STRING ======================
    public String argAsString(int idx, String def) {
        if (this.args.size() < idx + 1) {
            return def;
        }
        return this.args.get(idx);
    }

    public String argAsString(int idx) {
        return this.argAsString(idx, null);
    }

    // INT ======================
    public Integer strAsInt(String str, Integer def) {
        if (str == null) {
            return def;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return def;
        }
    }

    public Integer argAsInt(int idx, Integer def) {
        return strAsInt(this.argAsString(idx), def);
    }

    public Integer argAsInt(int idx) {
        return this.argAsInt(idx, null);
    }

    // Double ======================
    public Double strAsDouble(String str, Double def) {
        if (str == null) {
            return def;
        }
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {
            return def;
        }
    }

    public Double argAsDouble(int idx, Double def) {
        return strAsDouble(this.argAsString(idx), def);
    }

    public Double argAsDouble(int idx) {
        return this.argAsDouble(idx, null);
    }

    // TODO: Go through the str conversion for the other arg-readers as well.
    // Boolean ======================
    public Boolean strAsBool(String str) {
        str = str.toLowerCase();
        return str.startsWith("y") || str.startsWith("t") || str.startsWith("on") || str.startsWith("+") || str.startsWith("1");
    }

    public Boolean argAsBool(int idx, boolean def) {
        String str = this.argAsString(idx);
        if (str == null) {
            return def;
        }

        return strAsBool(str);
    }

    public Boolean argAsBool(int idx) {
        return this.argAsBool(idx, false);
    }

    // PLAYER ======================
    public Player strAsPlayer(String name, Player def, boolean msg) {
        Player ret = def;

        if (name != null) {
            Player player = Bukkit.getServer().getPlayer(name);
            if (player != null) {
                ret = player;
            }
        }

        if (msg && ret == null) {
            sender.sendMessage(TL.GENERIC_NOPLAYERFOUND.format(name));
        }

        return ret;
    }

    public Player argAsPlayer(int idx, Player def, boolean msg) {
        return this.strAsPlayer(this.argAsString(idx), def, msg);
    }

    public Player argAsPlayer(int idx, Player def) {
        return this.argAsPlayer(idx, def, true);
    }

    public Player argAsPlayer(int idx) {
        return this.argAsPlayer(idx, null);
    }

    // BEST PLAYER MATCH ======================
    public Player strAsBestPlayerMatch(String name, Player def, boolean msg) {
        Player ret = def;

        if (name != null) {
            List<Player> players = Bukkit.getServer().matchPlayer(name);
            if (players.size() > 0) {
                ret = players.get(0);
            }
        }

        if (msg && ret == null) {
            sender.sendMessage(TL.GENERIC_NOPLAYERMATCH.format(name));
        }

        return ret;
    }

    public Player argAsBestPlayerMatch(int idx, Player def, boolean msg) {
        return this.strAsBestPlayerMatch(this.argAsString(idx), def, msg);
    }

    public Player argAsBestPlayerMatch(int idx, Player def) {
        return this.argAsBestPlayerMatch(idx, def, true);
    }

    public Player argAsBestPlayerMatch(int idx) {
        return this.argAsPlayer(idx, null);
    }


    // -------------------------------------------- //
    // Faction Argument Readers
    // -------------------------------------------- //

    // FPLAYER ======================
    public FPlayer strAsFPlayer(String name, FPlayer def, boolean msg) {
        FPlayer ret = def;

        if (name != null) {
            for (FPlayer fplayer : FPlayers.getInstance().getAllFPlayers()) {
                if (fplayer.getName().equalsIgnoreCase(name)) {
                    ret = fplayer;
                    break;
                }
            }
        }

        if (msg && ret == null) {
            sender.sendMessage(TL.GENERIC_NOPLAYERFOUND.format(name));
        }

        return ret;
    }

    public FPlayer argAsFPlayer(int idx, FPlayer def, boolean msg) {
        return this.strAsFPlayer(this.argAsString(idx), def, msg);
    }

    public FPlayer argAsFPlayer(int idx, FPlayer def) {
        return this.argAsFPlayer(idx, def, true);
    }

    public FPlayer argAsFPlayer(int idx) {
        return this.argAsFPlayer(idx, null);
    }

    // BEST FPLAYER MATCH ======================
    public FPlayer strAsBestFPlayerMatch(String name, FPlayer def, boolean msg) {
        return strAsFPlayer(name, def, msg);
    }

    public FPlayer argAsBestFPlayerMatch(int idx, FPlayer def, boolean msg) {
        return this.strAsBestFPlayerMatch(this.argAsString(idx), def, msg);
    }

    public FPlayer argAsBestFPlayerMatch(int idx, FPlayer def) {
        return this.argAsBestFPlayerMatch(idx, def, true);
    }

    public FPlayer argAsBestFPlayerMatch(int idx) {
        return this.argAsBestFPlayerMatch(idx, null);
    }

    // FACTION ======================
    public Faction strAsFaction(String name, Faction def, boolean msg) {
        Faction ret = def;

        if (name != null) {
            // First we try an exact match
            Faction faction = Factions.getInstance().getByTag(name); // Checks for faction name match.

            // Now lets try for warzone / safezone. Helpful for custom warzone / safezone names.
            // Do this after we check for an exact match in case they rename the warzone / safezone
            // and a player created faction took one of the names.
            if (faction == null) {
                if (name.equalsIgnoreCase("warzone")) {
                    faction = Factions.getInstance().getWarZone();
                } else if (name.equalsIgnoreCase("safezone")) {
                    faction = Factions.getInstance().getSafeZone();
                }
            }

            // Next we match faction tags
            if (faction == null) {
                faction = Factions.getInstance().getBestTagMatch(name);
            }

            // Next we match player names
            if (faction == null) {
                FPlayer fplayer = strAsFPlayer(name, null, false);
                if (fplayer != null) {
                    faction = fplayer.getFaction();
                }
            }

            if (faction != null) {
                ret = faction;
            }
        }

        if (msg && ret == null) {
            sender.sendMessage(TL.GENERIC_NOFACTIONMATCH.format(name));
        }

        return ret;
    }

    public Faction argAsFaction(int idx, Faction def, boolean msg) {
        return this.strAsFaction(this.argAsString(idx), def, msg);
    }

    public Faction argAsFaction(int idx, Faction def) {
        return this.argAsFaction(idx, def, true);
    }

    public Faction argAsFaction(int idx) {
        return this.argAsFaction(idx, null);
    }

}
