package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

public record LeftUnaryExpression(
        Span span,
        BinaryOperator operator,
        Expression expression
) implements Expression {
}
