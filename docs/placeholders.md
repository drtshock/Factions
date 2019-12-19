We hook into the plugin [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) to allow you to hook 
Factions info into other plugins.

## Internal Placeholders
Spots that you can use placeholders from other plugins in FactionsUUID text:

* `/f show`
* scoreboards
* name tag prefix

## External Placeholders
These are placeholders you can use in other plugins (or the places listed above) that will hook into FactionsUUID.

### Relations
The following can be used in plugins that support PlaceholderAPI relational placeholders.

Relational Placeholder | Description
--- | ---
%rel_factionsuuid_relation% | The relation between the 2 players
%rel_factionsuuid_relation_color% | Color of the relation between the 2 players

### Players
The rest of the placeholders should be usable by any plugin supporting placeholders.

Player Placeholder | Description
--- | ---
%factionsuuid_player_name% | The player's name
%factionsuuid_player_lastseen% | Last time the player was seen on the server or their online status
%factionsuuid_player_group% | Player's permission group
%factionsuuid_player_balance% | Player's money
%factionsuuid_player_power% | Player's power
%factionsuuid_player_maxpower% | Max power a player can have
%factionsuuid_player_kills% | Kills by this player
%factionsuuid_player_deaths% | Deaths by this player
%factionsuuid_player_role% | Player's faction role
%factionsuuid_player_role_name% | Player's faction role's name

### Factions

Faction Placeholder | Description
--- | ---
%factionsuuid_faction_name% | Faction's tag
%factionsuuid_faction_name_custom% | Custom faction tag based on lang.yml CUSTOM name
%factionsuuid_faction_only_space% | Literally a space, only if the player is in a faction
%factionsuuid_faction_power% | Faction's current power
%factionsuuid_faction_powermax% | Faction's max power
%factionsuuid_faction_dtr% | Faction's DTR
%factionsuuid_faction_dtrmax% | Faction's max DTR
%factionsuuid_faction_maxclaims% | Faction's max claims
%factionsuuid_faction_description% | Faction's long description
%factionsuuid_faction_claims% | Number of claimed chunks
%factionsuuid_faction_founded% | Date your faction was founded
%factionsuuid_faction_joining% | If your faction is allowing new members
%factionsuuid_faction_peaceful% | If your faction is peaceful
%factionsuuid_faction_powerboost% | Faction's current powerboost
%factionsuuid_faction_leader% | Name of the leader
%factionsuuid_faction_warps% | Number of warps
%factionsuuid_faction_raidable% | If HCF features are enabled, shows if your faction is raidable
%factionsuuid_faction_home_world% | World of your faction's home
%factionsuuid_faction_home_x% | X coordinate of your faction's home
%factionsuuid_faction_home_y% | Y coordinate of your faction's home
%factionsuuid_faction_home_z% | Z coordinate of your faction's home
%factionsuuid_faction_land_value% | Total value of your faction's land
%factionsuuid_faction_land_refund% | How much your faction would get if they refunded the land
%factionsuuid_faction_bank_balance% | Faction's bank balance
%factionsuuid_faction_allies% | Number of allies
%factionsuuid_faction_enemies% | Number of enemies
%factionsuuid_faction_truces% | Number of truces
%factionsuuid_faction_online% | Number of players online in your faction
%factionsuuid_faction_offline% | Number of players offline in your faction
%factionsuuid_faction_size% | Total online and offline faction members
%factionsuuid_faction_kills% | Total kills your faction has
%factionsuuid_faction_deaths% | Total deaths your faction has
%factionsuuid_faction_maxvaults% | Max vaults your faction can have
