package de.til7701.continu_num.interpreter.variables;

import de.til7701.continu_num.core.reflect.I16;
import de.til7701.continu_num.core.reflect.Type;
import de.til7701.continu_num.interpreter.Variable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class I16Variable implements Variable, I16 {

    private static final I16 MIN_VALUE = new I16Variable(false, Short.MIN_VALUE);
    private static final I16 MAX_VALUE = new I16Variable(false, Short.MAX_VALUE);

    private final boolean mutable;

    private short value;

    public I16Variable(boolean mutable, Object value) {
        this.mutable = mutable;
        this.value = toShort(value);
    }

    private static short toShort(Object value) {
        return switch (value) {
            case I16Variable v -> v.value;
            case Short s -> s;
            case Number n -> n.shortValue();
            case String str -> Short.parseShort(str);
            case Boolean b -> (short) (b ? 1 : 0);
            default -> throw new IllegalArgumentException("Cannot convert value to short: " + value);
        };
    }

    @Override
    public Type type() {
        return I16.instance();
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public Short value() {
        return value;
    }

    @Override
    public Variable asMutable() {
        return new I16Variable(true, value);
    }

    public void setValue(short value) {
        if (!mutable) {
            throw new UnsupportedOperationException("Variable is immutable");
        }
        this.value = value;
    }

    @Override
    public I16 minValue() {
        return MIN_VALUE;
    }

    @Override
    public I16 maxValue() {
        return MAX_VALUE;
    }

    @Override
    public I16 add(I16 a, I16 b) {
        if (a instanceof I16Variable va && b instanceof I16Variable vb) {
            short result = (short) (va.value + vb.value);
            return new I16Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I16Variable instances");
    }

    @Override
    public I16 sub(I16 a, I16 b) {
        if (a instanceof I16Variable va && b instanceof I16Variable vb) {
            short result = (short) (va.value - vb.value);
            return new I16Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I16Variable instances");
    }

    @Override
    public I16 mul(I16 a, I16 b) {
        if (a instanceof I16Variable va && b instanceof I16Variable vb) {
            short result = (short) (va.value * vb.value);
            return new I16Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I16Variable instances");
    }

    @Override
    public I16 div(I16 a, I16 b) {
        if (a instanceof I16Variable va && b instanceof I16Variable vb) {
            if (vb.value == 0) {
                throw new ArithmeticException("Division by zero");
            }
            short result = (short) (va.value / vb.value);
            return new I16Variable(false, result);
        }
        throw new IllegalArgumentException("Operands must be I16Variable instances");
    }
}
