package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;

import java.util.List;

public record TypeList(
        Span span,
        List<Type> types
) implements Node {
}
