package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.statement.Statement;

public sealed interface Expression extends Statement permits ArrayLiteralCreation, BinaryExpression, BooleanLiteralExpression, ConstructorCall, FloatLiteralExpression, InstanceMethodCall, LeftUnaryExpression, NewExpression, RightUnaryExpression, SignedIntegerLiteralExpression, StaticMethodCall, StringLiteralExpression, SymbolExpression, TypeCastExpression, UnsignedIntegerLiteralExpression {
}
