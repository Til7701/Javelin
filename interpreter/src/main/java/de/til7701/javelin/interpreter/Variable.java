package de.til7701.javelin.interpreter;

import de.til7701.javelin.core.reflect.Type;

public interface Variable {

    Type type();

    boolean isMutable();

    Object value();

    Variable asMutable();

}
