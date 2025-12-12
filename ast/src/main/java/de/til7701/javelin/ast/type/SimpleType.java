package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Span;

public record SimpleType(
        Span span,
        String name
) implements Type {
}
