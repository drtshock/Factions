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

    private ConfigurationSection actionConfig = P.p.getConfig().getConfigurationSection("fperm-gui.action");

    public PermissableActionGUI(FPlayer fme, Permissable permissable, ItemStack relationItem) {
        this.fme = fme;
        this.permissable = permissable;

        // Build basic Inventory info
        guiSize = actionConfig.getInt("size", 27);
        guiName = ChatColor.translateAlternateColorCodes('&', actionConfig.getString("name", "FactionPerms"));
        actionGUI = Bukkit.createInventory(this, guiSize, guiName);

        // Add Back Button and current relation information
        actionGUI.setItem(actionConfig.getInt("slots.relation-info", 4), relationItem);
        backButtonSlot = actionConfig.getInt("slots.back", 0);

        actionItemName = ChatColor.translateAlternateColorCodes('&', actionConfig.getString("item.name", "{action-name}"));
        actionItemLore = translateColors(actionConfig.getStringList("item.lore"));

        // Build actions into HashMap
        for (String key : actionConfig.getConfigurationSection("slots").getKeys(false)) {
            if (key.equalsIgnoreCase("back") || key.equalsIgnoreCase("relation-info")) {
                continue;
            }

            if (!actionConfig.isInt("slots." + key) || actionConfig.getInt("slots." + key) + 1 > guiSize) {
                P.p.log(Level.WARNING, "Invalid slot of " + key.toUpperCase() + " in action GUI skipping it");
                continue;
            }

            PermissableAction permissableAction = PermissableAction.fromString(key);
            if (permissableAction == null) {
                P.p.log(Level.WARNING, "Invalid permissable action " + key.toUpperCase() + " skipping it");
                continue;
            }

            actionSlots.put(actionConfig.getInt("slots." + key), permissableAction);
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

            String name = replacePlaceholers(actionItemName, permissableAction);
            List<String> lore = new ArrayList<>();
            ItemStack item = new ItemStack(Material.matchMaterial(actionConfig.getString("materials." + permissableAction.name().toLowerCase(), "COBBLESTONE")));
            ItemMeta itemMeta = item.getItemMeta();

            for (String loreLine : actionItemLore) {
                lore.add(replacePlaceholers(loreLine, permissableAction));
            }

            itemMeta.setDisplayName(name);
            itemMeta.setLore(lore);
            item.setItemMeta(itemMeta);

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

    private String replacePlaceholers(String string, PermissableAction permissableAction) {
        String actionName = permissableAction.getName().substring(0, 1).toUpperCase() + permissableAction.getName().substring(1);
        string = string.replace("{action-name}", actionName);

        Access access = fme.getFaction().getAccess(permissable, permissableAction);
        if (access == null) {
            access = Access.UNDEFINED;
        }
        String actionAccess = access.getName();
        string = string.replace("{action-access}", actionAccess);
        string = string.replace("{action-access-color}", access.getColor().toString());

        return string;
    }

}
