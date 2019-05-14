package com.massivecraft.factions.struct;

/**
 *  Represents a entity within the world space that acts
 *  upon Faction data, Players, and Factions
 */
public interface FactionEntity {

    /**
     * Get the Id of this entity, UUID for Players, Numerical for Factions
     * @return this entity's Id
     */
    String getId();

    /**
     * Get the name of this entity, Username for players, Tag for factions
     * @return this entity's display name
     */
    String getName();

}
