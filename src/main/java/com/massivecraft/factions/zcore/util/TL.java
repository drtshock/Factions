/* 
 * Copyright (C) 2013 drtshock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.massivecraft.factions.zcore.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * An enum for requesting strings from the language file.
 */
public enum TL {

	// Misc
	A_SERVER_ADMIN("a-server-admin", "a server admin"),
	ARE("are", "are"),
    DISABLED("disabled", "disabled"),
    ENABLED("enabled", "enabled"),
    IS("is", "is"),
    THEIR("their", "their"),
    YOUR("your", "your"),
	
    // com.massivecraft.factions.FPlayer - claim errors
    CLAIM_ALLY("claim-ally", "&cYou can't claim the land of your allies."),
    CLAIM_ALREADY_OWN("claim-already-own", "{forfactionUC}&r&e already own this land."),
    CLAIM_BORDER("claim-border", "&cYou must start claiming land at the border of the territory."),
    CLAIM_CONNECTED("claim-connected", "&cYou can only claim additional land which is connected to your first claim!"),
    CLAIM_CONNECTED_FACTION("claim-connected-faction", "&cYou can only claim additional land which is connected to your first claim or controlled by another faction!"),
    CLAIM_DISABLED("claim-disabled", "&cSorry, this world has land claiming disabled."),
    CLAIM_LIMIT("claim-limit", "&cLimit reached. You can't claim more land!"),
    CLAIM_MODERATOR("claim-moderator", "&cYou must be &5{moderator}&r&c to claim land."),
    CLAIM_NEED_MEMBERS("claim-need-members", "Factions must have at least &5{minmembers}&r&c members to claim land."),
    CLAIM_NEED_POWER("claim-need-power", "&cYou can't claim more land! You need more power!"),
    CLAIM_NOTIFY("claim-notify", "&5{player}&r&e claimed land for &5{forfaction}&r&e from &5{curfaction}&e."),
    CLAIM_PEACEFUL("claim-peaceful", "{curfactionUC}&r&e owns this land. Your faction is peaceful, so you cannot claim land from other factions."),
    CLAIM_PEACEFUL_TARGET("claim-peaceful-target", "{curfactionUC}&r&e owns this land, and is a peaceful faction. You cannot claim land from them."),
    CLAIM_SAFE_ZONE("claim-safe-zone", "&cYou can not claim a Safe Zone."),
    CLAIM_STRONGER("claim-stronger", "{curfactionUC}&r&e owns this land and is strong enough to keep it."),
    CLAIM_WAR_ZONE("claim-war-zone", "&cYou can not claim a War Zone."),
    CLAIM_WG_PROTECTED("claim-wg-protected", "&cThis land is protected"),
    CLAIM_WRONG_FACTION("claim-wrong-faction", "&cYou can't claim land for &5{forfaction}&c."),
    
    // Commands
    CMD_ADMIN_DEMOTE_OTHERMSG("cmd-admin-demote-othermsg", "&eYou have been demoted from the position of faction admin by {admin}&e."),
    CMD_ADMIN_DEMOTE_SELFMSG("cmd-admin-demote-selfmsg", "&eYou have demoted {targetplayer}&r&e from the position of faction admin."),
    CMD_ADMIN_LEADERSHIP("cmd-admin-leadership", "{adminUC}&r&e gave {targetplayer}&r&e the leadership of {targetfaction}&e."),
    CMD_ADMIN_NOT_ADMIN("cmd-admin-not-admin", "&cYou are not the faction admin."),
    CMD_ADMIN_NOT_MEMBER("cmd-admin-not-member", "{targetplayer}&r&e is not a member in your faction."),
    CMD_ADMIN_PROMOTE("cmd-admin-promote", "&eYou have promoted {targetplayer}&r&e to the position of faction admin."),
    CMD_ADMIN_TARGET_SELF("cmd-admin-target-self", "&cThe target player musn't be yourself."),
    
    CMD_ANNOUNCE_PREFIX("cmd-announce-prefix", "&a{faction}&e [&7{user}&e] &r"),
    
