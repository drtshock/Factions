package com.massivecraft.factions.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.zcore.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class GUI<T> implements InventoryHolder {
    protected Inventory inventory;

    protected final int size;
    protected int back = -1;

    private Map<Integer, T> slotMap = new HashMap<>();

    protected FPlayer user;

    public GUI(FPlayer user, int rows) {
        this.size = rows * 9;
        this.user = user;
    }

    protected abstract String getName();

    // Parse all the placeholder values in this String, will be injected into the SimpleItem and return it
    protected abstract String parse(String toParse, T type);

    protected abstract void onClick(T action, ClickType clickType);

    // Should only be called by the InventoryListener
    public void click(int slot, ClickType clickType) {
        if (slotMap.containsKey(slot)) {
            onClick(slotMap.get(slot), clickType);
        } else if (this instanceof Backable && back == slot) {
            ((Backable) this).onBack();
        }
    }

    protected abstract Map<Integer, T> createSlotMap();

    public void build() {
        String guiName = this.getName();
        inventory = Bukkit.createInventory(this, size, guiName);

        this.slotMap = this.createSlotMap();

        buildDummyItems();
        buildItems();
    }

    protected abstract SimpleItem getItem(T t);

    protected void buildItems() {
        for (Map.Entry<Integer, T> entry : slotMap.entrySet()) {
            T type = entry.getValue();
            SimpleItem item = getItem(type);
            parse(item, type);
            inventory.setItem(entry.getKey(), item.get());
        }
    }

    protected abstract Map<Integer, SimpleItem> createDummyItems();

    protected void buildDummyItems() {
        for (Map.Entry<Integer, SimpleItem> entry : createDummyItems().entrySet()) {
            SimpleItem item = entry.getValue();
            parse(item, null);
            inventory.setItem(entry.getKey(), item.get());
        }
    }

    // Will parse default faction stuff, ie: Faction Name, Power, Colors etc
    protected void parse(SimpleItem item, T type) {
        item.setName(parseDefault(item.getName()));
        if (type != null) {
            item.setName(parse(item.getName(), type));
        }
        if (item.getLore() != null) {
            item.setLore(parseList(item.getLore(), type));
        }
    }

    protected List<String> parseList(List<String> stringList, T type) {
        List<String> newList = new ArrayList<>();
        for (String toParse : stringList) {
            String parsed = parseDefault(toParse);
            if (type != null) {
                parsed = parse(parsed, type);
            }
            newList.add(parsed);
        }
        return newList;
    }

    protected String parseDefault(String toParse) {
        toParse = TextUtil.parseColor(toParse);
        toParse = Tag.parsePlain(user, toParse);
        toParse = Tag.parsePlain(user.getFaction(), toParse);
        return Tag.parsePlaceholders(user.getPlayer(), toParse);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open() {
        user.getPlayer().openInventory(getInventory());
    }

    public interface Backable {
        void onBack();
    }
}
