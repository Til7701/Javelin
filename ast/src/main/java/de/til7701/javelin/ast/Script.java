package de.til7701.javelin.ast;

import de.til7701.javelin.ast.statement.Statement;

import java.util.List;

public record Script(
        Span span,
        List<Statement> statements
) implements Ast {
}
