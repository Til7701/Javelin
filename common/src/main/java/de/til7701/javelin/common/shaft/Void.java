package de.til7701.javelin.common.shaft;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.util.Ignore;

@JavelinType(
        fullyQualifiedJavelinName = Void.JAVELIN_TYPE_NAME
)
public final class Void implements Primitive {

    static final String JAVELIN_TYPE_NAME = "javelin.shaft.Void";
    public static final SimpleType TYPE = new SimpleType(Span.undefined(), JAVELIN_TYPE_NAME);

    private static final Void INSTANCE = new Void();

    @Ignore
    private Void() {
    }

    @Ignore
    public static Void of() {
        return INSTANCE;
    }

    @Ignore
    @Override
    public Type type() {
        return TYPE;
    }

}
