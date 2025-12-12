package de.til7701.javelin.ast.methods;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;

import java.util.List;

public record MethodParameters(
        Span span,
        List<MethodParameter> parameters
) implements Node {
}
