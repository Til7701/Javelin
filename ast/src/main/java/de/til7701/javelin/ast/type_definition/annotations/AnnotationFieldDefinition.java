package de.til7701.javelin.ast.type_definition.annotations;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type.Type;

public record AnnotationFieldDefinition(
        Span span,
        Type type,
        String name
) implements Node {
}
