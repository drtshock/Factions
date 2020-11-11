# Commands

Click on any command for more details.

!!! note
    Some commands take additional arguments.  
    Arguments like &lt;this&gt; are *required*.  
    Arguments like [this] are *optional*.

### General Commands

??? abstract  "/f autohelp"
    Show help for all commands.
    
    !!! success "Requirements"
        None.

??? abstract  "/f coords"
    Send faction members your current position
    
    !!! success "Requirements"
        `factions.coords` node.

??? abstract  "/f fly"
    Fly in your faction's territory. Disabled in combat.
    
    !!! success "Requirements"
        `factions.fly` node.

??? abstract  "/f join &lt;faction&gt;"
    Join a defined faction.
    
    !!! success "Requirements"
        `factions.join`

??? abstract  "/f leave"
    Leave your faction.
    
    !!! success "Requirements"
        `factions.leave`

??? abstract  "/f logins"
    Toggle monitoring of logins for your faction.
    
    !!! success "Requirements"
        `factions.monitorlogins`

??? abstract  "/f near"
    Shows nearby faction members
    
    !!! success "Requirements"
        `factions.near`

??? abstract "/f sb"
    Toggle the factions scoreboard on or off.

    !!! success "Requirements"
        `factions.scoreboard` node.

??? abstract  "/f stuck"
    Attempts to teleport you to the nearest wilderness chunk.
    
    !!! success "Requirements"
        `factions.stuck`

??? abstract "/f vault [number]"
    Opens your faction's vault. If no vault is defined, it will list available vaults.

    !!! success "Requirements"
        `factions.vault` node.

??? abstract  "/f version"
    Shows the version string for FactionsUUID.
    
    !!! success "Requirements"
        `factions.version`

??? abstract  "/f help &lt;page&gt;"
    List help pages for things.
    
    !!! success "Requirements"
        `factions.help`

### Info commands

??? abstract  "/f list"
    List top Factions by players.
    
    !!! success "Requirements"
        `factions.list`

??? abstract  "/f map [on/off]"
    View the faction map of the area around you.
    
    !!! success "Requirements"
        `factions.map`

??? abstract  "/f mapheight [value]"
    Set how many lines your /f map will show.
    
    !!! success "Requirements"
        `factions.mapheight`

??? abstract "/f power &lt;player&gt;"
    Check power of a player. Default is yourself.

    !!! success "Requirements"
        `factions.power` node.

??? abstract "/f sc"
    See outlines around the border of the chunk you're standing in. No one else can see the outlines.

    !!! success "Requirements"
        `factions.seechunk` node.

??? abstract "/f show [faction]"
    Shows info about a Faction. Default is yours. 

    !!! success "Requirements"
        `factions.show` node.

??? abstract  "/f status"
    Shows status of all players in your faction.
    
    !!! success "Requirements"
        `factions.status`

??? abstract "/f top &lt;criteria&gt; [page]"
    List top factions by criteria (members, start, power, land, online, money). 

    !!! success "Requirements"
        `factions.top` node.

??? abstract "/f ownerlist"
    Get the current owner of the chunk you're in if it's in your faction.

    !!! success "Requirements"
        `factions.ownerlist` node.

### Money

??? abstract  "/f money"
    Shows help for money commands.
    
    !!! success "Requirements"
        None.

??? abstract  "/f money balance"
    Check a faction's balance. Default is your own faction.
    
    !!! success "Requirements"
        `factions.money.balance` node.

??? abstract  "/f money deposit &lt;amount&gt; [faction]"
    Deposit money into your faction. Admins can specify other factions and can add money to the specified faction.
    
    !!! success "Requirements"
        `factions.money.deposit` node.

??? abstract  "/f money ff &lt;amount&gt; &lt;factionfrom&gt; &lt;factionto&gt;"
    Transfer money from one faction to another.
    
    !!! success "Requirements"
        `factions.money.f2f` node.

??? abstract  "/f money fp &lt;amount&gt; &lt;factionfrom&gt; &lt;playerto&gt;"
    Transfer money from one faction to a player.
    
    !!! success "Requirements"
        `factions.money.f2f` node.

??? abstract  "/f money pf &lt;amount&gt; &lt;playerfrom&gt; &lt;factionto&gt;"
    Transfer money from one player to a faction.
    
    !!! success "Requirements"
        `factions.money.f2f` node.

??? abstract  "/f money withdraw &lt;amount&gt; [faction]"
    Withdraw money from your faction. Admins can specify any faction and take away money from the faction.
    
    !!! success "Requirements"
        `factions.money.withdraw` node.

### Chat

??? abstract  "/f chat &lt;mode&gt;"
    Toggles chat modes or specify which channel you want to be in (public, alliance, faction, truce)
    
    !!! success "Requirements"
        `factions.chat` node.

??? abstract  "/f chatspy"
    Enable spying on all private chat channels.
    
    !!! success "Requirements"
        `factions.chatspy` node.

