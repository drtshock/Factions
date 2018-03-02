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
import org.bukkit.entity.Item;
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

    private HashMap<Integer, PermissableAction> actionSlots = new HashMap<>();
    private HashMap<Integer, SpecialItem> specialSlots = new HashMap<>();

    private final ConfigurationSection ACTION_CONFIG = P.p.getConfig().getConfigurationSection("fperm-gui.action");

    public PermissableActionGUI(FPlayer fme, Permissable permissable) {
        this.fme = fme;
        this.permissable = permissable;

        guiSize = ACTION_CONFIG.getInt("size", 27);
        guiName = ChatColor.translateAlternateColorCodes('&', ACTION_CONFIG.getString("name", "FactionPerms"));
        actionGUI = Bukkit.createInventory(this, guiSize, guiName);

        for (String key : ACTION_CONFIG.getConfigurationSection("slots").getKeys(false)) {
            int slot = ACTION_CONFIG.getInt("slots." + key);
            if (slot + 1 > guiSize || slot < 0) {
                P.p.log(Level.WARNING, "Invalid slot for: " + key.toUpperCase());
                continue;
            }

            if (SpecialItem.isSpecial(key)) {
                if (SpecialItem.fromString(key) == SpecialItem.ACTION) {
                    continue;
                }
                specialSlots.put(slot, SpecialItem.fromString(key));
                continue;
            }

            PermissableAction permissableAction = PermissableAction.fromString(key.toUpperCase().replace('-', '_'));
            if (permissableAction == null) {
                P.p.log(Level.WARNING, "Invalid permissable action: " + key.toUpperCase());
                continue;
            }

            actionSlots.put(ACTION_CONFIG.getInt("slots." + key), permissableAction);
        }

        buildItems();
        buildSpecialItems();
    }

    @Override
    public Inventory getInventory() {
        return actionGUI;
    }

    @Override
    public void onClick(int slot) {
        if (specialSlots.containsKey(slot)) {
            if (specialSlots.get(slot) == SpecialItem.BACK) {
                fme.getPlayer().openInventory(new PermissableRelationGUI(fme).getInventory());
            }
        }
        if (!actionSlots.containsKey(slot)) {
            return;
        }
        fme.getPlayer().openInventory(new PermissableAccessGUI(fme, permissable, actionSlots.get(slot)).getInventory());
    }

    private void buildItems() {
        for (Map.Entry<Integer, PermissableAction> entry : actionSlots.entrySet()) {
            PermissableAction permissableAction = entry.getValue();

            ItemStack item = permissableAction.buildItem(fme, permissable);

            if (item == null) {
                P.p.log(Level.WARNING, "Invalid item for: " + permissableAction.toString().toUpperCase());
                continue;
            }

            actionGUI.setItem(entry.getKey(), item);
        }
    }

    private void buildSpecialItems() {
        for (Map.Entry<Integer, SpecialItem> entry : specialSlots.entrySet()) {
            actionGUI.setItem(entry.getKey(), getSpecialItem(entry.getValue()));
        }
    }

    private ItemStack getSpecialItem(SpecialItem specialItem) {
        switch (specialItem) {
            case RELATION:
                return permissable.buildItem();
            case BACK:
                ConfigurationSection backButtonConfig = P.p.getConfig().getConfigurationSection("fperm-gui.back-button");

                ItemStack backButton = new ItemStack(Material.matchMaterial(backButtonConfig.getString("material")));
                ItemMeta backButtonMeta = backButton.getItemMeta();

                backButtonMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', backButtonConfig.getString("name")));
                List<String> lore = new ArrayList<>();
                for (String loreLine : backButtonConfig.getStringList("lore")) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
                }
                backButtonMeta.setLore(lore);

                backButton.setItemMeta(backButtonMeta);

                return backButton;
            default:
                return null;
        }
    }

}
