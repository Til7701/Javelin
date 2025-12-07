package de.til7701.continu_num.core.ast;

import java.util.List;
import java.util.Optional;

public record MethodCall(
        Optional<String> typeName,
        String methodName,
        List<Expression> arguments
) implements Expression, Instruction {
}
