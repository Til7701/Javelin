package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.primitive.Str;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
class StringVariable implements Variable {

    private String value;

    @Override
    public Type type() {
        return Str.TYPE;
    }

    @Override
    public void set(Variable variable) {
        if (variable instanceof StringVariable v)
            this.value = v.value;
        else
            throw new IllegalArgumentException();
    }

    @Override
    public Variable copyRef() {
        return new StringVariable(value);
    }

    @Override
    public Object javaValue() {
        return value;
    }

}
