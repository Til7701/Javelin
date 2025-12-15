package de.til7701.javelin.klass;

import de.til7701.javelin.ast.type.Type;

public sealed interface Metod permits JavaMetod, JavelinMetod {

    String name();

    Type returnType();

    Type[] parameterTypes();

    String[] parameterNames();

}
