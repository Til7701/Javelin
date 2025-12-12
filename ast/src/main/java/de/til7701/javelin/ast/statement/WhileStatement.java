package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.Expression;

public record WhileStatement(
        Span span,
        Expression condition,
        Statement body
) implements Statement {
}
