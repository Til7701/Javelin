package de.til7701.javelin.interpreter;

import module de.til7701.javelin.ast;
import module de.til7701.javelin.common;
import de.til7701.javelin.interpreter.natives.Io;
import de.til7701.javelin.interpreter.variable.Variable;
import de.til7701.javelin.interpreter.variable.VariableFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
public class Interpreter {

    private final Environment environment;
    private final ContextStack context = new ContextStack();
    private final VariableFactory variableFactory = new VariableFactory();

    public Interpreter(Environment environment) {
        this.environment = environment;

        Class<?>[] natives = {
                Io.class,
        };
        for (Class<?> clazz : natives) {
            environment.addJavaClass(clazz);
        }
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
        log.debug("Literals: {}", variableFactory.literalsToString());
    }

    private void executeStatement(Statement statement) {
        switch (statement) {
            case VariableInitialization(_, _, String name, Expression value) ->
                    context.initializeVariable(name, evaluateExpression(value).copyRef());
            case Assignment(_, Expression target, Expression value) -> {
                Variable t = evaluateExpression(target);
                t.set(evaluateExpression(value));
            }
            case ReturnStatement _ -> throw new NotImplementedException();
            case StatementList(_, List<Statement> statements) -> statements.forEach(this::executeStatement);
            case WhenStatement _ -> throw new NotImplementedException();
            case Expression e -> evaluateExpression(e);
        }
    }

    private Variable evaluateExpression(Expression expression) {
        return switch (expression) {
            case BooleanLiteralExpression(_, boolean value) -> variableFactory.fromBoolLiteral(value);
            case StringLiteralExpression(_, String value) -> variableFactory.fromStrLiteral(value);
            case SymbolExpression(_, String identifier) -> Objects.requireNonNull(context.getVariable(identifier));
            case InstanceMethodCall _ -> throw new NotImplementedException();
            case StaticMethodCall(_, Type type, String methodName, List<Expression> arguments) -> {
                Klass klass = environment.getKlassRegister().getKlass(type)
                        .orElseThrow(() -> new RuntimeException("Class not found for type: " + type));
                List<Variable> argumentValues = arguments.stream()
                        .map(this::evaluateExpression)
                        .toList();
                Type[] argumentTypes = argumentValues.stream()
                        .map(Variable::type)
                        .toArray(Type[]::new);
                Metod method = klass.getMethod(methodName, argumentTypes)
                        .orElseThrow(() -> new RuntimeException("Method: " + methodName + " with args: " + Arrays.deepToString(argumentTypes) + " not found for class: " + klass));
                return switch (method) {
                    case JavaMetod javaMetod -> {
                    }
                    case JavelinMetod javelinMetod -> {
                    }
                };
            }
            default -> throw new NotImplementedException(expression.toString());
        };
    }

}
