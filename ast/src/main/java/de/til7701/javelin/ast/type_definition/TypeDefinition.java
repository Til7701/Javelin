package de.til7701.javelin.ast.type_definition;

import de.til7701.javelin.ast.Ast;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationTypeDefinition;
import de.til7701.javelin.ast.type_definition.classes.ClassDefinition;
import de.til7701.javelin.ast.type_definition.enums.EnumTypeDefinition;

public sealed interface TypeDefinition extends Ast permits AnnotationTypeDefinition, ClassDefinition, EnumTypeDefinition {
}
