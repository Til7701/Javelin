package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

public record RightUnaryExpression(
        Span span,
        Expression expression,
        BinaryOperator operator
) implements Expression {
}
