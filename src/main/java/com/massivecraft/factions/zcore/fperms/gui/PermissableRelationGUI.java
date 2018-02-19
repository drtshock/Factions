package com.massivecraft.factions.zcore.fperms.gui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.fperms.Permissable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class PermissableRelationGUI implements InventoryHolder, PermissionGUI {

    private Inventory relationGUI;
    private FPlayer fme;

    private int guiSize;
    private String guiName;

    private String relationItemName;
    private List<String> relationItemLore = new ArrayList<>();

    private HashMap<Integer, Permissable> relationSlots = new HashMap<>();

    private ConfigurationSection relationConfig = P.p.getConfig().getConfigurationSection("fperm-gui.relation");


    public PermissableRelationGUI(FPlayer fme) {
        this.fme = fme;

        // Build basic Inventory info
        guiSize = relationConfig.getInt("size", 27);
        guiName = ChatColor.translateAlternateColorCodes('&', relationConfig.getString("name", "FactionPermissions"));
        relationGUI = Bukkit.createInventory(this, guiSize, guiName);

        for (String key : relationConfig.getConfigurationSection("slots").getKeys(false)) {
            if (!relationConfig.isInt("slots." + key) || relationConfig.getInt("slots." + key) + 1 > guiSize) {
                P.p.log(Level.WARNING, "Invalid slot of " + key.toUpperCase() + " in relation GUI skipping it");
                continue;
            }
            if (getPermissable(key) == null) {
                P.p.log(Level.WARNING, "Invalid permissable " + key.toUpperCase() + " skipping it");
                continue;
            }

            relationSlots.put(relationConfig.getInt("slots." + key), getPermissable(key));
        }

        // Get base item information
        relationItemName = ChatColor.translateAlternateColorCodes('&', relationConfig.getString("item.name"));
        for (String loreLine : relationConfig.getStringList("item.lore")) {
            relationItemLore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        }

        // Build items
        buildItems();
    }

    @Override
    public Inventory getInventory() {
        return relationGUI;
    }

    @Override
    public void onClick(int slot) {
        if (!relationSlots.containsKey(slot)) {
            return;
        }

        // Pass this through the constructor for ease of use in Action GUI
        ItemStack relationItem = relationGUI.getItem(slot);
        fme.getPlayer().openInventory(new PermissableActionGUI(fme, relationSlots.get(slot), relationItem).getInventory());
    }

    private Permissable getPermissable(String name) {
        try {
            return Relation.valueOf(name.toUpperCase());
        } catch (Exception e) {
        }
        try {
            return Role.valueOf(name.toUpperCase());
        } catch (Exception e) {
        }

        return null;
    }

    private void buildItems() {
        for (Map.Entry<Integer, Permissable> entry : relationSlots.entrySet()) {
            Permissable permissable = entry.getValue();

            String name = replacePlaceholers(relationItemName, permissable);
            List<String> lore = new ArrayList<>();
            ItemStack item = new ItemStack(Material.matchMaterial(relationConfig.getString("materials." + permissable.toString())));
            ItemMeta itemMeta = item.getItemMeta();

            for (String loreLine : relationItemLore) {
                lore.add(replacePlaceholers(loreLine, permissable));
            }

            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

            relationGUI.setItem(entry.getKey(), item);
        }
    }

    private String replacePlaceholers(String string, Permissable permissable) {
        String permissableName = permissable.toString().substring(0, 1).toUpperCase() + permissable.toString().substring(1);

        string = string.replace("{relation-color}", permissable.getColor().toString());
        string = string.replace("{relation-name}", permissableName);

        return string;
    }

}
