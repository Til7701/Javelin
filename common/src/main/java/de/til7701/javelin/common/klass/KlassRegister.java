package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.NotImplementedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KlassRegister {

    private final Map<String, Klass> klasses = new HashMap<>();

    public void registerKlass(Klass klass) {
        klasses.put(klass.name(), klass);
    }

    public Optional<Klass> getKlass(Type type) {
        return switch (type) {
            case SimpleType(_, String name) -> Optional.of(klasses.get(name));
            default -> throw new NotImplementedException(type.toString());
        };
    }

}
