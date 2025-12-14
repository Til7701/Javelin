package de.til7701.javelin.klass;

import de.til7701.javelin.ast.type.Type;

public record JavaMetod(
        Class<?> javaClass,
        String name,
        Type returnType,
        Type[] parameterTypes,
        Class<?>[] javaParameterClasses,
        String[] parameterNames
) implements Metod {
}
