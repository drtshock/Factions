package com.massivecraft.factions.config;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.util.material.FactionMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Config {

    protected FileConfiguration configFile;
    protected ConfigurationSection section;

    public boolean usingLegacy = false;

    public void load() {
        configFile = P.p.getConfig();

        Node classNode = getClass().getAnnotation(Node.class);
        if (classNode != null) {
            section = P.p.getConfig().getConfigurationSection(classNode.path());
        } else {
            section = configFile;
        }

        for (Field field : getClass().getFields()) {
            if (field.isAnnotationPresent(Node.class)) {
                Node node = field.getAnnotation(Node.class);

                String path;
                if (node.path().isEmpty()) {
                    path = field.getName().replaceAll("(?=[A-Z][a-z])", "-").toLowerCase();
                } else {
                    path = node.path();
                }

                String legacy = node.legacy();
                if (classNode != null) {
                    legacy = classNode.legacy() + legacy;
                }

                Object defaultValue = null;
                try {
                    defaultValue = field.get(this);
                    if (section.isSet(path)) {
                        Object configValue = section.get(path, defaultValue);
                        // Special cases
                        if (field.getType() == Material.class) {
                            field.set(this, FactionMaterial.from((String) configValue).get());
                        } else if (field.getType() == List.class && field.getGenericType() == Material.class) {
                            List<Material> materials = new ArrayList<>();
                            for (String string : section.getStringList(path)) {
                                materials.add(FactionMaterial.from(string).get());
                            }
                            field.set(this, materials);
                        } else {
                            field.set(this, configValue);
                        }
                    } else if (!legacy.isEmpty()) {
                        usingLegacy = true;
                        Field legacyField = Conf.class.getField(legacy);
                        Object legacyValue = legacyField.get(null);

                        if (legacyValue != null) {
                            field.set(this, legacyValue);
                        }
                    } else {
                        field.set(this, defaultValue);
                    }
                } catch (IllegalAccessException | NoSuchFieldException | IllegalArgumentException e) {
                    try {
                        field.set(this, defaultValue);
                    } catch (IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public FileConfiguration getConfigFile() {
        return configFile;
    }

}
