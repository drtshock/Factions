package com.massivecraft.factions.cmd.top;

/**
 * Implementation of FTopValue which sorts itself by virtue of value being greater than another FTopGTNumberValue.
 * @param <T> derived class
 * @param <N> value type
 */
public abstract class FTopGTNumberValue<T extends FTopGTNumberValue<T, N>, N extends Comparable<N>>
        implements FTopValue<T> {

    public FTopGTNumberValue(N value) {
        this.value = value;
    }

    protected final N value;

    @Override
    public int compareTo(T arg) {
        // When our value is greater than our arguments value, we want to be lower on the list. value.compareTo will
        // return >0, but we want to instead be <0. In the opposite case, invert the statement. When zero, -zero is zero.
        return -value.compareTo(arg.value);
    }
}
