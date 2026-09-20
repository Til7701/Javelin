package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.Type;

public record StaticFieldAccess(
        Span span,
        Type type,
        String fieldName
) implements Expression {
}
