package de.til7701.javelin.common.util.ints;

import de.til7701.javelin.common.NotImplementedException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@RequiredArgsConstructor
public final class IN {

    private static final long ONES = 0xffffffffffffffffL;

    private final long value;
    private final long valueBitmask;

    public static IN of(long value, int bitCount) {
        if (bitCount > 64)
            throw new NotImplementedException();
        return new IN(value, createBitmask(bitCount));
    }

    private static long createBitmask(int bitCount) {
        long bitmask = 0;
        for (int i = 0; i < bitCount - 1; i++) {
            bitmask = bitmask << 1;
            bitmask |= 1;
        }
        return bitmask;
    }

    private static long longerBitmask(IN in1, IN in2) {
        long longerBitmask = in1.valueBitmask;
        if (Long.bitCount(longerBitmask) < Long.bitCount(in2.valueBitmask))
            longerBitmask = in2.valueBitmask;
        return longerBitmask;
    }

    private static long crop(long valueToCrop, long bitmask) {
        long signBitmask = Long.highestOneBit(bitmask) << 1;
        boolean sign = Long.bitCount(valueToCrop & signBitmask) > 0;
        long cropped = valueToCrop & bitmask;
        if (!sign) {
            return cropped;
        } else {
            long signAndRest = ONES & ~bitmask;
            return cropped | signAndRest;
        }
    }

    public static IN add(IN left, IN right) {
        long sum = left.value + right.value;
        long longerBitmask = longerBitmask(left, right);
        sum = crop(sum, longerBitmask);
        return new IN(sum, longerBitmask);
    }

    public static IN sub(IN left, IN right) {
        long difference = left.value - right.value;
        long longerBitmask = longerBitmask(left, right);
        difference = crop(difference, longerBitmask);
        return new IN(difference, longerBitmask);
    }

    @Override
    public String toString() {
        return Long.toString(value);
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof IN in)) return false;
        return value == in.value && valueBitmask == in.valueBitmask;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, valueBitmask);
    }

}
