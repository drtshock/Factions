# Commands

!!! warning "This page is presently under construction, and is incomplete"

Click on any command for more details.

!!! note
    Some commands take additional arguments.  
    Arguments like &lt;this&gt; are *required*.  
    Arguments like [this] are *optional*.

### Territory

??? abstract  "/f claim [radius] [faction]"
    Defaults: radius = 1, faction = yours  
    Claims one or more chunks for the given faction.  
    Can only claim if the land is not claimed by another faction, or if the other faction has more land than power (and is not an ally).
    
    !!! success "Requirements"
        Land cannot be owned by another faction (unless using power, the owning faction has more land than power, and the owning faction is not an ally)  
        `factions.claim` node.  
        `factions.claim.radius` node to claim a radius greater than 1.  
        Must be faction admin, or be granted `territory` perms in the faction.  
        To claim for safezone, must have `factions.managesafezone`.  
        To claim for warzone, must have `factions.managewarzone`.  

??? abstract "/f unclaim [radius] [faction]"
    Defaults: radius = 1, faction = yours  
    Returns one or more chunks of faction territory to the wilderness.  
    
    !!! success "Requirements"
        `factions.unclaim` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  
        To unclaim for safezone, must have `factions.managesafezone`.  
        To unclaim for warzone, must have `factions.managewarzone`.  

??? abstract "/f autoclaim [faction]"
    Defaults: faction = yours
    Turns autoclaiming on or off. If on, any chunk you enter that you *can* claim, *will* be claimed up until
    you reach your faction's limit.
    
    !!! success "Requirements"
        `factions.autoclaim` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  
        To claim for safezone, must have `factions.managesafezone`.  
        To claim for warzone, must have `factions.managewarzone`.

??? abstract "/f claimat &lt;world&gt; &lt;x&gt; &lt;z&gt;"
    Attempts to claim a chunk in the given world at the given coordinates.  
    The x and z values are *chunk coordinates*, not normal block coordinates.  
    You can see chunk coordinates from `/f map`, for example.
    
    !!! success "Requirements"
        `factions.claimat` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  

??? abstract "/f claimfill [amount] [faction]"
    Defaults: amount = config limit, faction = yours
    Attempts to claim a number of blocks filling in an established shape of claims.  
    
    !!! success "Requirements"
        `factions.claim.fill` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  
        To claim for safezone, must have `factions.managesafezone`.  
        To claim for warzone, must have `factions.managewarzone`.

??? abstract "/f claimline [amount] [direction] [faction]"
    Defaults: amount = 1, direction = facing, faction = yours
    Attempts to claim a number of blocks in a line based on the direction given (or facing direction, if not specified).  
    Acceptable directions: north, south, east, west.
    
    !!! success "Requirements"
        `factions.claim.line` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  
        To claim for safezone, must have `factions.managesafezone`.  
        To claim for warzone, must have `factions.managewarzone`.

??? abstract "/f unclaimall"
    The nuclear option. Removes all claims in your faction, returning them to the wilderness.
    
    !!! success "Requirements"
        `factions.unclaimall` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  

??? abstract "/f safeunclaimall [world]"
    Defaults: world = all  
    Removes all safezone claims in a given world, or in all worlds.
    
    !!! success "Requirements"
        `factions.managesafezone` node.

??? abstract "/f warunclaimall [world]"
    Defaults: world = all  
    Removes all warzone claims in a given world, or in all worlds.
    
    !!! success "Requirements"
        `factions.managewarzone` node.

### Faction Management

??? abstract "/f admin &lt;player&gt;"
    Sets the new leader of your faction.
    
    A server administrator can make any targeted player the leader of the faction that player is in.
    
    !!! success "Requirements"
        `factions.admin` node.  
        Must be admin of your faction to use.  
        `factions.admin.any` node to change status of any player.

??? abstract "/f announce &lt;message...&gt;"
    Creates an announcement sent to all faction members.  
    Also saves the message for all current members who are offline, sending to them upon login.
    
    !!! success "Requirements"
        `factions.announce` node.  
        Must be moderator of your faction or higher to use.  

