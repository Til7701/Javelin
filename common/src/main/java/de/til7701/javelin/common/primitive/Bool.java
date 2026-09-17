package de.til7701.javelin.common.primitive;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;

public final class Bool {

    public static final SimpleType TYPE = new SimpleType(Span.undefined(), "Bool");

    private Bool() {
    }

    public static boolean and(boolean left, boolean right) {
        return left && right;
    }

}
