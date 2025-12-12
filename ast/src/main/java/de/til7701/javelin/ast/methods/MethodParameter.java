package de.til7701.javelin.ast.methods;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.Type;

public record MethodParameter(
        Span span,
        boolean mutable,
        Type type,
        String name
) implements Node {
}
