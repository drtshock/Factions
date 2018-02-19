package com.massivecraft.factions.zcore.fperms.gui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.Permissable;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
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

public class PermissableActionGUI implements InventoryHolder, PermissionGUI {

    private Inventory actionGUI;
    private FPlayer fme;

    private int guiSize;
    private String guiName;

    private Permissable permissable;

    private String actionItemName;
    private List<String> actionItemLore;

    private HashMap<Integer, PermissableAction> actionSlots = new HashMap<>();

    private int backButtonSlot;

    private final ConfigurationSection ACTION_CONFIG = P.p.getConfig().getConfigurationSection("fperm-gui.action");

    public PermissableActionGUI(FPlayer fme, Permissable permissable, ItemStack relationItem) {
        this.fme = fme;
        this.permissable = permissable;

        // Build basic Inventory info
        guiSize = ACTION_CONFIG.getInt("size", 27);
        guiName = ChatColor.translateAlternateColorCodes('&', ACTION_CONFIG.getString("name", "FactionPerms"));
        actionGUI = Bukkit.createInventory(this, guiSize, guiName);

        // Add Back Button and current relation information
        actionGUI.setItem(ACTION_CONFIG.getInt("slots.relation-info", 4), relationItem);
        backButtonSlot = ACTION_CONFIG.getInt("slots.back", 0);

        actionItemName = ChatColor.translateAlternateColorCodes('&', ACTION_CONFIG.getString("item.name", "{action-name}"));
        actionItemLore = translateColors(ACTION_CONFIG.getStringList("item.lore"));

        // Build actions into HashMap
        for (String key : ACTION_CONFIG.getConfigurationSection("slots").getKeys(false)) {
            if (key.equalsIgnoreCase("back") || key.equalsIgnoreCase("relation-info")) {
                continue;
            }

            if (!ACTION_CONFIG.isInt("slots." + key) || ACTION_CONFIG.getInt("slots." + key) + 1 > guiSize) {
                P.p.log(Level.WARNING, "Invalid slot of " + key.toUpperCase() + " in action GUI skipping it");
                continue;
            }

            PermissableAction permissableAction = PermissableAction.fromString(key.toUpperCase());
            if (permissableAction == null) {
                P.p.log(Level.WARNING, "Invalid permissable action " + key.toUpperCase() + " skipping it");
                continue;
            }

            actionSlots.put(ACTION_CONFIG.getInt("slots." + key), permissableAction);
        }

        buildItems();
    }

    @Override
    public Inventory getInventory() {
        return actionGUI;
    }

    @Override
    public void onClick(int slot) {
        if (slot == backButtonSlot) {
            fme.getPlayer().openInventory(new PermissableRelationGUI(fme).getInventory());
        }
        if (!actionSlots.containsKey(slot)) {
            return;
        }
        fme.getPlayer().openInventory(new PermissableAccessGUI(fme, permissable, actionSlots.get(slot)).getInventory());
    }

    private void buildItems() {
        // Build Back button
        ConfigurationSection backButtonConfig = P.p.getConfig().getConfigurationSection("fperm-gui.back-button");

        ItemStack backButton = new ItemStack(Material.matchMaterial(backButtonConfig.getString("material")));
        ItemMeta backButtonMeta = backButton.getItemMeta();

        backButtonMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', backButtonConfig.getString("name")));
        backButtonMeta.setLore(translateColors(backButtonConfig.getStringList("lore")));

        backButton.setItemMeta(backButtonMeta);
        actionGUI.setItem(backButtonSlot, backButton);
        // Finish Back Button

        for (Map.Entry<Integer, PermissableAction> entry : actionSlots.entrySet()) {
            PermissableAction permissableAction = entry.getValue();

            ItemStack item = permissableAction.buildItem(fme, permissable, actionItemName, actionItemLore);

            if (item == null) {
                P.p.log(Level.WARNING, "Invalid material for " + permissableAction.toString().toUpperCase() + " skipping it");
                continue;
            }

            actionGUI.setItem(entry.getKey(), item);
        }
    }

    private List<String> translateColors(List<String> stringList) {
        List<String> coloredList = new ArrayList<>();
        for (String loreLine : stringList) {
            coloredList.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        }
        return coloredList;
    }

}
