package de.til7701.javelin.ast.statement;

import de.til7701.javelin.ast.Span;
import de.til7701.javelin.ast.expression.Expression;
import de.til7701.javelin.ast.type.Type;

public record ForeachStatement(
        Span span,
        Type elementType,
        String elementName,
        Expression iterable,
        Statement body
) implements ForStatement {
}
