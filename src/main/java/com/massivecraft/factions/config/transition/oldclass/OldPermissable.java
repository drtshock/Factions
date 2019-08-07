package com.massivecraft.factions.config.transition.oldclass;

import com.massivecraft.factions.perms.Permissible;

public interface OldPermissable {
    String name();

    Permissible newPermissible();
}
