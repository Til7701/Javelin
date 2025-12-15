package de.til7701.javelin.util;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.IType;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;

import java.util.Map;

public class Java {

    private static final Map<Class<?>, Type> typeMappingCache = Map.of(
            Short.class, new IType(invalidSpan(), 16),
            Integer.class, new IType(invalidSpan(), 32),
            Long.class, new IType(invalidSpan(), 64),
            String.class, new SimpleType(invalidSpan(), "Str"),
            Boolean.class, new IType(invalidSpan(), 1)
    );

    public static Type mapTypes(Class<?> clazz) {
        return typeMappingCache.getOrDefault(clazz, new SimpleType(invalidSpan(), clazz.getSimpleName()));
    }

    private static Span invalidSpan() {
        return new Span(-1, -1, -1, -1);
    }

}
