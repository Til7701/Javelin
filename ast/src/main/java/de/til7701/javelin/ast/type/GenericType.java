package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Span;

public record GenericType(
        Span span,
        Type baseType,
        TypeList typeArguments
) implements Type {
}
