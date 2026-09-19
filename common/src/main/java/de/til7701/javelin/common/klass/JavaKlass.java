package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.statement.Import;

import java.util.List;
import java.util.Map;

public record JavaKlass(
        boolean isPub,
        Class<?> javaClass,
        String fullyQualifiedJavelinName,
        List<Metod> methods,
        Map<String, List<Metod>> methodsGroupedByName
) implements Klass {

    public List<Import> imports() {
        return List.of();
    }

}