    CMD_AUTOCLAIM_DISABLED("cmd-autoclaim-disabled", "&eAuto-claiming of land disabled."),
    CMD_AUTOCLAIM_RANK("cmd-autoclaim-rank", "&cYou must be &5{moderator}&c to claim land."),
    CMD_AUTOCLAIM_WRONG_FACTION("cmd-autoclaim-wrong-factions", "&cYou can't claim land for &5{forfaction}&c."),
    CMD_AUTOCLAIM_START("cmd-autoclaim-start", "&eNow auto-claiming land for &5{forfaction}&e."),
    
    CMD_BOOM_CONFIRM("cmd-boom-confirm", "{player}&e has {enabled} explosions in your faction's territory."),
    CMD_BOOM_NOT_PEACEFUL("cmd-boom-not-peaceful", "&cThis command is only usable by factions which are specially designated as peaceful."),
    
    CMD_BYPASS_DISABLE("cmd-bypass-disable", "&eYou have disabled admin bypass mode."),
    CMD_BYPASS_ENABLE("cmd-bypass-enable", "&eYou have enabled admin bypass mode. You will be able to build or destroy anywhere."),
    
    CMD_CHAT_ALLIANCE("cmd-chat-alliance", "&eAlliance only chat mode."),
    CMD_CHAT_DISABLED("cmd-chat-disabled", "&cThe built in chat channels are disabled on this server."),
    CMD_CHAT_FACTION("cmd-chat-faction", "&eFaction only chat mode."),
    CMD_CHAT_PUBLIC("cmd-chat-public", "&ePublic chat mode."),
    CMD_CHAT_UNRECOGNIZED("cmd-chat-unrecognized", "&cUnrecognised chat mode. &ePlease enter either 'a','f' or 'p'"),
    
    CMD_CHATSPY_DISABLED("cmd-chatspy-disabled", "&eYou have disabled chat spying mode."),
    CMD_CHATSPY_ENABLED("cmd-chatspy-enabled", "&eYou have enabled chat spying mode."),
    
    CMD_CLAIM_RADIUS_PERMISSION("cmd-claim-permission", "&cYou do not have permission to claim in a radius."),
    CMD_CLAIM_RADIUS_TOO_SMALL("cmd-claim-radius-too-small", "&cIf you specify a radius, it must be at least 1."),
    
    CMD_CONFIG_BOOLEAN_FALSE("cmd-config-boolean-false", "\"{field}\" option set to false (disabled)."),
    CMD_CONFIG_BOOLEAN_TRUE("cmd-config-boolean-true", "\"{field}\" option set to true (enabled)."),
    CMD_CONFIG_COLOR_ERROR("cmd-config-color-error", "Cannot set \"{field}\": \"{value}\" is not a valid color."),
    CMD_CONFIG_COLOR_SUCCESS("cmd-config-color-success", "\"{field}\" color option set to \"{value}\"."),
    CMD_CONFIG_DOUBLE_ERROR("cmd-config-double-error", "Cannot set \"{field}\": double (numeric) value required."),
    CMD_CONFIG_DUMMY("cmd-config-dummy", "Don't change this value"),
    CMD_CONFIG_ERROR("cmd-config-error", "Error setting configuration setting \"{field}\" to \"{value}\"."),
    CMD_CONFIG_FIELD_MISSING("cmd-config-field-missing", "Configuration setting \"{field}\" couldn't be matched, though it should be... please report this error."),
    CMD_CONFIG_FLOAT_ERROR("cmd-config-float-error", "Cannot set \"{field}\": float (numeric) value required."),
    CMD_CONFIG_INT_ERROR("cmd-config-int-error", "Cannot set \"{field}\": integer (whole number) value required."),
    CMD_CONFIG_LONG_ERROR("cmd-config-long-error", "Cannot set \"{field}\": long integer (whole number) value required."),
    CMD_CONFIG_MATERIAL_ADD("cmd-config-material-add", "\"{field}\" set: Material \"{value}\" added."),
    CMD_CONFIG_MATERIAL_ERROR("cmd-config-material-error", "Cannot change \"{field}\" set: \"{value}\" is not a valid material."),
    CMD_CONFIG_MATERIAL_REMOVE("cmd-config-material-remove", "\"{field}\" set: Material \"{value}\" removed."),
    CMD_CONFIG_SET_ADD("cmd-config-set-add", "\"{field}\" set: \"{value}\" added."),
    CMD_CONFIG_SET_ERROR("cmd-config-set-error", "\"{field}\" is not a data type set which can be modified with this command."),
    CMD_CONFIG_SET_REMOVE("cmd-config-set-remove", "\"{field}\" set: \"{value}\" removed."),
    CMD_CONFIG_SET_TYPE_ERROR("cmd-config-set-type-error", "\"{field}\" is not a data collection type which can be modified with this command."),
    CMD_CONFIG_TYPE_ERROR("cmd-config-type-error", "\"{field}\" is not a data type which can be modified with this command."),
    CMD_CONFIG_SUCCESS("cmd-config-success", "\"{field}\" option set to {value}."),
    CMD_CONFIG_INVALID_OPTION("cmd-config-invalid-option", "&cNo configuration setting \"&5{field}&c\" exists."),
    
