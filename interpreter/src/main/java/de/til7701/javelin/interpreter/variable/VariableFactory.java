package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.common.NotImplementedException;
import de.til7701.javelin.common.primitive.Primitive;
import de.til7701.javelin.common.primitive.Str;
import de.til7701.javelin.common.primitive.Void;
import org.jspecify.annotations.Nullable;

public class VariableFactory {

    private static final Variable VOID = new PrimitiveVariable(Void.of());
    private final LiteralStore literalStore = new LiteralStore();

    public Variable fromBoolLiteral(boolean value) {
        return literalStore.forBool(value);
    }

    public Variable fromStrLiteral(String value) {
        return new PrimitiveVariable(Str.of(value));
    }

    public String literalsToString() {
        return literalStore.valuesToString();
    }

    public Variable fromJavaValue(@Nullable Object result) {
        return switch (result) {
            case null -> VOID;
            case Primitive primitive -> new PrimitiveVariable(primitive);
            default -> throw new NotImplementedException(result.getClass().toString());
        };
    }

}
