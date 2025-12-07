package de.til7701.continu_num.core.ast;

public record Assignment(
        String name,
        Expression value
) implements Instruction {
}
