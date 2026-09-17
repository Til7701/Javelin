package de.til7701.javelin.primitive;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;

public final class Str {

    public static final SimpleType TYPE = new SimpleType(Span.undefined(), "Str");

    private Str() {
    }

}
