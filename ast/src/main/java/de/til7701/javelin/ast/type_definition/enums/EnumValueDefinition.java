package de.til7701.javelin.ast.type_definition.enums;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;

public record EnumValueDefinition(
        Span span,
        String name
) implements Node {
}
