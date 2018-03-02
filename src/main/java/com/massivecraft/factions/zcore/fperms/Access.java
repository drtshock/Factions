package com.massivecraft.factions.zcore.fperms;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public enum Access {
    ALLOW("Allow", ChatColor.GREEN),
    DENY("Deny", ChatColor.DARK_RED),
    UNDEFINED("Undefined", ChatColor.GRAY);

    private String name;
    private ChatColor color;

    Access(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Case insensitive check for access.
     *
     * @param check
     * @return
     */
    public static Access fromString(String check) {
        for (Access access : values()) {
            if (access.name().equalsIgnoreCase(check)) {
                return access;
            }
        }

        return null;
    }

    public String getName() {
        return this.name;
    }

    public ChatColor getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name();
    }

    public ItemStack buildItem(FPlayer fme, Permissable permissable, PermissableAction permissableAction) {
        final ConfigurationSection ACCESS_CONFIG = P.p.getConfig().getConfigurationSection("fperm-gui.access");

        Material accessMaterial = Material.matchMaterial(ACCESS_CONFIG.getString("items." + name().toLowerCase() + ".material", ""));
        if (accessMaterial == null) {
            P.p.log(Level.WARNING, "Invalid material for: " + name());
            return null;
        }

        ItemStack item = new ItemStack(accessMaterial);

        DyeColor accessDye;
        try {
            accessDye = DyeColor.valueOf(ACCESS_CONFIG.getString("items." + name().toLowerCase() + ".color", ""));
        } catch (Exception exception) {
            accessDye = null;
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(replacePlaceholders(ACCESS_CONFIG.getString("placeholder-item.name", ""), fme, permissable, permissableAction));

        List<String> lore = new ArrayList<>();
        for (String string : ACCESS_CONFIG.getStringList("placeholder-item.lore")) {
            lore.add(replacePlaceholders(string, fme, permissable, permissableAction));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        if (accessDye != null && accessMaterial == Material.WOOL) {
            item.setDurability((short) accessDye.getWoolData());
        }

        return item;
    }

    public String replacePlaceholders(String string, FPlayer fme, Permissable permissable, PermissableAction permissableAction) {
        // Run other placeholders
        string = permissableAction.replacePlaceholers(string, fme, permissable);
        string = string.replace("{access}", name);
        string = string.replace("{access-color}", color.toString());

        return string;
    }

}
