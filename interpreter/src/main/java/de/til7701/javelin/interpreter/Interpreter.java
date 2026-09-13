package de.til7701.javelin.interpreter;

import module de.til7701.javelin.ast;
import module de.til7701.javelin.common;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Slf4j
public class Interpreter {

    private final ContextStack context = new ContextStack();

    public Interpreter(Environment environment) {
    }

    public void interpret(Ast ast) {
        context.push(new Context());
        switch (ast) {
            case Script script -> {
                List<Statement> statements = script.statements();
                statements.forEach(this::executeStatement);
            }
            case TypeDefinition _ -> throw new NotImplementedException();
        }
        log.debug("Context: {}", context);
    }

    private void executeStatement(Statement statement) {
        switch (statement) {
            case VariableInitialization(_, _, String name, Expression value) ->
                    context.initializeVariable(name, evaluateExpression(value));
            case Assignment(_, Expression target, Expression value) -> {
                Variable t = evaluateExpression(target);
                t.set(evaluateExpression(value));
            }
            case ConstructorCall _ -> throw new NotImplementedException();
            case InstanceMethodCall _ -> throw new NotImplementedException();
            case StaticMethodCall _ -> throw new NotImplementedException();
            case ReturnStatement _ -> throw new NotImplementedException();
            case StatementList _ -> throw new NotImplementedException();
            case WhenStatement _ -> throw new NotImplementedException();
            case Expression e -> evaluateExpression(e);
        }
    }

    private Variable evaluateExpression(Expression expression) {
        return switch (expression) {
            case BooleanLiteralExpression(_, boolean value) -> new BooleanVariable(value);
            case StringLiteralExpression(_, String value) -> new StringVariable(value);
            case SymbolExpression(_, String identifier) -> Objects.requireNonNull(context.getVariable(identifier));
            default -> throw new NotImplementedException(expression.toString());
        };
    }

}
