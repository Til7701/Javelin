package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;

import java.util.List;

public record StatementList(
        Span span,
        List<Statement> statements
) implements Statement {
}