??? abstract  "/f togglealliancechat"
    Toggle ignoring alliance chat.
    
    !!! success "Requirements"
        `factions.togglealliancechat` node.

### Territory

??? abstract "/f autoclaim [faction]"
    Defaults: faction = yours
    Turns autoclaiming on or off. If on, any chunk you enter that you *can* claim, *will* be claimed up until
    you reach your faction's limit.
    
    !!! success "Requirements"
        `factions.autoclaim` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  
        To claim for safezone, must have `factions.managesafezone`.  
        To claim for warzone, must have `factions.managewarzone`.

??? abstract "/f boom [on/off]"
    Toggle peaceful explosions in your faction's territory on or off.
    
    !!! success "Requirements"
        `factions.noboom` node.  
        Must be faction moderator or higher.

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

??? abstract "/f listclaims [world] [faction]"
    Defaults: world = current, faction = current  
    Lists all coordinates of faction claims, merging attached claims into one coordinate with number of claims in parentheses.
    
    !!! success "Requirements"
        `factions.listclaims` node.  
        Must be faction admin, or be granted `listclaims` perms in the faction.  
        To view other factions, must have`factions.listclaims.other`.

??? abstract "/f safeunclaimall [world]"
    Defaults: world = all  
    Removes all safezone claims in a given world, or in all worlds.
    
    !!! success "Requirements"
        `factions.managesafezone` node.

??? abstract "/f unclaim [radius] [faction]"
    Defaults: radius = 1, faction = yours  
    Returns one or more chunks of faction territory to the wilderness.  
    
    !!! success "Requirements"
        `factions.unclaim` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  
        To unclaim for safezone, must have `factions.managesafezone`.  
        To unclaim for warzone, must have `factions.managewarzone`.  

??? abstract "/f unclaimall"
    The nuclear option. Removes all claims in your faction, returning them to the wilderness.
    
    !!! success "Requirements"
        `factions.unclaimall` node.  
        Must be faction admin, or be granted `territory` perms in the faction.  

??? abstract "/f warunclaimall [world]"
    Defaults: world = all  
    Removes all warzone claims in a given world, or in all worlds.
    
    !!! success "Requirements"
        `factions.managewarzone` node.

### TNT Bank

??? abstract "/f tnt"
    Shows faction TNT bank.
    
    !!! success "Requirements"
        `factions.tnt.info` node.

??? abstract "/f tnt deposit &lt;amount&gt;"
    Deposit into the faction TNT bank
    
    !!! success "Requirements"
        `factions.tnt.deposit` node.

??? abstract "/f tnt fill &lt;radius&gt; &lt;amount&gt;"
    Fill nearby dispensers with TNT from faction TNT bank
    
    !!! success "Requirements"
        `factions.tnt.fill` node.

??? abstract "/f tnt info"
    Shows faction TNT bank.
    
    !!! success "Requirements"
        `factions.tnt.info` node.

??? abstract "/f tnt siphon &lt;radius&gt; [amount]"
    Siphon TNT from nearby dispensers into the faction TNT bank
    
    !!! success "Requirements"
        `factions.tnt.siphon` node.

??? abstract "/f tnt withdraw &lt;amount&gt;"
    Withdraw from the faction TNT bank
    
    !!! success "Requirements"
        `factions.tnt.withdraw` node.

### Faction Management
??? abstract "/f admin &lt;player&gt;"
    Sets the new leader of your faction.
    
    A server administrator can make any targeted player the leader of the faction that player is in.
    
    !!! success "Requirements"
        `factions.admin` node.  
        Must be admin of your faction to use.  
        `factions.admin.any` node to change status of any player.

??? abstract "/f ahome &lt;target&gt;"
    Teleport a player to their faction's home.

    !!! success "Requirements"
        `factions.ahome` node.  

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

??? abstract "/f bypass"
    Set yourself to bypass faction permission checks.

    !!! success "Requirements"
        `factions.bypass` node.  

??? abstract "/f create &lt;name&gt;"
    Create a faction with the given name.

    !!! success "Requirements"
        `factions.create` node.  

??? abstract "/f defaultrole &lt;role&gt;"
    Set the default rank new members get when joining your faction.

    !!! success "Requirements"
        `factions.defaultrank` node.  
        Must be admin of your fraction to use

??? abstract "/f desc &lt;description...&gt;"
    Set your faction's new description.

    !!! success "Requirements"
        `factions.description` node.  
        Must be faction moderator or higher

??? abstract "/f deinvite &lt;target&gt;"
    Revoke an invite from a player. If no player is defined, it will list all players with pending invites. Click the names to revoke their invite.

    !!! success "Requirements"
        `factions.deinvite` node.
        Must be faction admin, or be granted `invite` perms in the faction.

??? abstract "/f demote &lt;name&gt;"
    Demote a player by one rank.

    !!! success "Requirements"
        `factions.promote` node.  

