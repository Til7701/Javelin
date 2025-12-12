package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.Expression;
import de.til7701.javelin.ast.type.Type;

public record VariableInitialization(
        Span span,
        boolean isMutable,
        Type type,
        String name,
        Expression value
) implements Statement {
}
