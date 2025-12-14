package de.til7701.javelin.ast.type_definition.classes;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;

public record FieldModifier(
        Span span,
        FieldModifierValue value
) implements Node {
}
