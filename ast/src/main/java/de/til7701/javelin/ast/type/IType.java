package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Span;

public record IType(
        Span span,
        int bitCount
) implements Type {
}
