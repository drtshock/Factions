package com.massivecraft.factions.zcore.fperms.gui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.Permissable;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import com.massivecraft.factions.zcore.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class PermissableAccessGUI implements InventoryHolder, PermissionGUI {


    private FPlayer fme;
    private Permissable permissable;
    private PermissableAction permissableAction;

    private Inventory accessGUI;

    private int guiSize;
    private String guiName;

    private HashMap<Integer, Access> accessSlots = new HashMap<>();

    private ConfigurationSection accessConfig = P.p.getConfig().getConfigurationSection("fperm-gui.access");

    public PermissableAccessGUI(FPlayer fme, Permissable permissable, PermissableAction permissableAction) {
        this.fme = fme;
        this.permissable = permissable;
        this.permissableAction = permissableAction;


        // Build basic Inventory
        guiSize = accessConfig.getInt("size", 27);
        guiName = ChatColor.translateAlternateColorCodes('&', accessConfig.getString("name", "FactionPerms"));
        accessGUI = Bukkit.createInventory(this, guiSize, guiName);

        // Temporary GUI items
        accessSlots.put(12, Access.ALLOW);
        accessSlots.put(13, Access.UNDEFINED);
        accessSlots.put(14, Access.DENY);
        buildItems();
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
        // Temporary system in place
        ItemStack allow = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getWoolData());
        accessGUI.setItem(12, allow);
        ItemStack undefined = new ItemStack(Material.WOOL, 1, DyeColor.GRAY.getWoolData());
        accessGUI.setItem(13, undefined);
        ItemStack deny = new ItemStack(Material.WOOL, 1, DyeColor.RED.getWoolData());
        accessGUI.setItem(14, deny);
    }

}
