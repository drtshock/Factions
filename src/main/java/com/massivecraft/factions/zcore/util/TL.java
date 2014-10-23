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
    CLAIM_ALLY("claim-ally", "<b>You can't claim the land of your allies."),
    CLAIM_ALREADY_OWN("claim-already-own", "%s<i> already own this land."),
    CLAIM_BORDER("claim-border", "<b>You must start claiming land at the border of the territory."),
    CLAIM_CONNECTED("claim-connected", "<b>You can only claim additional land which is connected to your first claim!"),
    CLAIM_CONNECTED_FACTION("claim-connected-faction", "<b>You can only claim additional land which is connected to your first claim or controlled by another faction!"),
    CLAIM_DISABLED("claim-disabled", "<b>Sorry, this world has land claiming disabled."),
    CLAIM_LIMIT("claim-limit", "<b>Limit reached. You can't claim more land!"),
    CLAIM_MODERATOR("claim-moderator", "<b>You must be <h>%s<b> to claim land."),
    CLAIM_NEED_MEMBERS("claim-need-members", "Factions must have at least <h>%s<b> members to claim land."),
    CLAIM_NEED_POWER("claim-need-power", "<b>You can't claim more land! You need more power!"),
    CLAIM_NOTIFY("claim-notify", "<h>%s<i> claimed land for <h>%s<i> from <h>%s<i>."),
    CLAIM_PEACEFUL("claim-peaceful", "%s<i> owns this land. Your faction is peaceful, so you cannot claim land from other factions."),
    CLAIM_PEACEFUL_TARGET("claim-peaceful-target", "%s<i> owns this land, and is a peaceful faction. You cannot claim land from them."),
    CLAIM_SAFE_ZONE("claim-safe-zone", "<b>You can not claim a Safe Zone."),
    CLAIM_STRONGER("claim-stronger", "%s<i> owns this land and is strong enough to keep it."),
    CLAIM_WAR_ZONE("claim-war-zone", "<b>You can not claim a War Zone."),
    CLAIM_WG_PROTECTED("claim-wg-protected", "<b>This land is protected"),
    CLAIM_WRONG_FACTION("claim-wrong-faction", "<b>You can't claim land for <h>%s<b>."),
    
    // Commands
    CMD_ADMIN_DEMOTE_OTHERMSG("cmd-admin-demote-othermsg", "<i>You have been demoted from the position of faction admin by %s<i>."),
    CMD_ADMIN_DEMOTE_SELFMSG("cmd-admin-demote-selfmsg", "<i>You have demoted %s<i> from the position of faction admin."),
    CMD_ADMIN_LEADERSHIP("cmd-admin-leadership", "%s<i> gave %s<i> the leadership of %s<i>."),
    CMD_ADMIN_NOT_ADMIN("cmd-admin-not-admin", "<b>You are not the faction admin."),
    CMD_ADMIN_NOT_MEMBER("cmd-admin-not-member", "%s<i> is not a member in your faction."),
    CMD_ADMIN_PROMOTE("cmd-admin-promote", "<i>You have promoted %s<i> to the position of faction admin."),
    CMD_ADMIN_TARGET_SELF("cmd-admin-target-self", "<b>The target player musn't be yourself."),
    
    CMD_AUTOCLAIM_DISABLED("cmd-autoclaim-disabled", "<i>Auto-claiming of land disabled."),
    CMD_AUTOCLAIM_RANK("cmd-autoclaim-rank", "<b>You must be <h>%s<b> to claim land."),
    CMD_AUTOCLAIM_WRONG_FACTION("cmd-autoclaim-wrong-factions", "<b>You can't claim land for <h>%s<b>."),
    CMD_AUTOCLAIM_START("cmd-autoclaim-start", "<i>Now auto-claiming land for <h>%s<i>."),
    
    CMD_BOOM_CONFIRM("cmd-boom-confirm", "%s<i> has %s explosions in your faction's territory."),
    CMD_BOOM_NOT_PEACEFUL("cmd-boom-not-peaceful", "<b>This command is only usable by factions which are specially designated as peaceful."),
    
    CMD_BYPASS_DISABLE("cmd-bypass-disable", "<i>You have disabled admin bypass mode."),
    CMD_BYPASS_ENABLE("cmd-bypass-enable", "<i>You have enabled admin bypass mode. You will be able to build or destroy anywhere."),
    
    CMD_CHAT_ALLIANCE("cmd-chat-alliance", "<i>Alliance only chat mode."),
    CMD_CHAT_DISABLED("cmd-chat-disabled", "<b>The built in chat channels are disabled on this server."),
    CMD_CHAT_FACTION("cmd-chat-faction", "<i>Faction only chat mode."),
    CMD_CHAT_PUBLIC("cmd-chat-public", "<i>Public chat mode."),
    CMD_CHAT_UNRECOGNIZED("cmd-chat-unrecognized", "<b>Unrecognised chat mode. <i>Please enter either 'a','f' or 'p'"),
    
    CMD_CHATSPY_DISABLED("cmd-chatspy-disabled", "<i>You have disabled chat spying mode."),
    CMD_CHATSPY_ENABLED("cmd-chatspy-enabled", "<i>You have enabled chat spying mode."),
    
    CMD_CLAIM_RADIUS_PERMISSION("cmd-claim-permission", "<b>You do not have permission to claim in a radius."),
    CMD_CLAIM_RADIUS_TOO_SMALL("cmd-claim-radius-too-small", "<b>If you specify a radius, it must be at least 1."),
    
    CMD_CREATE_CHANGE_DESCRIPTION("cmd-create-change-description", "<i>You should now: %s"),
    CMD_CREATE_INTERNAL_ERROR("cmd-create-internal-error", "<b>There was an internal error while trying to create your faction. Please try again."),
    CMD_CREATE_MUST_LEAVE("cmd-create-must-leave", "<b>You must leave your current faction first."),
    CMD_CREATE_NOTIFY("cmd-create-notify", "%s<i> created a new faction %s"),
    CMD_CREATE_TAG_IN_USE("cmd-create-tag-in-use", "<b>That tag is already in use."),
    
    CMD_DEINVITE_REVOKED_FACTION("cmd-deinvite-revoked-faction", "%s<i> revoked %s's<i> invitation."),
    CMD_DEINVITE_REVOKED_PLAYER("cmd-deinvite-revoked-player", "%s<i> revoked your invitation to <h>%s<i>."),
    
    CMD_DESCRIPTION_CHANGED("cmd-description-changed", "You have changed the description for <h>%s<i> to:"),
    CMD_DESCRIPTION_CHANGED_NOTIFY("cmd-description-changed-notify", "<i>The faction %s<i> changed their description to:"),
    
    CMD_DISBAND_BANK("cmd-disband-bank", "<i>You have been given the disbanded faction's bank, totaling %s."),
    CMD_DISBAND_NOT_NORMAL("cmd-disband-not-normal", "<i>You cannot disband the Wilderness, SafeZone, or WarZone."),
    CMD_DISBAND_OTHER("cmd-disband-other", "<h>%s<i> disbanded the faction %s."),
    CMD_DISBAND_PERMANENT("cmd-disband-permanent", "<i>This faction is designated as permanent, so you cannot disband it."),
    CMD_DISBAND_YOUR("cmd-disband-your", "<h>%s<i> disbanded your faction."),
    
    CMD_HOME_ASK_LEADER("cmd-home-ask-leader", "<i> Ask your leader to:"),
    CMD_HOME_DISABLED("cmd-home-disabled", "<b>Sorry, Faction homes are disabled on this server."),
    CMD_HOME_ENEMY_PROXIM("cmd-home-enemy-proxim", "<b>You cannot teleport to your faction home while an enemy is within %s blocks of you."),
    CMD_HOME_ENEMY_TERRITORY("cmd-home-enemy-territory", "<b>You cannot teleport to your faction home while in the territory of an enemy faction."),
    CMD_HOME_NOT_SET("cmd-home-not-set", "<b>Your faction does not have a home."),
    CMD_HOME_TELEPORT_DISABLED("cmd-home-teleport-disabled", "<b>Sorry, the ability to teleport to Faction homes is disabled on this server."),
    CMD_HOME_WRONG_WORLD("cmd-home-wrong-world", "<b>You cannot teleport to your faction home while in a different world."),
    CMD_HOME_YOU_SHOULD("cmd-home-you-should", "<i>You should:"),

    CMD_INVITE_ALREADY_MEMBER("cmd-deinvite-already-member", "%s<i> is already a member of %s"),
    CMD_INVITE_HAS("cmd-invite-has", " has invited you to join "),
    CMD_INVITE_INVITED("cmd-invite-invited", "%s<i> invited %s<i> to your faction."),
    CMD_INVITE_KICK("cmd-deinvite-kick", "<i>You might want to: %s"),
    CMD_INVITE_TOOLTIP("cmd-invite-tooltip", "Click to join!"),
    
    CMD_JOIN_ALREADY_MEMBER("cmd-join-already-member", "<b>%s %s already a member of %s"),
    CMD_JOIN_LIMIT("cmd-join-limit", " <b>!<white> The faction %s is at the limit of %d members, so %s cannot currently join."),
    CMD_JOIN_MOVED("cmd-join-moved", "<i>%s moved you into the faction %s."),
    CMD_JOIN_MUST_LEAVE("cmd-join-must-leave", "<b>%s must leave %s current faction first."),
    CMD_JOIN_NEG_POWER("cmd-join-neg-power", "<b>%s cannot join a faction with a negative power level."),
    CMD_JOIN_OTHERS_PERM("cmd-join-others-perm", "<b>You do not have permission to move other players into a faction."),
    CMD_JOIN_REQUIRES_INVITE("cmd-join-requires-invite", "<i>This faction requires invitation."),
    CMD_JOIN_SUCCESS("cmd-join-success", "<i>%s successfully joined %s."),
    CMD_JOIN_SYSTEM("cmd-join-system", "<b>Players may only join normal factions. This is a system faction."),
    CMD_JOIN_TRIED_NOTIFY("cmd-join-tried-notify", "%s<i> tried to join your faction."),
    CMD_JOIN_YOUR("cmd-join-your", "<i>%s joined your faction."),
    
    FACTION_ADMIN_REMOVED("faction-admin-removed", "&oFaction admin <h>%s&o has been removed. %s&o has been promoted as the new faction admin."),
    FACTION_CONFIRM_VALID_HOME("faction-confirm-valid-home", "&lYour faction home has been un-set since it is no longer in your territory."),
    FACTION_DISBANDED("faction-disbanded", "&oThe faction %s&o was disbanded."),

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
