package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

import java.util.List;

public record CollectionCreationExpression(
        Span span,
        List<Expression> elements
) implements Expression {
}
