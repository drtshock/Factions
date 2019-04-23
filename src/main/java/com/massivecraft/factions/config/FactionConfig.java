package com.massivecraft.factions.config;

import com.massivecraft.factions.P;

import java.util.ArrayList;
import java.util.List;

public class FactionConfig extends Config {

    @Node public boolean debug = false;

    @Node public boolean deleteEssHomes = true;

    // TODO: Default Relation

    @Node public int maxWarps;

    @Node public List<String> map = new ArrayList<>();

    @Node public List<String> showExempt = new ArrayList<>();


    public FindExploit findExploit = new FindExploit();

    @Node(path = "findfactionsexploit")
    public class FindExploit extends Config {
        @Node
        public int cooldown = 2000;
        @Node
        public boolean log = false;
    }

    public Portals portals = new Portals();

    @Node(path = "portals")
    public class Portals extends Config {
        @Node
        public boolean limit = false;

        // TODO: Relation
    }

    public void load() {
        super.load();
        findExploit.load();
        portals.load();

        P.p.log("Loaded FactionConfig");
    }

}
