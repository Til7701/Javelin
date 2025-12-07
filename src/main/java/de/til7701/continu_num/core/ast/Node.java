package de.til7701.continu_num.core.ast;

public sealed interface Node permits
        ContinuNumFile,
        Expression,
        Instruction {
}
