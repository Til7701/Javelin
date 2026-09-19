package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;

import java.util.List;
import java.util.function.Function;

public record TypeList(
        Span span,
        List<Type> types
) implements Node {

    public TypeList mapNames(Function<String, String> nameMapper) {
        return new TypeList(
                span,
                types.stream().map(type -> type.mapNames(nameMapper)).toList()
        );
    }

}
