package de.til7701.javelin.ast.type_definition;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;

public record TypeModifier(
        Span span,
        TypeModifierValue value
) implements Node {
}
