package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

public record UnsignedIntegerLiteralExpression(
        Span span,
        long value,
        long bitCount
) implements Expression {
}
