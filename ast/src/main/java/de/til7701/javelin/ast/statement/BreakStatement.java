package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;

public record BreakStatement(
        Span span
) implements Statement {
}
