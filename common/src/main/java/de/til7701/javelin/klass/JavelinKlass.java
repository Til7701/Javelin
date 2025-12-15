package de.til7701.javelin.klass;

import java.util.List;
import java.util.Map;

public record JavelinKlass(
        boolean isPub,
        boolean isNative,
        String name,
        List<KlassField> fields,
        List<JavelinConstructor> constructors,
        List<Metod> methods,
        Map<String, List<Metod>> methodsGroupedByName
) implements Klass {
}
