package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Span;

import java.util.List;

public record GenericType(
        Span span,
        Type baseType,
        List<Type> typeArguments
) implements Type {
}
