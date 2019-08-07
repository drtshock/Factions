package com.massivecraft.factions.config.file;

import com.massivecraft.factions.config.annotation.Comment;

public class DefaultOfflinePermissionsConfig {
    @Comment("Offline permissions settings\n" +
            "If you don't enable offline permissions in the main config and never plan to do so,\n" +
            "  you can ignore this file entirely. If you plan to do so in the future, you should probably\n" +
            "  set this file up a bit as these defaults will sit in the faction configs regardless.\n" +
            "\n" +
            "Each main section represents one permission.\n" +
            "Inside is each relation.\n" +
            "Each relation has a default value (true=allowed, false=disallowed)\n" +
            "  and true/false for if it's locked to editing by factions admins.")
    private DefaultPermissionsConfig.Permissions offlinePermissions = new DefaultPermissionsConfig.Permissions();

    public DefaultPermissionsConfig.Permissions getPermissions() {
        return this.offlinePermissions;
    }
}