package com.massivecraft.factions.config;

import java.util.ArrayList;
import java.util.List;

public class FactionConfig extends Config {

    @Node public boolean debug;

    /* Essentials integration */
    @Node public boolean deleteEssHomes = true;

    // TODO: Default Relation

    /* Find Exploit */
    @Node public FindExploit findExploit = new FindExploit();

    @Node(path = "findfactionsexploit")
    public class FindExploit extends Config {
        @Node public int cooldown = 2000;
        @Node public boolean log = false;
    }

    /* Nether Portals */
    @Node public Portals portals = new Portals();

    @Node(path = "portals")
    public class Portals extends Config {
        @Node public boolean limit = false;
        // TODO: Relation
    }

    /* Faction Warps */
    @Node public int maxWarps = 5;
    @Node public WarpCost warpCost = new WarpCost();

    @Node(path = "warp-cost")
    public class WarpCost extends Config {
        @Node public boolean enabled = false;
        @Node public int setwarp = 5;
        @Node public int delwarp = 5;
        @Node public int warp = 5;
    }

    /* Faction Flight */
    @Node public Fly fly = new Fly();

    @Node(path = "f-fly")
    public class Fly extends Config {
        @Node public boolean enabled = true;
        @Node public int falldamageCooldown = 3;
        @Node public int enemyRadius = 7;
        @Node public double radiusCheck = 1;
        @Node public boolean disableGenericDamage = false;
    }

    /* Faction Near */
    @Node(path = "f-near.radius")
    public int nearRadius = 20;

    /* Pistons */
    @Node public boolean disablePistonsInTerritory = false;

    /* Enter titles */
    @Node public EnterTitles enterTitles = new EnterTitles();

    @Node(path = "enter-titles")
    public class EnterTitles extends Config {
        @Node public boolean enabled = true;
        @Node public int fadeIn = 10;
        @Node public int fadeOut = 20;
        @Node public int stay = 70;
        @Node public boolean alsoShowChat = false;
    }

    /* See Chunk */
    @Node public SeeChunk seeChunk = new SeeChunk();

    @Node(path = "see-chunk")
    public class SeeChunk extends Config {
        @Node public boolean particles = true;
        @Node public String particle = "REDSTONE";
        @Node public boolean relationalColor = true;
        @Node public double particleUpdateTime = 0.75;
    }

    /* Tooltips */
    @Node(path = "tooltips.list") public List<String> listTooltip = new ArrayList<>();
    @Node(path = "tooltips.show") public List<String> showTooltip = new ArrayList<>();

    /* Warmups */
    @Node public Warmups warmups = new Warmups();

    @Node(path = "warmups")
    public class Warmups extends Config {
        @Node(path = "f-home") public int home;
        @Node(path = "f-warp") public int warp;
        @Node(path = "f-fly") public int fly;
    }

    /* Max Relations */
    @Node public MaxRelations maxRelations = new MaxRelations();

    @Node(path = "max-relations")
    public class MaxRelations extends Config {
        @Node public boolean enabled = false;
        @Node public int ally = 10;
        @Node public int truce = 10;
        @Node public int neutral = -1;
        @Node public int enemy = 10;
    }

    /* World border */
    @Node(path = "world-border.buffer") public int worldborderBuffer;

    /* HCF */
    @Node public HCF hcf = new HCF();

    @Node(path = "hcf")
    public class HCF extends Config {
        @Node public boolean raidable;
        @Node public boolean dtr;
        @Node public boolean allowOverclaim = true;
        @Node public int powerfreeze;
        @Node public int bufferZone;

        @Node public int stuckDelay = 30;
        @Node public int stuckRadius = 10;
    }

    /* Faction Show */
    @Node public List<String> show = new ArrayList<>();
    @Node public boolean minimalShow = false;
    @Node public List<String> showExempt = new ArrayList<>();

    /* Faction Map Tooltips */
    @Node public List<String> map = new ArrayList<>();

    /* Factions List */
    @Node public FList list = new FList();

    @Node(path = "list")
    static public class FList extends Config {
        @Node public String header = "&e&m----------&r&e[ &2Faction List &9{pagenumber}&e/&9{pagecount} &e]&m----------";
        @Node public String factionless = "<i>Factionless<i> {factionless} online";
        @Node public String entry = "<a>{faction} <i>{online} / {members} online, <a>Land / Power / Maxpower: <i>{chunks}/{power}/{maxPower}";
    }

    /* Help */
    @Node public boolean useOldHelp = true;

}
