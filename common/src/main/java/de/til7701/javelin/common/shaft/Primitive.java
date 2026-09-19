package de.til7701.javelin.common.shaft;

import de.til7701.javelin.ast.type.Type;

public sealed interface Primitive permits Array, Bool, Str, Void {

    Primitive copy();

    Type type();

}
