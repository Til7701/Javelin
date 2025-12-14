package de.til7701.javelin.ast.type_definition.classes;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.methods.MethodModifier;
import de.til7701.javelin.ast.methods.MethodParameters;
import de.til7701.javelin.ast.statement.Statement;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationUsage;

import java.util.List;

public record ConstructorDefinition(
        Span span,
        List<AnnotationUsage> annotations,
        List<MethodModifier> modifiers,
        MethodParameters parameters,
        Statement body
) implements Node {
}
