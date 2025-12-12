package de.til7701.javelin.ast.type_definition.annotations;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type_definition.TypeDefinition;
import de.til7701.javelin.ast.type_definition.TypeModifier;

import java.util.List;

public record AnnotationTypeDefinition(
        Span span,
        List<TypeModifier> modifiers,
        List<AnnotationFieldDefinition> fields
) implements TypeDefinition {
}
