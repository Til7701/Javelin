package de.til7701.continu_num.core.reflect;

import java.lang.reflect.Method;

public record Operation(
        Type leftType,
        Type rightType,
        Type resultType,
        Method declaringJavaMethod) {
}
