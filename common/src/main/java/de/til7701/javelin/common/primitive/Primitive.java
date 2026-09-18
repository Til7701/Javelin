package de.til7701.javelin.common.primitive;

import de.til7701.javelin.ast.type.Type;

public interface Primitive {

    Primitive copy();

    Type type();

}
