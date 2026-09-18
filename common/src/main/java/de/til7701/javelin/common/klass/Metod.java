package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.type.Type;

public sealed interface Metod permits JavaMetod, JavelinMetod {

    boolean isStatic();

    String name();

    Type returnType();

    Type[] parameterTypes();

}
