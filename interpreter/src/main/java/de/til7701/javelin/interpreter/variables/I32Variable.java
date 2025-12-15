package de.til7701.javelin.interpreter.variables;

import de.til7701.javelin.core.reflect.Bool;
import de.til7701.javelin.core.reflect.I32;
import de.til7701.javelin.core.reflect.Type;
import de.til7701.javelin.interpreter.Variable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class I32Variable implements Variable, I32 {

    private static final I32 MIN_VALUE = new I32Variable(false, Integer.MIN_VALUE);
    private static final I32 MAX_VALUE = new I32Variable(false, Integer.MAX_VALUE);

    private final boolean mutable;

    private int value;

    public I32Variable(Object value) {
        this(false, value);
    }

    public I32Variable(boolean mutable, Object value) {
        this.mutable = mutable;
        this.value = toInt(value);
    }

    private static int toInt(Object value) {
        return switch (value) {
            case I32Variable v -> v.value;
            case Integer i -> i;
            case Number n -> n.intValue();
            case String str -> Integer.parseInt(str);
            case Boolean b -> b ? 1 : 0;
            default -> throw new IllegalArgumentException("Cannot convert value to int: " + value);
        };
    }

    @Override
    public Type type() {
        return I32.instance();
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public Variable asMutable() {
        return new I32Variable(true, value);
    }

    public void setValue(int value) {
        if (!mutable) {
            throw new UnsupportedOperationException("Variable is immutable");
        }
        this.value = value;
    }

    @Override
    public I32 minValue() {
        return MIN_VALUE;
    }

    @Override
    public I32 maxValue() {
        return MAX_VALUE;
    }

    @Override
    public I32 add(I32 a, I32 b) {
        if (a instanceof I32Variable va && b instanceof I32Variable vb) {
            short result = (short) (va.value + vb.value);
            return new I32Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I16Variable instances");
    }

    @Override
    public I32 sub(I32 a, I32 b) {
        if (a instanceof I32Variable va && b instanceof I32Variable vb) {
            int result = va.value - vb.value;
            return new I32Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I32Variable instances");
    }

    @Override
    public I32 mul(I32 a, I32 b) {
        if (a instanceof I32Variable va && b instanceof I32Variable vb) {
            int result = va.value * vb.value;
            return new I32Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I32Variable instances");
    }

    @Override
    public I32 div(I32 a, I32 b) {
        if (a instanceof I32Variable va && b instanceof I32Variable vb) {
            if (vb.value == 0) {
                throw new ArithmeticException("Division by zero");
            }
            int result = va.value / vb.value;
            return new I32Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I32Variable instances");
    }

    @Override
    public Bool lt(I32 a, I32 b) {
        if (a instanceof I32Variable va && b instanceof I32Variable vb) {
            return va.value < vb.value ? BoolVariable.TRUE : BoolVariable.FALSE;
        }
        throw new IllegalArgumentException("Operands must be I32Variable instances");
    }

}
