# Introduction to Factions

## What is a faction?

It's a group of players, like a team or a club. A faction can **claim territory**, work together to **build a base**,
and build (or destroy) relationships with other factions. 

## Territory

!!! info "Land control in Factions is per chunk"  
    In Minecraft, a "chunk" is a 3D piece of the Minecraft world. It goes from the bedrock all the way into the sky (y)
    and is 16 blocks by 16 blocks in the horizontal dimensions (x and z).

A faction claims land by chunk, dependent on how much [power](#power) they possess, at a rate of 1 chunk per power.
Within its territory, members of the faction can exclusively build without interference of non-members (configurable
through the `/f perms` command). Your faction's territory is where you can build your base, set your faction's home 
location, create warp points, and more.

Claiming territory is performed with the `/f claim` command.

!!! tip
    Want to see the boundaries of the chunk in which you're standing? Use the `/f seechunk` command!

## Power

Every player has a quantity of "power." A new player typically starts with 0 power and slowly gains power over time by
playing on the server. Dying takes a way a set amount of power. 

When a player is part of a faction, their faction's power is calculated as a total of its players' power. Power is how
factions acquire territory, or raid another faction. A faction can claim territory as long as its power is greater than
its current territory claims. 

## Raiding

If, through player death or players leaving a faction, territory count is greater than power the faction can be raided
by enemies who can now destroy blocks in their enemy's territory and maybe even claim territory away from the faction.
