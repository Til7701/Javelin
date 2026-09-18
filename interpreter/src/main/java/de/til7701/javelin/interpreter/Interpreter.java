package de.til7701.javelin.interpreter;

import module de.til7701.javelin.ast;
import module de.til7701.javelin.common;
import de.til7701.javelin.interpreter.natives.Io;
import de.til7701.javelin.interpreter.variable.Variable;
import de.til7701.javelin.interpreter.variable.VariableFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class Interpreter {

    private final Environment environment;
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
        ContextStack context = new ContextStack();
        context.push(new Context());
        switch (ast) {
            case Script script -> {
                List<Statement> statements = script.statements();
                statements.forEach(statement -> executeStatement(statement, context));
            }
            case TypeDefinition _ -> throw new NotImplementedException();
        }
        log.debug("Context: {}", context);
        log.debug("Literals: {}", variableFactory.literalsToString());
    }

    void executeStatement(Statement statement, ContextStack context) {
        switch (statement) {
            case VariableInitialization(_, _, String name, Expression value) ->
                    context.initializeVariable(name, evaluateExpression(value, context));
            case Assignment(_, Expression target, Expression value) -> {
                Variable t = evaluateExpression(target, context);
                t.set(evaluateExpression(value, context));
            }
            case ReturnStatement _ -> throw new NotImplementedException();
            case StatementList(_, List<Statement> statements) -> statements.forEach(s -> executeStatement(s, context));
            case WhenStatement(_, boolean evalInstantly, Expression condition, Statement body) -> {
                Set<Variable> topVariables = topVariables(condition, context);
                ContextStack snapshot = context.snapshot();
                When when = new When(this, snapshot, condition, body, topVariables);
                if (evalInstantly) when.eval();
            }
            case Expression e -> evaluateExpression(e, context);
        }
    }

    Variable evaluateExpression(Expression expression, ContextStack context) {
        return switch (expression) {
            case NewExpression(_, Expression e) -> evaluateExpression(e, context).createNew();
            case BooleanLiteralExpression(_, boolean value) -> variableFactory.fromBoolLiteral(value);
            case StringLiteralExpression(_, String value) -> variableFactory.fromStrLiteral(value);
            case SymbolExpression(_, String identifier) -> Objects.requireNonNull(context.getVariable(identifier));
            case InstanceMethodCall _ -> throw new NotImplementedException();
            case StaticMethodCall(_, Type type, String methodName, List<Expression> arguments) -> {
                Klass klass = environment.getKlassRegister().getKlass(type)
                        .orElseThrow(() -> new RuntimeException("Class not found for type: " + type));
                List<Variable> argumentValues = arguments.stream()
                        .map((Expression e) -> evaluateExpression(e, context))
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
                Variable leftV = evaluateExpression(left, context);
                Variable rightV = evaluateExpression(right, context);
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
            case ArrayLiteralCreation(_, List<Expression> values) -> {
                Variable[] variables = values.stream()
                        .map((Expression e) -> evaluateExpression(e, context))
                        .toArray(Variable[]::new);
                Type elementType = variables[0].type();
                yield variableFactory.asArray(variables, elementType);
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

    private Set<Variable> topVariables(Expression expression, ContextStack context) {
        return switch (expression) {
            case SymbolExpression(_, String name) -> Set.of(Objects.requireNonNull(context.getVariable(name)));
            case NewExpression _,
                 BooleanLiteralExpression _,
                 StringLiteralExpression _ -> Set.of(evaluateExpression(expression, context));
            case BinaryExpression(_, Expression left, _, Expression right) -> {
                Collection<Variable> l = topVariables(left, context);
                Collection<Variable> r = topVariables(right, context);
                Set<Variable> result = new HashSet<>();
                result.addAll(l);
                result.addAll(r);
                yield result;
            }
            default -> throw new NotImplementedException(expression.toString());
        };
    }

}
