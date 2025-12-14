package de.til7701.javelin.klass;

import java.util.List;
import java.util.Map;

public record EnumKlass(
        boolean isPub,
        boolean isNative,
        String name,
        List<String> enumConstants,
        List<Metod> methods
) implements Klass {

    @Override
    public Map<String, List<Metod>> methodsGroupedByName() {
        return Map.of();
    }

}
