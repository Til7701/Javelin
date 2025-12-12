package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

public record BooleanLiteralExpression(
        Span span,
        boolean value
) implements Expression {
}
