package de.til7701.javelin.interpreter.variables;

import de.til7701.javelin.core.reflect.I32;
import de.til7701.javelin.core.reflect.Str;
import de.til7701.javelin.core.reflect.Type;
import de.til7701.javelin.interpreter.Variable;

public class StrVariable implements Variable, Str {

    private final boolean mutable;

    private String value;

    public StrVariable(String value) {
        this(false, value);
    }

    public StrVariable(boolean mutable, Object value) {
        this.mutable = mutable;
        this.value = toStr(value);
    }

    private static String toStr(Object value) {
        return switch (value) {
            case StrVariable v -> v.value;
            case String str -> str;
            case Character c -> String.valueOf(c);
            case Number n -> n.toString();
            case Boolean b -> b ? "true" : "false";
            default -> throw new IllegalArgumentException("Cannot convert value to string: " + value);
        };
    }

    public void setValue(String value) {
        if (!mutable) {
            throw new UnsupportedOperationException("Variable is immutable");
        }
        this.value = value;
    }

    @Override
    public Type type() {
        return Str.instance();
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public Variable asMutable() {
        return new StrVariable(true, value);
    }

    @Override
    public Str charAt(Str str, I32 index) {
        if (!(str instanceof StrVariable strVar)) {
            throw new IllegalArgumentException("Expected StrVariable for str parameter");
        }
        if (!(index instanceof I32Variable indexVar)) {
            throw new IllegalArgumentException("Expected I32Variable for index parameter");
        }

        char ch = strVar.value.charAt(indexVar.value());
        return new StrVariable(String.valueOf(ch));
    }

}
