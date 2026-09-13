package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.expression.ConstructorCall;
import de.til7701.javelin.ast.expression.Expression;
import de.til7701.javelin.ast.expression.InstanceMethodCall;
import de.til7701.javelin.ast.expression.StaticMethodCall;

public sealed interface Statement extends Node permits ConstructorCall, Expression, InstanceMethodCall, StaticMethodCall, Assignment, ReturnStatement, StatementList, VariableInitialization, WhenStatement {
}
