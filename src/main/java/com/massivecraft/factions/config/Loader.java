package com.massivecraft.factions.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.massivecraft.factions.P;
import com.massivecraft.factions.config.annotation.Comment;
import com.massivecraft.factions.config.annotation.ConfigName;
import com.typesafe.config.ConfigRenderOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

public class Loader {
    public static void load(String file, Object config, String comment) throws IOException, IllegalAccessException {
        Path configFolder = P.p.getDataFolder().toPath().resolve("config");
        if (!configFolder.toFile().exists()) {
            configFolder.toFile().mkdir();
        }
        Path path = configFolder.resolve(file + ".conf");
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setPath(path)
                .setRenderOptions(ConfigRenderOptions.defaults().setComments(true).setOriginComments(false).setJson(false))
                .build();
        CommentedConfigurationNode node = loader.load();

        loadNode(node, config);
        node.setComment(comment);

        loader.save(node);
    }

    private static Set<Class<?>> types = new HashSet<>();

    static {
        types.add(Boolean.TYPE);
        types.add(Byte.TYPE);
        types.add(Character.TYPE);
        types.add(Double.TYPE);
        types.add(Float.TYPE);
        types.add(Integer.TYPE);
        types.add(Long.TYPE);
        types.add(Short.TYPE);
        types.add(List.class);
        types.add(Set.class);
        types.add(String.class);
    }

    private static void loadNode(CommentedConfigurationNode current, Object object) throws IllegalAccessException {
        for (Field field : getFields(object.getClass())) {
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0 || field.isSynthetic()) {
                continue;
            }
            field.setAccessible(true);
            ConfigName configName = field.getAnnotation(ConfigName.class);
            Comment comment = field.getAnnotation(Comment.class);
            String confName = configName == null || configName.value().isEmpty() ? field.getName() : configName.value();
            CommentedConfigurationNode node = current.getNode(confName);
            if (types.contains(field.getType())) {
                //System.out.println("Found " + getNodeName(node.getPath()) + " " + field.get(object));
                if (node.isVirtual()) {
                    if (comment != null && !node.getComment().isPresent()) {
                        node.setComment(comment.value());
                    }
                    node.setValue(field.get(object));
                } else {
                    try {
                        if (node.getValue()!=null && Set.class.isAssignableFrom(field.getType()) && List.class.isAssignableFrom(node.getValue().getClass())) {
                            field.set(object, new HashSet((List<?>)node.getValue()));
                        } else {
                            field.set(object, node.getValue());
                        }
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Found incorrect type for " + getNodeName(node.getPath()) + ": Expected " + field.getType() + ", found " + node.getValue().getClass());
                    }
                }
            } else {
                Object o = field.get(object);
                if (o == null) {
                    node.setValue(null);
                } else {
                    loadNode(node, o);
                }
            }
        }
    }

    private static List<Field> getFields(Class<?> clazz) {
        return getFields(new ArrayList<>(), clazz);
    }

    private static List<Field> getFields(List<Field> fields, Class<?> clazz) {
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null) {
            getFields(fields, clazz.getSuperclass());
        }

        return fields;
    }

    private static String getNodeName(Object[] path) {
        StringBuilder builder = new StringBuilder();
        for (Object o : path) {
            if (o != null) {
                builder.append(o.toString()).append('.');
            }
        }
        return builder.substring(0, builder.length() - 1);
    }
}
