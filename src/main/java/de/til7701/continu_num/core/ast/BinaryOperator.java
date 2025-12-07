package de.til7701.continu_num.core.ast;

import lombok.Getter;

public enum BinaryOperator {

    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    @Getter
    private final String symbol;

    BinaryOperator(String symbol) {
        this.symbol = symbol;
    }

    public static BinaryOperator fromString(String operator) {
        return switch (operator) {
            case "+" -> ADD;
            case "-" -> SUBTRACT;
            case "*" -> MULTIPLY;
            case "/" -> DIVIDE;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
