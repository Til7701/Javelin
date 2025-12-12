package de.til7701.javelin.ast.type_definition.annotations;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.statement.Assignment;
import de.til7701.javelin.ast.type.Type;

import java.util.List;

public record AnnotationUsage(
        Span span,
        Type annotationType,
        List<Assignment> values
) implements Node {
}
