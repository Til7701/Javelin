package de.til7701.javelin.klass;

import de.til7701.javelin.ast.type.Type;

public record AnnotationField(
        String name,
        Type type
) {
}
