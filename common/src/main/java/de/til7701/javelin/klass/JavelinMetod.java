package de.til7701.javelin.klass;

import de.til7701.javelin.ast.statement.Statement;
import de.til7701.javelin.ast.type.Type;

import java.util.Optional;

public record JavelinMetod(
        String name,
        Type returnType,
        Type[] parameterTypes,
        String[] parameterNames,
        Optional<Statement> body
) implements Metod {

    public JavelinMetod(String name, Type returnType) {
        this(name, returnType, new Type[0], new String[0], Optional.empty());
    }

}
