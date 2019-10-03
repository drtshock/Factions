package com.massivecraft.factions.cmd.top;

/**
 * FTopValue is a quickly-comparable derived value of a faction with the purpose of being compared against other
 * FTopValues to determine Faction ranking.
 * @param <T> derived class
 */
public interface FTopValue<T extends FTopValue> extends Comparable<T> {
    /**
     * Returns a string which is suitable to display to the user
     * @return display string
     */
    String getDisplayString();
}
