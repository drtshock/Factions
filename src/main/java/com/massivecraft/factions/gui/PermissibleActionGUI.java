package com.massivecraft.factions.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.perms.PermissibleAction;
import com.massivecraft.factions.util.material.FactionMaterial;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.event.inventory.ClickType;

public class PermissibleActionGUI extends GUI<PermissibleAction> implements GUI.Backable {
    private static SimpleItem backItem = SimpleItem.builder().setMaterial(FactionMaterial.from("ARROW").get()).setName("BACK").build();
    private static SimpleItem allow;
    private static SimpleItem allowLocked;
    private static SimpleItem disallow;
    private static SimpleItem disallowLocked;

    static {
        List<String> lore = ImmutableList.of(
                "&8Access:",
                "&8[{action-access-color}{action-access}&8]",
                "",
                "&8Left click to &a&lAllow",
                "&8Right click to &c&lDeny");
        SimpleItem.Builder builder = SimpleItem.builder().setLore(lore).setName("&8[{action-access-color}{action}&8]");
        allow = builder.setMaterial(FactionMaterial.from("LIME_TERRACOTTA").get()).setColor(DyeColor.LIME).build();
        disallow = builder.setMaterial(FactionMaterial.from("RED_TERRACOTTA").get()).setColor(DyeColor.RED).build();
        allowLocked = builder.setMaterial(FactionMaterial.from("GREEN_TERRACOTTA").get()).setColor(DyeColor.GREEN).build();
        disallowLocked = builder.setMaterial(FactionMaterial.from("MAGENTA_TERRACOTTA").get()).setColor(DyeColor.MAGENTA).build();
    }

    private Permissible permissible;
    private boolean online;

    public PermissibleActionGUI(boolean online, FPlayer user, Permissible permissible) {
        super(user, (int) Math.ceil(((double) PermissibleAction.values().length) / 9d) + 1);
        this.permissible = permissible;
        this.online = online;
        build();
    }

    @Override
    protected String getName() {
        return "Permissions: " + permissible.name().toLowerCase();
    }

    @Override
    protected String parse(String toParse, PermissibleAction action) {
        String actionName = action.name().substring(0, 1).toUpperCase() + action.name().substring(1);
        toParse = toParse.replace("{action}", actionName);

        boolean access = user.getFaction().hasAccess(online, permissible, action);
        String extra = "";
        if (user.getFaction().isLocked(online, permissible, action)) {
            extra = " (Locked)";
        }

        toParse = toParse.replace("{action-access}", (access ? "Allow" : "Deny") + extra);
        toParse = toParse.replace("{action-access-color}", access ? ChatColor.GREEN.toString() : ChatColor.DARK_RED.toString());

        return toParse;
    }

    @Override
    protected void onClick(PermissibleAction action, ClickType click) {
        boolean access;
        if (click == ClickType.LEFT) {
            access = true;
        } else if (click == ClickType.RIGHT) {
            access = false;
        } else {
            return;
        }
        if (user.getFaction().setPermission(online, permissible, action, access)) {
            // Reload items to reparse placeholders
            buildItem(action);
            user.msg(TL.COMMAND_PERM_SET, action.name(), access ? "allow" : "deny", permissible.name());
            P.p.log(TL.COMMAND_PERM_SET.format(access ? "allow" : "deny", access ? "Allow" : "Deny", permissible.name()) + " for faction " + user.getTag());
        } else {
            user.msg(TL.COMMAND_PERM_INVALID_SET);
        }
    }

    @Override
    protected Map<Integer, PermissibleAction> createSlotMap() {
        Map<Integer, PermissibleAction> map = new HashMap<>();
        int i = 0;
        for (PermissibleAction action : PermissibleAction.values()) {
            map.put(i++, action);
        }
        return map;
    }

    @Override
    protected SimpleItem getItem(PermissibleAction permissibleAction) {
        boolean access = user.getFaction().hasAccess(online, permissible, permissibleAction);
        boolean locked = user.getFaction().isLocked(online, permissible, permissibleAction);
        return new SimpleItem(access ? (locked ? allowLocked : allow) : (locked ? disallowLocked : disallow));
    }

    @Override
    protected Map<Integer, SimpleItem> createDummyItems() {
        return ImmutableMap.of(this.back = ((PermissibleAction.values().length / 9) + 1) * 9, backItem);
    }

    // For dummy items only parseDefault is called, but we want to provide the relation placeholders, so: Override
    @Override
    protected String parseDefault(String string) {
        String parsed = super.parseDefault(string);

        String permissibleName = permissible.toString().substring(0, 1).toUpperCase() + permissible.toString().substring(1);
        parsed = parsed.replace("{relation-color}", permissible.getColor().toString());
        parsed = parsed.replace("{relation}", permissibleName);
        return parsed;
    }

    @Override
    public void onBack() {
        new PermissibleRelationGUI(online, user).open();
    }
}
