package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.common.NotImplementedException;
import org.jspecify.annotations.Nullable;

public class VariableFactory {

    private final LiteralStore literalStore = new LiteralStore();

    public Variable fromBool(boolean value) {
        return new BooleanVariable(value);
    }

    public Variable fromBoolLiteral(boolean value) {
        return literalStore.forBool(value);
    }

    public Variable fromStrLiteral(String value) {
        return new StringVariable(value);
    }

    public String literalsToString() {
        return literalStore.valuesToString();
    }

    public Variable fromJavaValue(@Nullable Object result) {
        return switch (result) {
            case null -> VoidVariable.INSTANCE;
            case Boolean b -> fromBool(b);
            case String s -> fromStrLiteral(s);
            default -> throw new NotImplementedException();
        };
    }

}
