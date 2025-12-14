package de.til7701.javelin.operation;

import de.til7701.javelin.ast.type.Type;

import java.lang.reflect.Method;

public record Operation(
        Type leftType,
        Type rightType,
        Type resultType,
        Method declaringJavaMethod
) {
}
