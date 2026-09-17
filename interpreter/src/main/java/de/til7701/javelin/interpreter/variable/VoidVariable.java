package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.primitive.Void;
import lombok.ToString;

@ToString
class VoidVariable implements Variable {

    public static Variable INSTANCE = new VoidVariable();

    private VoidVariable() {
    }

    @Override
    public Type type() {
        return Void.TYPE;
    }

    @Override
    public void set(Variable variable) {
        // NOOP
    }

    @Override
    public Variable copyRef() {
        return this;
    }

    @Override
    public Object javaValue() {
        throw new UnsupportedOperationException();
    }

}
