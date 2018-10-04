package com.massivecraft.factions.cmd.tabcomplete;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
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
    TabCompleteProvider FACTIONS = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> factions = new ArrayList<>();
            for (Faction faction : Factions.getInstance().getAllFactions()) {
                factions.add(faction.getTag());
            }
            return factions;
        }
    };

    TabCompleteProvider ROLES = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> roles = new ArrayList<>();
            for (Role role : Role.values()) {
                roles.add(role.name().toLowerCase());
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
    TabCompleteProvider PERMISSABLES = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> permissables = ROLES.get();
            permissables.addAll(RELATIONS.get());
            return permissables;
        }
    };
    TabCompleteProvider ACCESS = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> accessNames = new ArrayList<>();
            for (Access access : Access.values()) {
                accessNames.add(access.getName().toLowerCase());
            }
            return accessNames;
        }
    };
    TabCompleteProvider ACTIONS = new TabCompleteProvider() {
        @Override
        public List<String> get() {
            List<String> actions = new ArrayList<>();
            for (PermissableAction action : PermissableAction.values()) {
                actions.add(action.getName());
            }
            return actions;
        }
    };

}
