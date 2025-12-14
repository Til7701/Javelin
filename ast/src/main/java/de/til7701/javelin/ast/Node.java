package de.til7701.javelin.ast;

import de.til7701.javelin.ast.expression.Expression;
import de.til7701.javelin.ast.methods.MethodDefinition;
import de.til7701.javelin.ast.methods.MethodModifier;
import de.til7701.javelin.ast.methods.MethodParameter;
import de.til7701.javelin.ast.methods.MethodParameters;
import de.til7701.javelin.ast.statement.Statement;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.ast.type.TypeList;
import de.til7701.javelin.ast.type_definition.TypeModifier;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationFieldDefinition;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationUsage;
import de.til7701.javelin.ast.type_definition.classes.ClassFieldDefinition;
import de.til7701.javelin.ast.type_definition.classes.ConstructorDefinition;
import de.til7701.javelin.ast.type_definition.classes.FieldModifier;
import de.til7701.javelin.ast.type_definition.enums.EnumValueDefinition;

public sealed interface Node permits Ast, Expression, MethodDefinition, MethodModifier, MethodParameter, MethodParameters, Statement, Type, TypeList, TypeModifier, AnnotationFieldDefinition, AnnotationUsage, ClassFieldDefinition, ConstructorDefinition, FieldModifier, EnumValueDefinition {

    Span span();

}
