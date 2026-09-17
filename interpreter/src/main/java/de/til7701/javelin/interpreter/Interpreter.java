package de.til7701.javelin.interpreter;

import module de.til7701.javelin.ast;
import module de.til7701.javelin.common;
import de.til7701.javelin.interpreter.natives.Io;
import de.til7701.javelin.interpreter.variable.Variable;
import de.til7701.javelin.interpreter.variable.VariableFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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
                yield switch (method) {
                    case JavaMetod javaMetod -> executeJavaMethod(javaMetod, argumentValues);
                    case JavelinMetod javelinMetod -> executeJavelinMethod(javelinMetod, argumentValues);
                };
            }
            case BinaryExpression(_, Expression left, BinaryOperator binaryOperator, Expression right) -> {
                Variable leftV = evaluateExpression(left);
                Variable rightV = evaluateExpression(right);
                String methodName = binaryOperator.name().toLowerCase(Locale.ROOT);
                Type type = leftV.type();
                Klass klass = environment.getKlassRegister().getKlass(type)
                        .orElseThrow(() -> new RuntimeException("Class not found for type: " + type));
                Type[] argumentTypes = {leftV.type(), rightV.type()};
                Metod method = klass.getMethod(methodName, argumentTypes)
                        .orElseThrow(() -> new RuntimeException("Method: " + methodName + " with args: " + Arrays.deepToString(argumentTypes) + " not found for class: " + klass));
                yield switch (method) {
                    case JavaMetod javaMetod -> executeJavaMethod(javaMetod, List.of(leftV, rightV));
                    case JavelinMetod javelinMetod -> executeJavelinMethod(javelinMetod, List.of(leftV, rightV));
                };
            }
            default -> throw new NotImplementedException(expression.toString());
        };
    }

    private Variable executeJavaMethod(JavaMetod javaMetod, List<Variable> argumentValues) {
        Class<?> clazz = javaMetod.javaClass();
        Object result;
        try {
            Method method = clazz.getMethod(javaMetod.name(), javaMetod.javaParameterClasses());
            if (javaMetod.isStatic()) {
                result = method.invoke(null, argumentValues.stream().map(Variable::javaValue).toArray());
            } else {
                result = null;
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return variableFactory.fromJavaValue(result);
    }

    private Variable executeJavelinMethod(JavelinMetod javelinMetod, List<Variable> argumentValues) {
        throw new NotImplementedException();
    }

}
