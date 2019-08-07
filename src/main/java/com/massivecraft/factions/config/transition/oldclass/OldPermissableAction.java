package com.massivecraft.factions.config.transition.oldclass;

import com.massivecraft.factions.perms.PermissibleAction;

public enum OldPermissableAction {
    BAN("ban", PermissibleAction.BAN),
    BUILD("build", PermissibleAction.BUILD),
    DESTROY("destroy", PermissibleAction.DESTROY),
    FROST_WALK("frostwalk", PermissibleAction.FROSTWALK),
    PAIN_BUILD("painbuild", PermissibleAction.PAINBUILD),
    DOOR("door", PermissibleAction.DOOR),
    BUTTON("button", PermissibleAction.BUTTON),
    LEVER("lever", PermissibleAction.LEVER),
    CONTAINER("container", PermissibleAction.CONTAINER),
    INVITE("invite", PermissibleAction.INVITE),
    KICK("kick", PermissibleAction.KICK),
    ITEM("items", PermissibleAction.ITEM), // generic for most items
    SETHOME("sethome", PermissibleAction.SETHOME),
    WITHDRAW("withdraw", PermissibleAction.ECONOMY),
    TERRITORY("territory", PermissibleAction.TERRITORY),
    ACCESS("access", PermissibleAction.PLATE),
    DISBAND("disband", PermissibleAction.DISBAND),
    PROMOTE("promote", PermissibleAction.PROMOTE),
    SETWARP("setwarp", PermissibleAction.SETWARP),
    WARP("warp", PermissibleAction.WARP),
    FLY("fly", PermissibleAction.FLY),
    ;

    private String name;

    private PermissibleAction action;

    OldPermissableAction(String name, PermissibleAction newPermissibleAction) {
        this.name = name;
        this.action = newPermissibleAction;
    }

    public PermissibleAction getNew() {
        return this.action;
    }

    /**
     * Get the friendly name of this action. Used for editing in commands.
     *
     * @return friendly name of the action as a String.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Case insensitive check for action.
     *
     * @param check check
     * @return permissible
     */
    public static OldPermissableAction fromString(String check) {
        for (OldPermissableAction permissableAction : values()) {
            if (permissableAction.name().equalsIgnoreCase(check)) {
                return permissableAction;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
