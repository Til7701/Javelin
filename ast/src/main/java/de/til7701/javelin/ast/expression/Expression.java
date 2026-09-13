package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.statement.Statement;

public sealed interface Expression extends Statement permits BinaryExpression, BooleanLiteralExpression, ConstructorCall, FloatLiteralExpression, InstanceMethodCall, LeftUnaryExpression, RightUnaryExpression, SignedIntegerLiteralExpression, StaticMethodCall, StringLiteralExpression, SymbolExpression, TypeCastExpression, UnsignedIntegerLiteralExpression {
}
