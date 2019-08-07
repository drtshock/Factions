package com.massivecraft.factions.perms;

import java.util.ArrayList;
import java.util.List;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.tag.Tag;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface Permissible {

    default ItemStack buildGUIItem(FPlayer fme) {
        final ConfigurationSection RELATION_CONFIG = P.p.getConfig().getConfigurationSection("fperm-gui.relation");

        String displayName = replacePlaceholders(RELATION_CONFIG.getString("placeholder-item.name", ""), fme);
        List<String> lore = new ArrayList<>();

        Material material = Material.matchMaterial(RELATION_CONFIG.getString("materials." + name().toLowerCase(), "STAINED_CLAY"));
        if (material == null) {
            return null;
        }

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        for (String loreLine : RELATION_CONFIG.getStringList("placeholder-item.lore")) {
            lore.add(replacePlaceholders(loreLine, fme));
        }

        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);

        return item;
    }

    default String replacePlaceholders(String string, FPlayer fme) {
        string = ChatColor.translateAlternateColorCodes('&', string);
        string = Tag.parsePlain(fme, string);
        string = Tag.parsePlain(fme.getFaction(), string);

        String permissibleName = this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1);

        string = string.replace("{relation-color}", getColor().toString());
        string = string.replace("{relation}", permissibleName);

        return string;
    }

    String name();

    ChatColor getColor();

}
