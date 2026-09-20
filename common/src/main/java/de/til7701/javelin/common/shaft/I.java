package de.til7701.javelin.common.shaft;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.util.Ignore;
import lombok.Getter;

@JavelinType(
        fullyQualifiedJavelinName = I.JAVELIN_TYPE_NAME
)
public final class I implements Primitive {

    static final String JAVELIN_TYPE_NAME = "javelin.shaft.I";
    public static final SimpleType TYPE = new SimpleType(Span.undefined(), JAVELIN_TYPE_NAME);

    @Getter(onMethod_ = {@Ignore})
    private final int value;

    @Ignore
    private I(int value) {
        this.value = value;
    }

    @Ignore
    public static I of(int value) {
        return new I(value);
    }

    public static I add(I left, I right) {
        return of(left.value + right.value);
    }

    public static Str asStr(I value) {
        return Str.of(Integer.toString(value.value));
    }

    @Override
    public I copy() {
        return of(value);
    }

    @Ignore
    @Override
    public Type type() {
        return TYPE;
    }
}
