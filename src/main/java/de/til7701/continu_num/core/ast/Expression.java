package de.til7701.continu_num.core.ast;

public sealed interface Expression extends Node permits BinaryExpression, BooleanLiteralExpression, IntegerLiteralExpression, MethodCall, StringLiteralExpression, SymbolExpression {
}
