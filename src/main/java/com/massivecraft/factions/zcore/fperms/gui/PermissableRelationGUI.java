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
import org.bukkit.event.inventory.ClickType;
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

    private HashMap<Integer, Permissable> relationSlots = new HashMap<>();

    private final ConfigurationSection RELATION_CONFIG = P.p.getConfig().getConfigurationSection("fperm-gui.relation");


    public PermissableRelationGUI(FPlayer fme) {
        this.fme = fme;

        // Build basic Inventory info
        guiSize = RELATION_CONFIG.getInt("size", 27);
        guiName = ChatColor.translateAlternateColorCodes('&', RELATION_CONFIG.getString("name", "FactionPermissions"));
        relationGUI = Bukkit.createInventory(this, guiSize, guiName);

        for (String key : RELATION_CONFIG.getConfigurationSection("slots").getKeys(false)) {
            if (!RELATION_CONFIG.isInt("slots." + key) || RELATION_CONFIG.getInt("slots." + key) + 1 > guiSize) {
                P.p.log(Level.WARNING, "Invalid slot of " + key.toUpperCase() + " in relation GUI skipping it");
                continue;
            }
            if (getPermissable(key) == null) {
                P.p.log(Level.WARNING, "Invalid permissable " + key.toUpperCase() + " skipping it");
                continue;
            }

            relationSlots.put(RELATION_CONFIG.getInt("slots." + key), getPermissable(key));
        }

        buildItems();
    }

    @Override
    public Inventory getInventory() {
        return relationGUI;
    }

    @Override
    public void onClick(int slot, ClickType clickType) {
        if (!relationSlots.containsKey(slot)) {
            return;
        }

        fme.getPlayer().openInventory(new PermissableActionGUI(fme, relationSlots.get(slot)).getInventory());
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

            ItemStack item = permissable.buildItem();

            if (item == null) {
                P.p.log(Level.WARNING, "Invalid material for " + permissable.toString().toUpperCase() + " skipping it");
                continue;
            }

            relationGUI.setItem(entry.getKey(), item);
        }
    }

}
