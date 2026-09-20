package de.til7701.javelin.interpreter;

import module de.til7701.javelin.ast;
import module de.til7701.javelin.common;
import de.til7701.javelin.interpreter.variable.Variable;
import de.til7701.javelin.interpreter.variable.VariableFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class Interpreter {

    private final Environment environment;
    private final VariableFactory variableFactory = new VariableFactory();
    private final Deque<Interrupt> interruptQueue = new ArrayDeque<>();
    private boolean executingInterrupt = false;

    private Imports currentImports;

    public Interpreter(Environment environment) {
        this.environment = environment;
        resetImports();
    }

    private void resetImports() {
        currentImports = environment.getPrimitiveImports();
    }

    public void interpret(Ast ast) {
        Stack context = new Stack();
        context.push(new StackFrame());
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

    private void executeStatement(Statement statement, Stack context) {
        handleInterrupts();
        switch (statement) {
            case Import imp -> currentImports.addImport(imp);
            case VariableInitialization(_, String name, Expression value) ->
                    context.initializeVariable(name, evaluateExpression(value, context));
            case Assignment(_, Expression target, Expression value) -> {
                Variable t = evaluateExpression(target, context);
                t.set(evaluateExpression(value, context));
            }
            case ReturnStatement _ -> throw new NotImplementedException();
            case StatementList(_, List<Statement> statements) -> statements.forEach(s -> executeStatement(s, context));
            case WhenStatement(_, boolean evalInstantly, Expression condition, Statement body) -> {
                Set<Variable> topVariables = topVariables(condition, context);
                Stack stackSnapshot = context.snapshot();
                Imports importsSnapshot = currentImports.snapshot();
                When when = new When(this, stackSnapshot, importsSnapshot, condition, body, topVariables);
                if (evalInstantly) when.eval();
            }
            case Expression e -> evaluateExpression(e, context);
        }
    }

    Variable evaluateExpression(Expression expression, Stack context) {
        return switch (expression) {
            case NewExpression(_, Expression e) -> evaluateExpression(e, context).createNew();
            case BooleanLiteralExpression(_, boolean value) -> variableFactory.fromBoolLiteral(value);
            case SignedIntegerLiteralExpression(_, long value, long bitCount) ->
                    variableFactory.fromILiteral(value, (int) bitCount);
            case UnsignedIntegerLiteralExpression(_, long value, long bitCount) ->
                    variableFactory.fromULiteral(value, (int) bitCount);
            case StringLiteralExpression(_, String value) -> variableFactory.fromStrLiteral(value);
            case SymbolExpression(_, String identifier) -> Objects.requireNonNull(context.getVariable(identifier));
            case InstanceMethodCall _ -> throw new NotImplementedException();
            case StaticMethodCall(_, Type type, String methodName, List<Expression> arguments) -> {
                Klass klass = environment.getKlassRegister().getKlass(type, currentImports)
                        .orElseThrow(() -> new RuntimeException("Class not found for type: " + type));
                List<Variable> argumentValues = arguments.stream()
                        .map((Expression e) -> evaluateExpression(e, context))
                        .toList();
                Type[] argumentTypes = argumentValues.stream()
                        .map(Variable::type)
                        .toArray(Type[]::new);
                Metod method = klass.getMethod(methodName, argumentTypes, environment)
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
                Klass klass = environment.getKlassRegister().getKlass(type, currentImports)
                        .orElseThrow(() -> new RuntimeException("Class not found for type: " + type));
                Type[] argumentTypes = {leftV.type(), rightV.type()};
                Metod method = klass.getMethod(methodName, argumentTypes, environment)
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
        Object result;
        try {
            Method method = javaMetod.javaMethod();
            Object instance = null;
            if (!javaMetod.isStatic()) {
                // TODO
            }
            log.debug(javaMetod.toString());
            if (javaMetod.needsNatives())
                result = method.invoke(instance, Stream.concat(Stream.of(environment.getNatives()), argumentValues.stream().map(Variable::javaValue)).toArray());
            else
                result = method.invoke(instance, argumentValues.stream().map(Variable::javaValue).toArray());
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return variableFactory.fromJavaValue(result);
    }

    private Variable executeJavelinMethod(JavelinMetod javelinMetod, List<Variable> argumentValues) {
        throw new NotImplementedException();
    }

    private Set<Variable> topVariables(Expression expression, Stack context) {
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

    void interrupt(Interrupt item) {
        this.interruptQueue.addLast(item);
    }

    private void handleInterrupts() {
        if (!executingInterrupt) {
            while (!interruptQueue.isEmpty()) {
                Interrupt interrupt = this.interruptQueue.pollFirst();
                Imports oldImports = currentImports;
                currentImports = interrupt.imports();
                executingInterrupt = true;
                executeStatement(interrupt.unterbrechungsbehandlungsprozedur(), interrupt.stack());
                executingInterrupt = false;
                currentImports = oldImports;
            }
        }
    }

}
