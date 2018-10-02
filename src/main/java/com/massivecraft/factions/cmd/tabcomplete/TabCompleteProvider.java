package com.massivecraft.factions.cmd.tabcomplete;

import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface TabCompleteProvider {

    List<String> get();

    TabCompleteProvider PLAYERS = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> players = new ArrayList<>();
            for (Player online : Bukkit.getOnlinePlayers()) {
                players.add(online.getName());
            }
            return players;
        }
    };

    TabCompleteProvider ROLES = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> roles = new ArrayList<>();
            for (Role role : Role.values()) {
                roles.add(role.nicename);
            }
            return roles;
        }
    };
    TabCompleteProvider RELATIONS = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> relations = new ArrayList<>();
            for (Relation relation : Relation.values()) {
                relations.add(relation.nicename);
            }
            return relations;
        }
    };

}
