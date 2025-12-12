package de.til7701.javelin.ast.methods;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.statement.Statement;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationUsage;

import java.util.List;
import java.util.Optional;

public record MethodDefinition(
        Span span,
        List<AnnotationUsage> annotations,
        List<MethodModifier> modifiers,
        Optional<Type> returnType,
        String name,
        MethodParameters parameters,
        Statement body
) implements Node {
}
