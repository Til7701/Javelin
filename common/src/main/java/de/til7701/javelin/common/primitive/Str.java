package de.til7701.javelin.common.primitive;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.util.Ignore;
import lombok.Getter;

public final class Str implements Primitive {

    public static final SimpleType TYPE = new SimpleType(Span.undefined(), "Str");

    @Getter(onMethod_ = {@Ignore})
    private final String value;

    @Ignore
    private Str(String value) {
        this.value = value;
    }

    @Ignore
    public static Str of(String value) {
        return new Str(value);
    }

    public static Str add(Str left, Str right) {
        return of(left.value + right.value);
    }

    public static Bool eq(Str left, Str right) {
        return Bool.of(left.value.equals(right.value));
    }

    @Override
    public Str copy() {
        return of(value);
    }

    @Ignore
    @Override
    public Type type() {
        return TYPE;
    }

}
