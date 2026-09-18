package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.type.Type;

import java.lang.reflect.Method;

public record JavaMetod(
        boolean isStatic,
        Method javaMethod,
        String name,
        Type returnType,
        Type[] parameterTypes,
        boolean needsNatives
) implements Metod {
}
