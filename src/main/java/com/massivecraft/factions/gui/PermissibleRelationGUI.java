package com.massivecraft.factions.gui;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.perms.Permissible;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.util.material.FactionMaterial;
import org.bukkit.event.inventory.ClickType;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PermissibleRelationGUI extends GUI<Permissible> {
    private static Map<Permissible, SimpleItem> items;

    static {
        items = new LinkedHashMap<>();
        SimpleItem.Builder starter = SimpleItem.builder().setName("&8[{relation-color}{relation}&8]");

        SimpleItem recruit = starter.build();
        recruit.setName(Role.RECRUIT.getTranslation().toString());
        recruit.setMaterial(FactionMaterial.from("WOODEN_SWORD").get());
        items.put(Role.RECRUIT, recruit);

        SimpleItem normal = starter.build();
        normal.setName(Role.NORMAL.getTranslation().toString());
        normal.setMaterial(FactionMaterial.from("STONE_SWORD").get());
        items.put(Role.NORMAL, normal);

        SimpleItem moderator = starter.build();
        moderator.setName(Role.MODERATOR.getTranslation().toString());
        moderator.setMaterial(FactionMaterial.from("IRON_SWORD").get());
        items.put(Role.MODERATOR, moderator);

        SimpleItem coleader = starter.build();
        coleader.setName(Role.COLEADER.getTranslation().toString());
        coleader.setMaterial(FactionMaterial.from("DIAMOND_SWORD").get());
        items.put(Role.COLEADER, coleader);

        SimpleItem ally = starter.build();
        ally.setName(Relation.ALLY.getTranslation().toString());
        ally.setMaterial(FactionMaterial.from("GOLD_SWORD").get());
        items.put(Relation.ALLY, ally);

        SimpleItem truce = starter.build();
        truce.setName(Relation.TRUCE.getTranslation().toString());
        truce.setMaterial(FactionMaterial.from("IRON_AXE").get());
        items.put(Relation.TRUCE, truce);

        SimpleItem neutral = starter.build();
        neutral.setName(Relation.NEUTRAL.getTranslation().toString());
        neutral.setMaterial(FactionMaterial.from("STONE_HOE").get());
        items.put(Relation.NEUTRAL, neutral);

        SimpleItem enemy = starter.build();
        enemy.setName(Relation.ENEMY.getTranslation().toString());
        enemy.setMaterial(FactionMaterial.from("STONE_AXE").get());
        items.put(Relation.ENEMY, enemy);
    }


    private boolean online;

    public PermissibleRelationGUI(boolean online, FPlayer user) {
        super(user, 1);
        this.online = online;
        build();
    }

    @Override
    protected String getName() {
        return "Choose a relation:";
    }

    @Override
    protected String parse(String toParse, Permissible permissible) {
        // Uppercase the first letter
        String name = permissible.toString().substring(0, 1).toUpperCase() + permissible.toString().substring(1);

        toParse = toParse.replace("{relation-color}", permissible.getColor().toString());
        toParse = toParse.replace("{relation}", name);
        return toParse;
    }

    @Override
    protected void onClick(Permissible permissible, ClickType clickType) {
        new PermissibleActionGUI(online, user, permissible).open();
    }

    @Override
    protected Map<Integer, Permissible> createSlotMap() {
        Map<Integer, Permissible> map = new HashMap<>();
        map.put(0, Role.RECRUIT);
        map.put(1, Role.NORMAL);
        map.put(2, Role.MODERATOR);
        map.put(3, Role.COLEADER);
        map.put(5, Relation.ALLY);
        map.put(6, Relation.NEUTRAL);
        map.put(7, Relation.TRUCE);
        map.put(8, Relation.ENEMY);
        return map;
    }

    @Override
    protected SimpleItem getItem(Permissible permissible) {
        return items.get(permissible);
    }

    @Override
    protected Map<Integer, SimpleItem> createDummyItems() {
        return Collections.emptyMap();
    }
}
