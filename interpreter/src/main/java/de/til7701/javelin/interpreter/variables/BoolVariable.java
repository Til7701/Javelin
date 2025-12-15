package de.til7701.javelin.interpreter.variables;

import de.til7701.javelin.core.reflect.Bool;
import de.til7701.javelin.core.reflect.Type;
import de.til7701.javelin.interpreter.Variable;

public class BoolVariable implements Variable, Bool {

    public static final BoolVariable TRUE = new BoolVariable(true);
    public static final BoolVariable FALSE = new BoolVariable(false);

    private final boolean mutable;

    private boolean value;

    public BoolVariable(Object value) {
        this(false, value);
    }

    public BoolVariable(boolean mutable, Object value) {
        this.mutable = mutable;
        this.value = toBool(value);
    }

    private static boolean toBool(Object value) {
        return switch (value) {
            case BoolVariable v -> v.value;
            case String str -> Boolean.parseBoolean(str);
            case Boolean b -> b;
            default -> throw new IllegalArgumentException("Cannot convert value to string: " + value);
        };
    }

    public void setValue(boolean value) {
        if (!mutable) {
            throw new UnsupportedOperationException("Variable is immutable");
        }
        this.value = value;
    }

    @Override
    public Type type() {
        return Bool.instance();
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public Boolean value() {
        return value;
    }

    @Override
    public Variable asMutable() {
        return new BoolVariable(true, value);
    }

}
