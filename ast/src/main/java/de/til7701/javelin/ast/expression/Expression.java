package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Node;

public sealed interface Expression extends Node permits BinaryExpression, BooleanLiteralExpression, CollectionCreationExpression, InstanceMethodCall, IntegerLiteralExpression, LeftUnaryExpression, RightUnaryExpression, StaticMethodCall, StringLiteralExpression, SymbolExpression {
}
