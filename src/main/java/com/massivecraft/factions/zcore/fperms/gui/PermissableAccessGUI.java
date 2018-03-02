package com.massivecraft.factions.zcore.fperms.gui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.Permissable;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class PermissableAccessGUI implements InventoryHolder, PermissionGUI {


    private FPlayer fme;
    private Permissable permissable;
    private PermissableAction permissableAction;

    private Inventory accessGUI;

    private int guiSize;
    private String guiName;

    private String accessItemName;
    private List<String> accessItemLore = new ArrayList<>();

    private HashMap<Integer, Access> accessSlots = new HashMap<>();

    private final ConfigurationSection ACCESS_CONFIG = P.p.getConfig().getConfigurationSection("fperm-gui.access");

    public PermissableAccessGUI(FPlayer fme, Permissable permissable, PermissableAction permissableAction) {
        this.fme = fme;
        this.permissable = permissable;
        this.permissableAction = permissableAction;

        // Build basic Inventory
        guiSize = ACCESS_CONFIG.getInt("size", 27);
        guiName = ChatColor.translateAlternateColorCodes('&', ACCESS_CONFIG.getString("name", "FactionPerms"));
        accessGUI = Bukkit.createInventory(this, guiSize, guiName);

        for (String key : ACCESS_CONFIG.getConfigurationSection("items").getKeys(false)) {
            if (SpecialItem.isSpecial(key)) {
                continue;
            }

            Access access = Access.fromString(key.toUpperCase());
            if (access == null) {
                P.p.log(Level.WARNING, "Invalid access: " + key.toUpperCase());
                continue;
            }

            accessSlots.put(ACCESS_CONFIG.getInt("items." + key + ".slot"), access);
        }

        // Get base information
        accessItemName = ChatColor.translateAlternateColorCodes('&', ACCESS_CONFIG.getString("placeholder-item.name"));
        for (String loreLine : ACCESS_CONFIG.getStringList("placeholder-item.lore")) {
            accessItemLore.add(ChatColor.translateAlternateColorCodes('&', loreLine));
        }

        buildItems();
        buildSpecialItems();
    }

    @Override
    public Inventory getInventory() {
        return accessGUI;
    }

    @Override
    public void onClick(int slot) {
        if (!accessSlots.containsKey(slot)) {
            return;
        }

        fme.getPlayer().closeInventory();
        fme.getFaction().setPermission(permissable, permissableAction, accessSlots.get(slot));
        fme.msg(TL.COMMAND_PERM_SET, permissableAction.toString().toUpperCase(), accessSlots.get(slot).name(), permissable.toString().toUpperCase());
        P.p.log(String.format(TL.COMMAND_PERM_SET.toString().toUpperCase(), permissableAction.toString(), accessSlots.get(slot).name(), permissable.toString().toUpperCase()) + " for faction " + fme.getTag());
    }

    private void buildItems() {
        for (Map.Entry<Integer, Access> entry : accessSlots.entrySet()) {
            Access access = entry.getValue();

            ConfigurationSection accessConfig = ACCESS_CONFIG.getConfigurationSection("items." + access.name().toLowerCase());

            Material accessMaterial = Material.matchMaterial(accessConfig.getString("material"));
            if (accessMaterial == null) {
                P.p.log(Level.WARNING, "Invalid material for: " + access.name());
                continue;
            }

            ItemStack accessItem = new ItemStack(accessMaterial);

            DyeColor accessDye;
            try {
                accessDye = DyeColor.valueOf(accessConfig.getString("color"));
            } catch (Exception exception) {
                accessDye = null;
            }

            ItemMeta itemMeta = accessItem.getItemMeta();
            itemMeta.setDisplayName(access.replacePlaceholders(accessItemName, fme, permissable, permissableAction));

            List<String> lore = new ArrayList<>();
            for (String string : accessItemLore) {
                lore.add(access.replacePlaceholders(string, fme, permissable, permissableAction));
            }

            itemMeta.setLore(lore);
            accessItem.setItemMeta(itemMeta);

            if (accessDye != null && accessMaterial == Material.WOOL) {
                accessItem.setDurability((short) accessDye.getWoolData());
            }

            accessGUI.setItem(entry.getKey(), accessItem);
        }
    }

    private void buildSpecialItems() {

    }

}
