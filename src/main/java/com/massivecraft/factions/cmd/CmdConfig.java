package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;

public class CmdConfig extends FCommand {

    private static HashMap<String, String> properFieldNames = new HashMap<String, String>();

    public CmdConfig() {
        super();
        this.aliases.add("config");

        this.requiredArgs.add("setting");
        this.requiredArgs.add("value");
        this.errorOnToManyArgs = false;

        this.permission = Permission.CONFIG.node;
        this.disableOnLock = true;

        senderMustBePlayer = false;
        senderMustBeMember = false;
        senderMustBeModerator = false;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        // store a lookup map of lowercase field names paired with proper capitalization field names
        // that way, if the person using this command messes up the capitalization, we can fix that
        if (properFieldNames.isEmpty()) {
            Field[] fields = Conf.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                properFieldNames.put(fields[i].getName().toLowerCase(), fields[i].getName());
            }
        }

        String field = this.argAsString(0).toLowerCase();
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }
        String fieldName = properFieldNames.get(field);

        if (fieldName == null || fieldName.isEmpty()) {
            values.put("field", field);
            TLmsg(TL.CMD_CONFIG_INVALID_OPTION, values);
            return;
        }

        TL success = TL.CMD_CONFIG_DUMMY;

        String value = args.get(1);
        for (int i = 2; i < args.size(); i++) {
            value += ' ' + args.get(i);
        }

        try {
            Field target = Conf.class.getField(fieldName);
            values.put("field", fieldName);

            // boolean
            if (target.getType() == boolean.class) {
                boolean targetValue = this.strAsBool(value);
                target.setBoolean(null, targetValue);

                if (targetValue) {
                    success = TL.CMD_CONFIG_BOOLEAN_TRUE;
                } else {
                    success = TL.CMD_CONFIG_BOOLEAN_FALSE;
                }
            }

            // int
            else if (target.getType() == int.class) {
                try {
                    int intVal = Integer.parseInt(value);
                    target.setInt(null, intVal);
                    values.put("value", String.valueOf(intVal));
                    success = TL.CMD_CONFIG_SUCCESS;
                } catch (NumberFormatException ex) {
                    TLmsg(TL.CMD_CONFIG_INT_ERROR, values);
                    return;
                }
            }

            // long
            else if (target.getType() == long.class) {
                try {
                    long longVal = Long.parseLong(value);
                    target.setLong(null, longVal);
                    values.put("value", String.valueOf(longVal));
                    success = TL.CMD_CONFIG_SUCCESS;
                } catch (NumberFormatException ex) {
                    TLmsg(TL.CMD_CONFIG_LONG_ERROR, values);
                    return;
                }
            }

            // double
            else if (target.getType() == double.class) {
                try {
                    double doubleVal = Double.parseDouble(value);
                    target.setDouble(null, doubleVal);
                    values.put("value", String.valueOf(doubleVal));
                    success = TL.CMD_CONFIG_SUCCESS;
                } catch (NumberFormatException ex) {
                    TLmsg(TL.CMD_CONFIG_DOUBLE_ERROR, values);
                    return;
                }
            }

            // float
            else if (target.getType() == float.class) {
                try {
                    float floatVal = Float.parseFloat(value);
                    target.setFloat(null, floatVal);
                    values.put("value", String.valueOf(floatVal));
                    success = TL.CMD_CONFIG_SUCCESS;
                } catch (NumberFormatException ex) {
                    TLmsg(TL.CMD_CONFIG_FLOAT_ERROR, values);
                    return;
                }
            }

            // String
            else if (target.getType() == String.class) {
                target.set(null, value);
                values.put("value", value);
                success = TL.CMD_CONFIG_SUCCESS;
            }

            // ChatColor
            else if (target.getType() == ChatColor.class) {
                values.put("value", value.toUpperCase());
                ChatColor newColor = null;
                try {
                    newColor = ChatColor.valueOf(value.toUpperCase());
                } catch (IllegalArgumentException ex) {

                }
                if (newColor == null) {
                    TLmsg(TL.CMD_CONFIG_COLOR_ERROR, values);
                    return;
                }
                target.set(null, newColor);
                success = TL.CMD_CONFIG_COLOR_SUCCESS;
            }

            // Set<?> or other parameterized collection
            else if (target.getGenericType() instanceof ParameterizedType) {
                ParameterizedType targSet = (ParameterizedType) target.getGenericType();
                Type innerType = targSet.getActualTypeArguments()[0];

                // not a Set, somehow, and that should be the only collection we're using in Conf.java
                if (targSet.getRawType() != Set.class) {
                    TLmsg(TL.CMD_CONFIG_SET_TYPE_ERROR, values);
                    return;
                }

                // Set<Material>
                else if (innerType == Material.class) {
                    values.put("value", value.toUpperCase());
                    Material newMat = null;
                    try {
                        newMat = Material.valueOf(value.toUpperCase());
                    } catch (IllegalArgumentException ex) {

                    }
                    if (newMat == null) {
                        TLmsg(TL.CMD_CONFIG_MATERIAL_ERROR, values);
                        return;
                    }

                    @SuppressWarnings("unchecked") Set<Material> matSet = (Set<Material>) target.get(null);

                    // Material already present, so remove it
                    if (matSet.contains(newMat)) {
                        matSet.remove(newMat);
                        target.set(null, matSet);
                        success = TL.CMD_CONFIG_MATERIAL_REMOVE;
                    }
                    // Material not present yet, add it
                    else {
                        matSet.add(newMat);
                        target.set(null, matSet);
                        success = TL.CMD_CONFIG_MATERIAL_ADD;
                    }
                }

                // Set<String>
                else if (innerType == String.class) {
                    values.put("value", value);
                    @SuppressWarnings("unchecked") Set<String> stringSet = (Set<String>) target.get(null);

                    // String already present, so remove it
                    if (stringSet.contains(value)) {
                        stringSet.remove(value);
                        target.set(null, stringSet);
                        success = TL.CMD_CONFIG_SET_REMOVE;
                    }
                    // String not present yet, add it
                    else {
                        stringSet.add(value);
                        target.set(null, stringSet);
                        success = TL.CMD_CONFIG_SET_ADD;
                    }
                }

                // Set of unknown type
                else {
                    TLmsg(TL.CMD_CONFIG_SET_ERROR, values);
                    return;
                }
            }

            // unknown type
            else {
                TLmsg(TL.CMD_CONFIG_TYPE_ERROR, values);
                return;
            }
        } catch (NoSuchFieldException ex) {
            TLmsg(TL.CMD_CONFIG_FIELD_MISSING, values);
            return;
        } catch (IllegalAccessException ex) {
            TLmsg(TL.CMD_CONFIG_ERROR, values);
            return;
        }

        if (!success.equals(TL.CMD_CONFIG_DUMMY)) {
            if (sender instanceof Player) {
                TLmsg(success, values);
                P.p.log(success + " Command was run by " + fme.getName() + ".");
            } else  // using P.p.log() instead of sendMessage if run from server console so that "[Factions v#.#.#]" is prepended in server log
            {
                P.p.log(P.p.txt.substitute(success, values));
            }
        }
        // save change to disk
        Conf.save();
    }

}
