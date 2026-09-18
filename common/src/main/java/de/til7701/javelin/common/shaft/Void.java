package de.til7701.javelin.common.shaft;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.util.Ignore;

public class Void implements Primitive {

    public static final SimpleType TYPE = new SimpleType(Span.undefined(), "Void");

    private static final Void INSTANCE = new Void();

    @Ignore
    private Void() {
    }

    @Ignore
    public static Void of() {
        return INSTANCE;
    }

    @Override
    public Void copy() {
        return of();
    }

    @Ignore
    @Override
    public Type type() {
        return TYPE;
    }

}
