package com.massivecraft.factions.config;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.util.material.FactionMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Singleton
public abstract class Config {

    protected FileConfiguration configFile;
    protected ConfigurationSection section;

    private boolean usingLegacy = false;

    @Inject protected P p;

    public void load() {
        configFile = p.getConfig();

        Node classNode = getClass().getAnnotation(Node.class);
        if (classNode != null) {
            section = p.getConfig().getConfigurationSection(classNode.path());
        } else {
            section = configFile;
        }

        for (Field field : getClass().getFields()) {
            if (field.isAnnotationPresent(Node.class)) {
                if (Config.class.isAssignableFrom(field.getType())) {
                    try {
                        Config group = (Config) field.get(this);
                        group.load();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

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

        if (usingLegacy) {
            p.getLogger().severe("conf.json is still in use, please port your config to respective files");
        }
    }

    public ConfigurationSection getSection() {
        return section;
    }

    public FileConfiguration getConfigFile() {
        return configFile;
    }

}
