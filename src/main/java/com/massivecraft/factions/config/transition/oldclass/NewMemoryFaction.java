package com.massivecraft.factions.config.transition.oldclass;

import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.BanInfo;
import com.massivecraft.factions.util.LazyLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class NewMemoryFaction {
    private String id;
    private boolean peacefulExplosionsEnabled;
    private boolean permanent;
    private String tag;
    private String description;
    private boolean open;
    private boolean peaceful;
    private Integer permanentPower;
    private LazyLocation home;
    private long foundedDate;
    private double powerBoost;
    private Map<String, Relation> relationWish;
    private Map<FLocation, Set<String>> claimOwnership;
    private Set<String> invites;
    private HashMap<String, List<String>> announcements;
    private ConcurrentHashMap<String, LazyLocation> warps;
    private ConcurrentHashMap<String, String> warpPasswords;
    private long lastDeath;
    private int maxVaults;
    private Role defaultRole;
    private Map<Permissible, Map<PermissibleAction, Boolean>> permissions;
    private Map<Permissible, Map<PermissibleAction, Boolean>> permissionsOffline;
    private Set<BanInfo> bans;

    public NewMemoryFaction(OldMemoryFaction old) {
        this.id = old.id;
        this.peacefulExplosionsEnabled = old.peacefulExplosionsEnabled;
        this.permanent = old.permanent;
        this.tag = old.tag;
        this.description = old.description;
        this.open = old.open;
        this.peaceful = old.peaceful;
        this.permanentPower = old.permanentPower;
        this.home = old.home;
        this.foundedDate = old.foundedDate;
        this.powerBoost = old.powerBoost;
        this.relationWish = old.relationWish;
        this.claimOwnership = old.claimOwnership;
        this.invites = old.invites;
        this.announcements = old.announcements;
        this.warps = old.warps;
        this.warpPasswords = old.warpPasswords;
        this.lastDeath = old.lastDeath;
        this.maxVaults = old.maxVaults;
        this.defaultRole = old.defaultRole;
        this.permissions = new HashMap<>();
        this.permissionsOffline = new HashMap<>();
        old.permissions.forEach((permiss, map) -> {
            Map<PermissibleAction, Boolean> newMap = new HashMap<>();
            map.forEach((permact, access) -> {
                if (access == Access.ALLOW || access == Access.DENY) {
                    newMap.put(permact.getNew(), access == Access.ALLOW);
                }
            });
            this.permissions.put(permiss.newPermissible(), newMap);
            this.permissionsOffline.put(permiss.newPermissible(), newMap);
        });
        this.bans = old.bans;
    }
}