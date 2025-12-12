package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.Expression;

public record Assignment(
        Span span,
        Expression target,
        Expression value
) implements Statement {
}
