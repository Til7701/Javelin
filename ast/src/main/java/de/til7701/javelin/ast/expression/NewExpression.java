package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

public record NewExpression(
        Span span,
        Expression expression
) implements Expression {
}
