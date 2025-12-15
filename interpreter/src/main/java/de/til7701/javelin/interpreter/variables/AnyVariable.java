package de.til7701.javelin.interpreter.variables;

import de.til7701.javelin.core.reflect.Any;
import de.til7701.javelin.core.reflect.Type;
import de.til7701.javelin.interpreter.Variable;

public class AnyVariable implements Variable, Any {

    private final boolean mutable;

    private Object value;

    public AnyVariable(Object value) {
        this(false, value);
    }

    public AnyVariable(boolean mutable, Object value) {
        this.mutable = mutable;
        this.value = value;
    }

    public void setValue(String value) {
        if (!mutable) {
            throw new UnsupportedOperationException("Variable is immutable");
        }
        this.value = value;
    }

    @Override
    public Type type() {
        return Any.instance();
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public Variable asMutable() {
        return new AnyVariable(true, value);
    }

}
