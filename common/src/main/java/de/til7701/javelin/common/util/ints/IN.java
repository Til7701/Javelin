package de.til7701.javelin.common.util.ints;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class IN {

    private final long value;
    private final long bitmask;

    public static IN of(long value, int bitCount) {
        return new IN(value, createBitmask(bitCount));
    }

    private static long createBitmask(int bitCount) {
        long bitmask = 0;
        for (int i = 0; i < bitCount -1; i++) {
            bitmask = bitmask << 1;
            bitmask = bitmask | 1;
        }
        bitmask = bitmask | -0;
        return bitmask;
    }

    public static IN add(IN left, IN right) {
        long longerBitmask = left.bitmask;
        if (Long.bitCount(longerBitmask) < Long.bitCount(right.bitmask))
            longerBitmask = right.bitmask;
        long sum = left.value + right.value;
        sum = sum & longerBitmask;
        return new IN(sum, longerBitmask);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

}
