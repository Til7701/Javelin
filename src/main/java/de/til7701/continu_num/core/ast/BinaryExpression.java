package de.til7701.continu_num.core.ast;

public record BinaryExpression(
        Expression left,
        BinaryOperator operator,
        Expression right
) implements Expression {
}
