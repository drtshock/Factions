package com.massivecraft.factions.config;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.config.annotation.ConfigFile;
import com.massivecraft.factions.config.annotation.Node;
import com.massivecraft.factions.util.material.FactionMaterial;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Singleton
public abstract class Config {

    protected File file;
    protected YamlConfiguration yamlConfig;
    protected ConfigurationSection section;

    private boolean usingLegacy = false;

    @Inject protected P p;

    public void load() {
        String fileName = getClass().getAnnotation(ConfigFile.class).value();

        file = new File(p.getDataFolder(), fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            p.saveResource(fileName, false);
        }

        try {
            yamlConfig = new YamlConfiguration();
            yamlConfig.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        Node classNode = getClass().getAnnotation(Node.class);
        if (classNode != null && !classNode.path().isEmpty()) {
            section = yamlConfig.getConfigurationSection(classNode.path());
        } else {
            section = yamlConfig;
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

                Object defaultValue = null;
                try {
                    defaultValue = field.get(this);
                    if (section.isSet(path)) {
                        Object configValue = section.get(path, defaultValue);
                        // Special cases TODO: Make this better for future use
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
                    } else if (!node.legacy().isEmpty() || (classNode != null && !classNode.legacy().isEmpty())) {
                        usingLegacy = true;
                        String legacy;
                        if (classNode == null) {
                            legacy = node.legacy();
                        } else {
                            legacy = classNode.legacy();
                            if (!classNode.legacy().isEmpty()) {
                                legacy = legacy + node.legacy();
                            } else {
                                // Lets uppercase the first letter if a legacy is not provided and the class has legacy
                                String name = field.getName();
                                legacy = legacy + name.substring(0,1).toUpperCase() + name.substring(1);
                            }
                        }

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

    public File getFile() {
        return file;
    }

    public YamlConfiguration getYamlConfig() {
        return yamlConfig;
    }

    public ConfigurationSection getSection() {
        return section;
    }

}
