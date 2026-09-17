package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.statement.Statement;
import de.til7701.javelin.ast.type.Type;

import java.util.Optional;

public record JavelinMetod(
        boolean isStatic,
        String name,
        Type returnType,
        Type[] parameterTypes,
        String[] parameterNames,
        Optional<Statement> body
) implements Metod {
}
