package com.massivecraft.factions.config.transition;

import com.google.gson.Gson;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.config.transition.oldclass.OldMemoryFaction;

import java.util.HashMap;
import java.util.Map;

public class FactionsMigrator {
    public static class OldFactions {
        public final Map<String, OldMemoryFaction> factions = new HashMap<>();
        public int nextId = 1;
    }

    public static class NewFactions {
        public Map<String, Faction> factions = new HashMap<>();
        public int nextId;

        public NewFactions(OldFactions oldFactions) {

        }
    }

    public static void migrate(Gson oldGson, Gson newGson) {

    }
}
