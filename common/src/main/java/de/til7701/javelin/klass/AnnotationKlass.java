package de.til7701.javelin.klass;

import java.util.List;
import java.util.Map;

public record AnnotationKlass(
        boolean isPub,
        boolean isNative,
        String name,
        List<AnnotationField> fields,
        List<Metod> methods,
        Map<String, List<Metod>> methodsGroupedByName
) implements Klass {

}
