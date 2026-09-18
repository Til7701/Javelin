package de.til7701.javelin.common.shaft;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.util.Ignore;
import lombok.Getter;

public final class Bool implements Primitive {

    public static final SimpleType TYPE = new SimpleType(Span.undefined(), "Bool");

    @Getter(onMethod_ = {@Ignore})
    private final boolean value;

    @Ignore
    private Bool(boolean value) {
        this.value = value;
    }

    @Ignore
    public static Bool of(boolean value) {
        return new Bool(value);
    }

    public static Bool and(Bool left, Bool right) {
        return of(left.value && right.value);
    }

    public static Bool eq(Bool left, Bool right) {
        return of(left.value == right.value);
    }

    public static Str asStr(Bool value) {
        return Str.of(Boolean.toString(value.value));
    }

    @Override
    public Bool copy() {
        return of(value);
    }

    @Ignore
    @Override
    public Type type() {
        return TYPE;
    }

}
