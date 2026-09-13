package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.Expression;
import org.jspecify.annotations.Nullable;

public record ReturnStatement(
        Span span,
        @Nullable Expression value
) implements Statement {
}
