package de.til7701.javelin.ast.type_definition.enums;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.type_definition.TypeDefinition;
import de.til7701.javelin.ast.type_definition.TypeModifier;

import java.util.List;

public record EnumTypeDefinition(
        Span span,
        List<TypeModifier> modifiers,
        List<EnumValueDefinition> values
) implements TypeDefinition {
}
