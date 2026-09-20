package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.type.IType;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.NotImplementedException;
import de.til7701.javelin.common.environment.Imports;
import de.til7701.javelin.common.shaft.I;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class KlassRegister {

    private final Map<String, Klass> klasses = new HashMap<>();

    public void registerKlass(Klass klass) {
        klasses.put(klass.fullyQualifiedJavelinName(), klass);
    }

    public Optional<Klass> getKlass(Type type, Imports imports) {
        return switch (type) {
            case SimpleType(_, String name) -> Optional.of(klasses.get(imports.map(name)));
            case IType _ -> Optional.of(klasses.get(I.TYPE.name()));
            default -> throw new NotImplementedException(type.toString());
        };
    }

}
