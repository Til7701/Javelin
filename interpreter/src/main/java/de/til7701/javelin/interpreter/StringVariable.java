package de.til7701.javelin.interpreter;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class StringVariable implements Variable {

    private String value;

    @Override
    public Type type() {
        return new SimpleType(Span.undefined(), "String");
    }

    @Override
    public void set(Variable variable) {
        if (variable instanceof StringVariable v)
            this.value = v.value;
        else
            throw new IllegalArgumentException();
    }

    @Override
    public Variable copy() {
        return new StringVariable(value);
    }

    @Override
    public Variable clone() {
        return this.copy();
    }

}
