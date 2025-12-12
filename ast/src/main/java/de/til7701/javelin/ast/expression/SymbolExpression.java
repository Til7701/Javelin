package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;

public record SymbolExpression(
        Span span,
        String identifier
) implements Expression {
}
