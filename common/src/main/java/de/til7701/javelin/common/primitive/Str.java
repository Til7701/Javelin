package de.til7701.javelin.common.primitive;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;

public final class Str {

    public static final SimpleType TYPE = new SimpleType(Span.undefined(), "Str");

    private Str() {
    }

}