??? abstract "/f ban &lt;target&gt;"
    Bans a player from the faction.  
    
    !!! success "Requirements"
        `factions.ban` node.  
        Must be faction admin, or be granted `ban` perms in the faction.  

### Old command documentation

This documentation will stay here until all commands are documented in the above manner:

Command | Permission | Meaning
--- | --- | ---
/f admin <target> | factions.admin | Set new player as leader of your faction.
`/f ahome <target>` | factions.ahome | Teleport a player to their faction's home.
`/f announce <message…>` | factions.announce | Send a message to players in your faction.
`/f autoclaim [faction]` | factions.autoclaim | Auto-claim land when you walk over wilderness for your faction. Admins can specify other factions and claim for them.
`/f autohelp` | none | Show help for all commands.
`/f boom [on/off]` | factions.noboom | Toggle peaceful explosions in your faction's territory on or off.
`/f bypass` | factions.bypass | Set yourself to bypass faction permission checks.
`/f chat <mode>` | factions.chat | Toggle chat modes or specify which channel you want to be in (public, alliance, faction, truce)
`/f chatspy` | factions.chatspy | Enable spying on all private chat channels.
`/f claim [radius] [faction]` | factions.claim | Claim land for your faction given an optional radius. Admins can specify other factions and claim for them.
`/f claimline <chunks> [direction] [faction]` | factions.claimline | Claim land in a line for your faction given an optional length and direction. Admins can specify other factions and claim for them.
`/f config <setting> <value>` | factions.config | Set configuration values in the conf.json. 
`/f convert <backend>` | console only | Convert from your current backend to a new backend (JSON). New backends will be added in the future (MYSQL).
`/f create <name>` | factions.create | Create a faction with the given name.
`/f defaultrole <role>` | factions.defaultrank | Set the default rank new members get when joining your faction.
`/f deinvite [name]` | factions.deinvite | Revoke an invite from a player. If no player is defined, it will list all players with pending invites. Click the names to revoke their invite.
`/f delwarp <name>` | factions.setwarp | Delete a warp
`/f demote <name>` | factions.promote | Demote a player by one rank.
`/f desc <description…>` | factions.description | Set your faction's new description.
`/f disband` | factions.disband | Disband your faction.
`/f fly` | factions.fly | Fly in your faction's territory. Disabled in combat.
`/f help <page>` | factions.help | List help pages for things.
`/f home` | factions.home | Go to your faction's home.
`/f invite <target>` | factions.invite | Invite a player to your faction.
`/f join <faction>` | factions.join | Join a defined faction.
`/f kick <target>` | factions.kick | Kick a player from your faction.
`/f leave` | factions.leave | Leave your faction.
`/f list` | factions.list | List top Factions by players.
`/f lock` | factions.lock | Lock datafiles from being overwritten. Will make anything on the server not get saved.
`/f logins` | factions.monitorlogins | Toggle monitoring of logins for your faction.
`/f map [on/off]` | factions.map | View the faction map of the area around you.
`/f mapheight [value]` | factions.mapheight | Set how many lines your /f map will show.
`/f mod [name]` | factions.mod | Promote a player in your faction to mod.
`/f modifypower <name> <power>` | factions.modifypower | Modify a player's power. The <power> variable adds power to the player's current power.
`/f money` | none | Shows help for money commands.
`/f money balance [faction]` | factions.money.balance | Check a faction's balance. Default is your own faction.
`/f money deposit <amount> [faction]` | factions.money.deposit | Deposit money into your faction. Admins can specify other factions and can add money to the specified faction.
`/f money ff <amount> <factionfrom> <factionto>` | factions.money.f2f | Transfer money from one faction to another.
`/f money fp <amount> <factionfrom> <playerto>` | factions.money.f2p | Transfer money from one faction to a player.
`/f money pf <amount> <playerfrom> <factionto>` | factions.money.pf2 | Transfer money from one player to a faction.
`/f money withdraw <amount> [faction]` | factions.money.withdraw | Withdraw money from your faction. Admins can specify any faction and take away money from the faction.
`/f open` | factions.open | Toggle allowing anyone being able to join the faction.
`/f owner [name]` | factions.owner | Set claim ownership for this chunk. Admins can specify a target player.
`/f ownerlist` | factions.ownerlist | Get the current owner of the chunk you're in if it's in your faction.
`/f peaceful <faction>` | factions.setpeaceful | Set a faction to being peaceful.
`/f perm [relation] [action] [access]` | factions.permissions | Manage permissions for your Faction. Example /f perm recruit build deny.
`/f permanent <faction>` | factions.setpermanent | Set a faction to permanent status. This will make the faction stay if there are zero members.
`/f permanentpower <faction> [power]` | factions.setpermanentpower | Set permanent power to a faction.
`/f power <player>` | factions.power | Check power of a player. Default is yourself.
`/f powerboost <player/faction> <name> <number>` | factions.powerboost | Set powerboost of a player or faction. <player/faction> can be 'f' or 'p' to let the plugin know if you're specifying a player or faction.
`/f promote <name>` | factions.promote | Promote a player in your faction by one rank. Will not let you promote them to leader.
`/f rel <relation> <faction>` | factions.relation | Request to change your faction's relationship with a target faction. Relations can be ally, truce, neutral, enemy.
`/f reload` | factions.reload | Reload configurations (lang.yml, config.yml, conf.json). This does not reload factions saved data from disk.
`/f safeunclaimall [world]` | factions.managesafezone | Safely unclaim all territories in your world. Can specify another world.
`/f saveall` | factions.save | Force save all factions data to disk.
`/f sb` | factions.scoreboard | Toggle the factions scoreboard on or off.
`/f sc` | factions.seechunk | See outlines around the border of the chunk you're standing in. No one else can see the outlines.
`/f defaultrole <role>` | factions.defaultrank | Set the default role that new members will get when they join your faction.
`/f setwarp <name> [password]` | factions.setwarp | Set a warp with an optional password to your location. More information at [Password Protected Warps](https://github.com/drtshock/Factions/wiki/Password-Protected-Warps)
`/f sethome` | factions.sethome | Set your faction's home.
`/f setmaxvaults <faction> <number>` | factions.setmaxvaults | Set the max vaults a faction can have.
`/f show [faction]` | factions.show | Show info about a Faction. Default is yours.
`/f showinvites` | factions.showinvites | Show pending invites for your faction.
`/f status` | factions.status | Show status of all players in your faction.
`/f stuck` | factions.stuck | Attempts to teleport you to the nearest wilderness chunk.
`/f tag <tag>` | factions.tag | Change your faction's tag.
`/f title <player> [title]` | factions.title | Set a player's custom title. Will charge them if enabled.
`/f togglealliancechat` | factions.togglealliancechat | Toggle ignoring alliance chat.
`/f top <criteria> [page]` | factions.top | List top factions by criteria (members, start, power, land, online, money). 
`/f unclaim [radius] [faction]` | factions.unclaim | Unclaim for your faction. Optionally define the radius. Admins can specify other factions and unclaim their land.
`/f unclaimall` | factions.unclaimall | Unclaim all of your faction's land.
`/f vault [number]` | factions.vault | Open your faction's vault. If no vault is defined, it will list available vaults.
`/f version` | factions.version | Show the version string for FactionsUUID.
`/f warp [warp] [password]` | factions.warp | To go a warp, password optional. Opens GUI if no warp defined.
`/f warunclaimall` | factions.managewarzone | Unclaim all warzone claims.
`/f near` | factions.near | Show nearby faction members
`/f tnt` | factions.tnt.info | Show faction TNT bank
`/f tnt info` | factions.tnt.info | Show faction TNT bank
`/f tnt deposit <amount>` | factions.tnt.deposit | Deposit into the faction TNT bank
`/f tnt withdraw <amount>` | factions.tnt.withdraw | Withdraw from the faction TNT bank
`/f tnt fill <radius> <amount>` | factions.tnt.fill | Fill nearby dispensers with TNT from faction TNT bank
`/f tnt siphon <radius> [amount]` | factions.tnt.siphon | Siphon TNT from nearby dispensers into the faction TNT bank
