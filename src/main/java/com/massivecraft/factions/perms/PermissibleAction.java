package com.massivecraft.factions.perms;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.massivecraft.factions.config.file.DefaultPermissionsConfig;

public enum PermissibleAction {
    BUILD(DefaultPermissionsConfig.Permissions::getBuild),
    DESTROY(DefaultPermissionsConfig.Permissions::getDestroy),
    PAINBUILD(DefaultPermissionsConfig.Permissions::getPainBuild),
    ITEM(DefaultPermissionsConfig.Permissions::getItem),
    CONTAINER(DefaultPermissionsConfig.Permissions::getContainer),
    BUTTON(DefaultPermissionsConfig.Permissions::getButton),
    DOOR(DefaultPermissionsConfig.Permissions::getDoor),
    LEVER(DefaultPermissionsConfig.Permissions::getLever),
    PLATE(DefaultPermissionsConfig.Permissions::getPlate),
    FROSTWALK(DefaultPermissionsConfig.Permissions::getFrostWalk),
    INVITE(true, DefaultPermissionsConfig.Permissions::getInvite),
    KICK(true, DefaultPermissionsConfig.Permissions::getKick),
    BAN(true, DefaultPermissionsConfig.Permissions::getBan),
    PROMOTE(true, DefaultPermissionsConfig.Permissions::getPromote),
    DISBAND(true, DefaultPermissionsConfig.Permissions::getDisband),
    ECONOMY(true, DefaultPermissionsConfig.Permissions::getEconomy),
    TERRITORY(true, DefaultPermissionsConfig.Permissions::getTerritory),
    SETHOME(true, DefaultPermissionsConfig.Permissions::getSetHome),
    SETWARP(true, DefaultPermissionsConfig.Permissions::getSetWarp),
    WARP(DefaultPermissionsConfig.Permissions::getWarp),
    FLY(DefaultPermissionsConfig.Permissions::getFly),
    ;

    private final boolean factionOnly;
    private Function<DefaultPermissionsConfig.Permissions, DefaultPermissionsConfig.Permissions.FullPermInfo> fullFunction;
    private Function<DefaultPermissionsConfig.Permissions, DefaultPermissionsConfig.Permissions.FactionOnlyPermInfo> factionOnlyFunction;

    PermissibleAction(Function<DefaultPermissionsConfig.Permissions, DefaultPermissionsConfig.Permissions.FullPermInfo> fullFunction) {
        this.factionOnly = false;
        this.fullFunction = fullFunction;
    }

    PermissibleAction(boolean factionOnly, Function<DefaultPermissionsConfig.Permissions, DefaultPermissionsConfig.Permissions.FactionOnlyPermInfo> factionOnlyFunction) {
        this.factionOnly = factionOnly;
        if (this.factionOnly) {
            this.factionOnlyFunction = factionOnlyFunction;
        } else {
            throw new AssertionError("May only set factionOnly actions in this constructor");
        }
    }

    private static Map<String, PermissibleAction> map = new HashMap<>();

    static {
        for (PermissibleAction action : values()) {
            map.put(action.name().toLowerCase(), action);
        }
    }

    public boolean isFactionOnly() {
        return this.factionOnly;
    }

    public DefaultPermissionsConfig.Permissions.FullPermInfo getFullPerm(DefaultPermissionsConfig.Permissions permissions) {
        return this.fullFunction.apply(permissions);
    }

    public DefaultPermissionsConfig.Permissions.FactionOnlyPermInfo getFactionOnly(DefaultPermissionsConfig.Permissions permissions) {
        return this.factionOnlyFunction.apply(permissions);
    }

    /**
     * Case insensitive check for action.
     *
     * @param check check
     * @return permissible
     */
    public static PermissibleAction fromString(String check) {
        return check == null ? null : map.get(check.toLowerCase());
    }

    @Override
    public String toString() {
        return name();
    }

}
