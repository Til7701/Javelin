package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;
import org.jspecify.annotations.Nullable;

public record Import(
        Span span,
        String typeToImport,
        @Nullable String asName
) implements Statement {
}
