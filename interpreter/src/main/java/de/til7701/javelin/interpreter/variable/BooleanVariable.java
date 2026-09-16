package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
class BooleanVariable implements Variable {

    protected boolean value;

    @Override
    public Type type() {
        return new SimpleType(Span.undefined(), "Boolean");
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

}
