package de.til7701.continu_num.core.reflect;

import java.util.Arrays;
import java.util.Objects;

public record JavaMetod(
        Class<?> javaClass,
        String name,
        Type returnType,
        Type[] parameterTypes,
        Class<?>[] javaParameterClasses,
        String[] parameterNames
) implements Metod {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JavaMetod javaMetod = (JavaMetod) o;
        return Objects.equals(name, javaMetod.name) && Objects.equals(returnType, javaMetod.returnType) && Objects.equals(javaClass, javaMetod.javaClass) && Objects.deepEquals(parameterTypes, javaMetod.parameterTypes) && Objects.deepEquals(parameterNames, javaMetod.parameterNames) && Objects.deepEquals(javaParameterClasses, javaMetod.javaParameterClasses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(javaClass, name, returnType, Arrays.hashCode(parameterTypes), Arrays.hashCode(javaParameterClasses), Arrays.hashCode(parameterNames));
    }

    @Override
    public String toString() {
        return "JavaMetod{" +
                "javaClass=" + javaClass +
                ", name='" + name + '\'' +
                ", returnType=" + returnType +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", javaParameterClasses=" + Arrays.toString(javaParameterClasses) +
                ", parameterNames=" + Arrays.toString(parameterNames) +
                '}';
    }
}
