package de.til7701.javelin.common.util;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.primitive.Bool;
import de.til7701.javelin.common.primitive.Str;

import java.util.Map;

public class Java {

    private static final Map<Class<?>, Type> typeMappingCache = Map.of(
            String.class, Str.TYPE,
            Boolean.class, Bool.TYPE
    );

    public static Type mapTypes(Class<?> clazz) {
        return typeMappingCache.getOrDefault(clazz, new SimpleType(Span.undefined(), clazz.getSimpleName()));
    }

}