    CMD_CREATE_CHANGE_DESCRIPTION("cmd-create-change-description", "&eYou should now: {createdesc}"),
    CMD_CREATE_INTERNAL_ERROR("cmd-create-internal-error", "&cThere was an internal error while trying to create your faction. Please try again."),
    CMD_CREATE_MUST_LEAVE("cmd-create-must-leave", "&cYou must leave your current faction first."),
    CMD_CREATE_NOTIFY("cmd-create-notify", "{player}&r&e created a new faction {faction}"),
    CMD_CREATE_TAG_IN_USE("cmd-create-tag-in-use", "&cThat tag is already in use."),
    
    CMD_DEINVITE_REVOKED_FACTION("cmd-deinvite-revoked-faction", "%s&r&e revoked %s&r&e's invitation."),
    CMD_DEINVITE_REVOKED_PLAYER("cmd-deinvite-revoked-player", "%s&r&e revoked your invitation to &5%s&e."),
    
    CMD_DESCRIPTION_CHANGED("cmd-description-changed", "You have changed the description for &5%s&r&e to:"),
    CMD_DESCRIPTION_CHANGED_NOTIFY("cmd-description-changed-notify", "&eThe faction %s&r&e changed their description to:"),
    
    CMD_DISBAND_BANK("cmd-disband-bank", "&eYou have been given the disbanded faction's bank, totaling %s."),
    CMD_DISBAND_NOT_NORMAL("cmd-disband-not-normal", "&eYou cannot disband the Wilderness, SafeZone, or WarZone."),
    CMD_DISBAND_OTHER("cmd-disband-other", "&5%s&r&e disbanded the faction %s."),
    CMD_DISBAND_PERMANENT("cmd-disband-permanent", "&eThis faction is designated as permanent, so you cannot disband it."),
    CMD_DISBAND_YOUR("cmd-disband-your", "&5%s&r&e disbanded your faction."),
    
    CMD_HOME_ASK_LEADER("cmd-home-ask-leader", "&e Ask your leader to:"),
    CMD_HOME_DISABLED("cmd-home-disabled", "&cSorry, Faction homes are disabled on this server."),
    CMD_HOME_ENEMY_PROXIM("cmd-home-enemy-proxim", "&cYou cannot teleport to your faction home while an enemy is within %s&r blocks of you."),
    CMD_HOME_ENEMY_TERRITORY("cmd-home-enemy-territory", "&cYou cannot teleport to your faction home while in the territory of an enemy faction."),
    CMD_HOME_NOT_SET("cmd-home-not-set", "&cYour faction does not have a home."),
    CMD_HOME_TELEPORT_DISABLED("cmd-home-teleport-disabled", "&cSorry, the ability to teleport to Faction homes is disabled on this server."),
    CMD_HOME_WRONG_WORLD("cmd-home-wrong-world", "&cYou cannot teleport to your faction home while in a different world."),
    CMD_HOME_YOU_SHOULD("cmd-home-you-should", "&eYou should:"),

