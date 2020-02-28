# PlaceholderAPI

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

**Fun fact!** You can edit any placeholder below to say `faction_territory` instead and it will display
the information for the faction in which the player is presently standing. For example, 
`%factionsuuid_faction_territory_name%` will display the name of the faction that owns the chunk the player is
standing inside at the moment it is queried.

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
%factionsuuid_faction_allies_players% | Number of allied players
%factionsuuid_faction_allies_players_online% | Number of allied players online
%factionsuuid_faction_allies_players_offline% | Number of allied players offline
%factionsuuid_faction_enemies% | Number of enemies
%factionsuuid_faction_enemies_players% | Number of enemy players
%factionsuuid_faction_enemies_players_online% | Number of enemy players online
%factionsuuid_faction_enemies_players_offline% | Number of enemy players offline
%factionsuuid_faction_truces% | Number of truces
%factionsuuid_faction_truces_players% | Number of truced players
%factionsuuid_faction_truces_players_online% | Number of truced players online
%factionsuuid_faction_truces_players_offline% | Number of truced players offline 
%factionsuuid_faction_online% | Number of players online in your faction
%factionsuuid_faction_offline% | Number of players offline in your faction
%factionsuuid_faction_relation_color% | Relation color (more useful for territory mode)
%factionsuuid_faction_size% | Total online and offline faction members
%factionsuuid_faction_kills% | Total kills your faction has
%factionsuuid_faction_deaths% | Total deaths your faction has
%factionsuuid_faction_maxvaults% | Max vaults your faction can have
%factionsuuid_faction_dtr_frozen% | True or false (customizable in lang.yml) for DTR frozen state
%factionsuuid_faction_dtr_frozen_time% | Time remaining in frozen state (configure in main.conf) or blank if not frozen
%factionsuuid_faction_tnt_balance% | TNT bank balance
%factionsuuid_faction_tnt_max_balance% | Maximum TNT bank balance
