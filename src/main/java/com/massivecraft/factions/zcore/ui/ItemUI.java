package com.massivecraft.factions.zcore.ui;

import com.massivecraft.factions.P;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Colorable;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Wool;

import java.util.List;
import java.util.logging.Level;

public class ItemUI {

    private String name;
    private List<String> lore;
    private Material material;
    private DyeColor color;

    public ItemUI(String name, List<String> lore, Material material) {
        this.name = name;
        this.lore = lore;
        this.material = material;
    }

    public ItemUI(ItemUI itemUI) {
        this.name = itemUI.getName();
        this.lore = itemUI.getLore();
        this.material = itemUI.getMaterial();
        this.color = itemUI.getColor();
    }

    public ItemStack get() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        if (getColor() != null && (itemStack.getData() instanceof Colorable || material == Material.STAINED_GLASS_PANE || material == Material.STAINED_GLASS || material == Material.STAINED_CLAY)) {
            // ItemStack.setData() does not work :(
            itemStack.setDurability(color.getWoolData());
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemUI fromConfigSection(ConfigurationSection section) {
        String name = section.getString("name");
        List<String> lore = section.getStringList("lore");
        Material material = Material.matchMaterial(section.getString("material"));
        if (name != null || material != null) {
            ItemUI itemUI = new ItemUI(name, lore, material);

            String colorName = section.getString("color");
            if (colorName != null) {
                try {
                    itemUI.setColor(DyeColor.valueOf(colorName.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    P.p.log(Level.WARNING, "Invalid Color: " + colorName);
                }
            }

            return itemUI;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public DyeColor getColor() {
        return color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }
}