    CMD_INVITE_ALREADY_MEMBER("cmd-deinvite-already-member", "%s&e is already a member of %s"),
    CMD_INVITE_HAS("cmd-invite-has", " has invited you to join "),
    CMD_INVITE_INVITED("cmd-invite-invited", "%s&r&e invited %s&r&e to your faction."),
    CMD_INVITE_KICK("cmd-deinvite-kick", "&eYou might want to: %s"),
    CMD_INVITE_TOOLTIP("cmd-invite-tooltip", "Click to join!"),
    
    CMD_JOIN_ALREADY_MEMBER("cmd-join-already-member", "&c%s&r %s&r already a member of %s"),
    CMD_JOIN_LIMIT("cmd-join-limit", " &c!&f The faction %s&r is at the limit of %d members, so %s&r cannot currently join."),
    CMD_JOIN_MOVED("cmd-join-moved", "&e%s moved you into the faction %s."),
    CMD_JOIN_MUST_LEAVE("cmd-join-must-leave", "&c%s must leave %s current faction first."),
    CMD_JOIN_NEG_POWER("cmd-join-neg-power", "&c%s cannot join a faction with a negative power level."),
    CMD_JOIN_OTHERS_PERM("cmd-join-others-perm", "&cYou do not have permission to move other players into a faction."),
    CMD_JOIN_REQUIRES_INVITE("cmd-join-requires-invite", "&eThis faction requires invitation."),
    CMD_JOIN_SUCCESS("cmd-join-success", "&e%s successfully joined %s."),
    CMD_JOIN_SYSTEM("cmd-join-system", "&cPlayers may only join normal factions. This is a system faction."),
    CMD_JOIN_TRIED_NOTIFY("cmd-join-tried-notify", "%s&e tried to join your faction."),
    CMD_JOIN_YOUR("cmd-join-your", "&e%s joined your faction."),
    
    FACTION_ADMIN_REMOVED("faction-admin-removed", "&oFaction admin &5%s&r&o has been removed. %s&r&o has been promoted as the new faction admin."),
    FACTION_CONFIRM_VALID_HOME("faction-confirm-valid-home", "&lYour faction home has been un-set since it is no longer in your territory."),
    FACTION_DISBANDED("faction-disbanded", "&oThe faction {faction}&o was disbanded."),

    PLAYER_CANNOT_LEAVE_POWER("player-cannot-leave-power", "&lYou cannot leave until your power is positive."),
    PLAYER_GIVE_ADMIN("player-give-admin", "&lYou must give the admin role to someone else first."),

    TITLE("title", "&bFactions &0|&r"),
    WILDERNESS("wilderness", "&2Wilderness"),
    WILDERNESS_DESCRIPTION("wilderness-description", ""),
    WARZONE("warzone", "&4Warzone"),
    WARZONE_DESCRIPTION("warzone-description", "Not the safest place to be."),
    SAFEZONE("safezone", "&6Safezone"),
    SAFEZONE_DESCRIPTION("safezone-description", "Free from pvp and monsters."),
    TOGGLE_SB("toggle-sb", "You now have scoreboards set to {value}"),
    DEFAULT_PREFIX("default-prefix", "{relationcolor}[{faction}] &r");

    private String path;
    private String def;
    private static YamlConfiguration LANG;

    /**
     * Lang enum constructor.
     *
     * @param path  The string path.
     * @param start The default string.
     */
    TL(String path, String start) {
        this.path = path;
        this.def = start;
    }

    /**
     * Set the {@code YamlConfiguration} to use.
     *
     * @param config The config to set.
     */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }

    @Override
    public String toString() {
        return this == TITLE ? ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)) + " " : ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }

    /**
     * Get the default value of the path.
     *
     * @return The default value of the path.
     */
    public String getDefault() {
        return this.def;
    }

    /**
     * Get the path to the string.
     *
     * @return The path to the string.
     */
    public String getPath() {
        return this.path;
    }
}
