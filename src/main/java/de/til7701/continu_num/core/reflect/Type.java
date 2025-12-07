package de.til7701.continu_num.core.reflect;

public sealed interface Type permits Any, I16, I32, None, Str {

    boolean isAssignableFrom(Type other);

    static Type fromName(String name) {
        return switch (name) {
            case "i16" -> I16.instance();
            case "i32" -> I32.instance();
            case "str" -> Str.instance();
            case "none" -> None.instance();
            default -> throw new IllegalArgumentException("Unknown type name: " + name);
        };
    }

}
