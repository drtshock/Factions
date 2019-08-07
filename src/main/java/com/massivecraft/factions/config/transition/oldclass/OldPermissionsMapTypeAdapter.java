package com.massivecraft.factions.config.transition.oldclass;

import com.google.gson.*;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.util.TL;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class OldPermissionsMapTypeAdapter implements JsonDeserializer<Map<OldPermissable, Map<OldPermissableAction, Access>>> {

    @Override
    public Map<OldPermissable, Map<OldPermissableAction, Access>> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

        try {
            JsonObject obj = json.getAsJsonObject();
            if (obj == null) {
                return null;
            }

            Map<OldPermissable, Map<OldPermissableAction, Access>> permissionsMap = new ConcurrentHashMap<>();

            // Top level is Relation
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                OldPermissable permissable = getPermissable(entry.getKey());

                if (permissable == null) {
                    continue;
                }

                // Second level is the map between action -> access
                Map<OldPermissableAction, Access> accessMap = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {
                    OldPermissableAction permissableAction = OldPermissableAction.fromString(entry2.getKey());
                    if (permissableAction == null) {
                        switch (entry2.getKey()) {
                            case "frostwalk":
                                permissableAction = OldPermissableAction.FROST_WALK;
                                break;
                            case "painbuild":
                                permissableAction = OldPermissableAction.PAIN_BUILD;
                                break;
                            case "items":
                                permissableAction = OldPermissableAction.ITEM;
                                break;
                        }
                    }
                    Access access = Access.fromString(entry2.getValue().getAsString());
                    accessMap.put(permissableAction, access);
                }
                permissionsMap.put(permissable, accessMap);
            }

            return permissionsMap;

        } catch (Exception ex) {
            P.p.log(Level.WARNING, "Error encountered while deserializing a PermissionsMap.");
            ex.printStackTrace();
            return null;
        }
    }

    private OldPermissable getPermissable(String name) {
        // If name is uppercase then it is (probably, no way to completely know) valid if not begin conversion
        if (name.equals(name.toUpperCase())) {
            if (OldRole.fromString(name.toUpperCase()) != null) {
                return OldRole.fromString(name.toUpperCase());
            } else if (OldRelation.fromString(name.toUpperCase()) != null) {
                return OldRelation.fromString(name.toUpperCase());
            } else {
                return null;
            }
        } else {
            if (name.equals(TL.ROLE_RECRUIT.toString())) {
                return OldRole.RECRUIT;
            } else if (name.equals(TL.ROLE_NORMAL.toString())) {
                return OldRole.NORMAL;
            } else if (name.equals(TL.ROLE_MODERATOR.toString())) {
                return OldRole.MODERATOR;
            } else {
                // If it is explicitly member and its old data then it refers to relation member not role, skip it
                if (name.equals("member")) {
                    return null;
                }
                return OldRelation.fromString(name);
            }
        }
    }

}
