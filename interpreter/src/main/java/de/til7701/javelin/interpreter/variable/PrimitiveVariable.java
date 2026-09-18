package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.primitive.Primitive;

public class PrimitiveVariable implements Variable {

    private Primitive value;

    public PrimitiveVariable(Primitive value) {
        this.value = value;
    }

    @Override
    public Type type() {
        return value.type();
    }

    @Override
    public void set(Variable variable) {
        if (variable instanceof PrimitiveVariable primitive) {
            this.value = primitive.value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Variable createNew() {
        return new PrimitiveVariable(value);
    }

    @Override
    public Object javaValue() {
        return value;
    }

}
