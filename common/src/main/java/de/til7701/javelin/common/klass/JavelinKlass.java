package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.statement.Import;

import java.util.List;
import java.util.Map;

public record JavelinKlass(
        List<Import> imports,
        boolean isPub,
        boolean isNative,
        String fullyQualifiedJavelinName,
        List<KlassField> fields,
        List<JavelinMetod> constructors,
        List<Metod> methods,
        Map<String, List<Metod>> methodsGroupedByName
) implements Klass {
}
