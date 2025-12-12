package de.til7701.javelin.ast.expression;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.statement.Statement;

import java.util.List;
import java.util.Optional;

public record StaticMethodCall(
        Span span,
        Optional<String> typeName,
        String methodName,
        List<Expression> arguments
) implements Expression, Statement {
}
