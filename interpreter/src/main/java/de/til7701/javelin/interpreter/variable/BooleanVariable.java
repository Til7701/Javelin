package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.primitive.Bool;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
class BooleanVariable implements Variable {

    protected boolean value;

    @Override
    public Type type() {
        return Bool.TYPE;
    }

    @Override
    public void set(Variable variable) {
        if (variable instanceof BooleanVariable v)
            this.value = v.value;
        else
            throw new IllegalArgumentException();
    }

    @Override
    public Variable copyRef() {
        return new BooleanVariable(value);
    }

    @Override
    public Object javaValue() {
        return value;
    }

}
