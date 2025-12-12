package de.til7701.javelin.ast.methods;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;

public record MethodModifier(
        Span span,
        MethodModifierValue value
) implements Node {
}
