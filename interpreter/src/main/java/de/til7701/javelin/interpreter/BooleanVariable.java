package de.til7701.javelin.interpreter;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class BooleanVariable implements Variable {

    private boolean value;

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
    public Variable copy() {
        return new BooleanVariable(value);
    }

    @Override
    public Variable clone() {
        return this.copy();
    }

}
