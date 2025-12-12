package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

public record IntegerLiteralExpression(
        Span span,
        String value
) implements Expression {
}
