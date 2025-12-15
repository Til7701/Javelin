package de.til7701.javelin.klass;

import java.util.List;
import java.util.Map;

public record JavaKlass(
        boolean isPub,
        boolean isNative,
        Class<?> javaClass,
        String name,
        List<Metod> methods,
        Map<String, List<Metod>> methodsGroupedByName
) implements Klass {
}