??? abstract "/f disband"
    Disband your faction.

    !!! success "Requirements"
        `factions.disband` node.  

??? abstract "/f invite &lt;target&gt;"
    Invite a player to your faction.

    !!! success "Requirements"
        `factions.invite` node.
        Must be faction admin, or be granted `invite` perms in the faction.

??? abstract "/f kick &lt;target&gt;"
    Kicks a player from the faction.

    !!! success "Requirements"
        `factions.kick` node.
        Must be faction admin, or be granted `kick` perms in the faction.

??? abstract "/f mod [name]"
    Promote a player in your faction to mod.

    !!! success "Requirements"
        `factions.mod` node.  
        Must be faction coleader or higher

??? abstract "/f open"
    Toggle allowing anyone being able to join the faction.

    !!! success "Requirements"
        `factions.open` node.  
        Must be faction moderator or higher

??? abstract "/f peaceful"
    Set a faction to being peaceful.

    !!! success "Requirements"
        `factions.setpeaceful` node.  
        Must be faction moderator or higher

??? abstract "/f perm [relation] [action] [access]"
    Manage permissions for your Faction. Example: /f perm recruit build deny. Opens GUI if no arguments provided.

    !!! success "Requirements"
        `factions.permissions` node.
        Must be faction admin.

??? abstract "/f promote &lt;name&gt;"
    Promote a player by one rank.

    !!! success "Requirements"
        `factions.promote` node.  

??? abstract "/f rel &lt;relation&gt; &lt;faction&gt;"
    Request to change your faction's relationship with a target faction. Relations can be ally, truce, neutral, enemy.

    !!! success "Requirements"
        `factions.relation` node.  
        Must be faction moderator or higher

??? abstract "/f showinvites"
    Show pending invites for your faction.

    !!! success "Requirements"
        `factions.showinvites` node.
        Must be faction admin, or be granted `invite` perms in the faction.

??? abstract "/f tag &lt;tag&gt;"
    Change your faction's tag.

    !!! success "Requirements"
        `factions.tag` node.
        Must be faction moderator or higher

??? abstract "/f title &lt;player&gt; [title]"
    Set a player's custom title. Will charge them if enabled.

    !!! success "Requirements"
        `factions.title` node.
        Must be faction moderator or higher

### Warp and home commands

??? abstract "/f delwarp &lt;name&gt;"
    Delete a warp

    !!! success "Requirements"
        `factions.setwarp` node.

??? abstract  "/f home"
    Teleports you to your faction's home.
    
    !!! success "Requirements"
        `factions.home`

??? abstract "/f sethome"
    Set your faction's home.

    !!! success "Requirements"
        `factions.sethome` node.

??? abstract "/f setwarp &lt;name&gt; [password]"
    Set a warp with an optional password to your location.

    !!! success "Requirements"
        `factions.setwarp` node.

??? abstract "/f warp &lt;name&gt; [password]"
    Teleports you to a warp, password optional. Opens GUI if no warp defined.

    !!! success "Requirements"
        `factions.warp` node.

### Server Administration

??? abstract "/f lock"
    Lock datafiles from being overwritten. Will make anything on the server not get saved.
    
    !!! success "Requirements"
        `factions.lock` node.

??? abstract "/f modifypower &lt;name&gt; &lt;power&gt;"
    Modify a player's power. The &lt;power&gt; variable adds power to the player's current power.
    
    !!! success "Requirements"
        `factions.save` node.

??? abstract "/f owner [name]"
    Set claim ownership for this chunk. Admins can specify a target player.
    
    !!! success "Requirements"
        `factions.owner` node.

??? abstract "/f permanent &lt;faction&gt;"
    Set a faction to permanent status. This will make the faction stay if there are zero members.
    
    !!! success "Requirements"
        `factions.setpermanent` node.

??? abstract "/f permanentpower &lt;faction&gt; [power]"
    Set permanent power to a faction.
    
    !!! success "Requirements"
        `factions.setpermanentpower` node.

??? abstract "/f powerboost &lt;player/faction&gt; &lt;name&gt; &lt;number&gt;"
    Set powerboost of a player or faction. &lt;player/faction&gt; can be 'f' or 'p' to let the plugin know if you're specifying a player or faction.

    !!! success "Requirements"
        `factions.powerboost` node.  

??? abstract "/f reload"
    Reload configurations (lang.yml, config.yml, conf.json). This does not reload factions saved data from disk.
    
    !!! success "Requirements"
        `factions.reload` node.

??? abstract "/f setmaxvaults &lt;faction&gt; &lt;number&gt;"
    Set the max vaults a faction can have.
    
    !!! success "Requirements"
        `factions.setmaxvaults` node.


??? abstract "/f saveall"
    Force save all factions data to disk.
    
    !!! success "Requirements"
        `factions.save` node.