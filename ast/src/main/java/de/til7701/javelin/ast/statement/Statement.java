package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Node;
import de.til7701.javelin.ast.expression.InstanceMethodCall;
import de.til7701.javelin.ast.expression.StaticMethodCall;

public sealed interface Statement extends Node permits InstanceMethodCall, StaticMethodCall, Assignment, BreakStatement, ContinueStatement, ForStatement, IfStatement, ReturnStatement, StatementList, VariableInitialization, WhileStatement {
}
