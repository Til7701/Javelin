package de.til7701.javelin.ast;

import de.til7701.javelin.ast.type_definition.TypeDefinition;

public sealed interface Ast extends Node permits Script, TypeDefinition {
}
