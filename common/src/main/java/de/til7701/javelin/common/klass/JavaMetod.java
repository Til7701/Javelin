package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.type.Type;

public record JavaMetod(
        boolean isStatic,
        Class<?> javaClass,
        String name,
        Type returnType,
        Type[] parameterTypes,
        Class<?>[] javaParameterClasses,
        String[] parameterNames
) implements Metod {
}
