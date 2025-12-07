package de.til7701.continu_num.interpreter;

import de.til7701.continu_num.core.reflect.Type;

public interface Variable {

    Type type();

    boolean isMutable();

    Object value();

    Variable asMutable();

}
