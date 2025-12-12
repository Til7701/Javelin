package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.Expression;

import java.util.Optional;

public record IfStatement(
        Span span,
        Expression condition,
        Statement thenBranch,
        Optional<Statement> elseBranch
) implements Statement {
}
