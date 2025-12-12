package de.til7701.javelin.type_checker;

import de.til7701.javelin.core.ast.*;
import de.til7701.javelin.core.environment.Environment;
import de.til7701.javelin.core.reflect.*;

import java.util.Arrays;
import java.util.List;

public class TypeChecker {

    private final OperationsRegister operationsRegister;
    private final KlassRegister klassRegister;

    private ContextStack context;

    public TypeChecker(Environment environment) {
        this.operationsRegister = environment.getOperationsRegister();
        this.klassRegister = environment.getKlassRegister();
    }

    public void check(Ast ast) {
        context = new ContextStack();
        context.push(new Context());

        List<Instruction> instructions = ast.instructions();
        for (Instruction instruction : instructions) {
            check(instruction);
        }
    }

    private void check(Instruction instruction) {
        switch (instruction) {
            case SymbolInitialization symbolInitialization -> checkSymbolInitialization(symbolInitialization);
            case StaticMethodCall staticMethodCall -> checkStaticMethodCall(staticMethodCall);
            case Assignment assignment -> checkAssignment(assignment);
            case InstructionList instructionList -> {
                context.push(new Context());
                instructionList.instructions().forEach(this::check);
                context.pop();
            }
            case WhileStatement whileStatement -> {
                Expression condition = whileStatement.condition();
                Type conditionType = evaluateExpressionType(condition);
                if (!conditionType.equals(Bool.instance())) {
                    throw new RuntimeException("Type mismatch: while condition must be of type Bool, but found " + conditionType);
                }
                check(whileStatement.body());
            }
            case InstanceMethodCall instanceMethodCall -> checkInstanceMethodCall(instanceMethodCall);
        }
    }

    private void checkAssignment(Assignment assignment) {
        Type expressionType = evaluateExpressionType(assignment.value());
        Type variableType = context.getVariable(assignment.name());
        if (!variableType.isAssignableFrom(expressionType)) {
            throw new RuntimeException("Type mismatch: cannot assign " + expressionType + " to " + variableType);
        }
    }

    private Type checkStaticMethodCall(StaticMethodCall staticMethodCall) {
        Klass klass = klassRegister.getKlass(staticMethodCall.typeName().orElseThrow()).orElseThrow();

        Type[] parameterTypes = staticMethodCall.arguments().stream()
                .map(this::evaluateExpressionType)
                .toArray(Type[]::new);

        Metod metod = klass.getMethod(staticMethodCall.methodName(), parameterTypes).orElseThrow(() ->
                new RuntimeException("Method " + staticMethodCall.methodName() + "(" + String.join(", ", Arrays.stream(parameterTypes)
                        .map(Type::toString)
                        .toArray(String[]::new)) + ") not found in class " + klass.name())
        );
        if (!(metod instanceof JavaMetod)) {
            throw new UnsupportedOperationException();
        }
        return metod.returnType();
    }

    private Type checkInstanceMethodCall(InstanceMethodCall instanceMethodCall) {
        Expression instance = instanceMethodCall.instance();
        String methodName = instanceMethodCall.methodName();
        List<Expression> arguments = instanceMethodCall.arguments();

        Type instanceType = evaluateExpressionType(instance);
        Klass klass = klassRegister.getKlass(instanceType.toString()).orElseThrow();
        Type[] parameterTypes = arguments.stream()
                .map(this::evaluateExpressionType)
                .toArray(Type[]::new);
        Metod metod = klass.getMethod(methodName, parameterTypes).orElseThrow(() ->
                new RuntimeException("Method " + methodName + "(" + String.join(", ", Arrays.stream(parameterTypes)
                        .map(Type::toString)
                        .toArray(String[]::new)) + ") not found in class " + klass.name())
        );
        if (!(metod instanceof JavaMetod)) {
            throw new UnsupportedOperationException();
        }
        return metod.returnType();
    }


    private void checkSymbolInitialization(SymbolInitialization symbolInitialization) {
        Type symbolType = symbolInitialization.type();
        Type expressionType = evaluateExpressionType(symbolInitialization.value());
        if (!symbolType.isAssignableFrom(expressionType)) {
            throw new RuntimeException("Type mismatch: cannot assign " + expressionType + " to " + symbolType);
        }
        context.initializeVariable(symbolInitialization.name(), symbolType);
    }

    private Type evaluateExpressionType(Expression value) {
        return switch (value) {
            case BooleanLiteralExpression _ -> throw new UnsupportedOperationException();
            case IntegerLiteralExpression _ -> I32.instance();
            case StringLiteralExpression _ -> Str.instance();
            case StaticMethodCall staticMethodCall -> checkStaticMethodCall(staticMethodCall);
            case BinaryExpression(Expression left, BinaryOperator operator, Expression right) -> {
                Type leftType = evaluateExpressionType(left);
                Type rightType = evaluateExpressionType(right);
                Operation operation = operationsRegister.getBinaryOperation(operator, leftType, rightType)
                        .orElseThrow(() -> new RuntimeException("Operation " + operator +
                                " not found for types " + leftType + " and " + rightType));
                yield operation.resultType();
            }
            case SymbolExpression(String identifier) -> context.getVariable(identifier);
            case InstanceMethodCall instanceMethodCall -> checkInstanceMethodCall(instanceMethodCall);
            case CollectionCreationExpression(List<Expression> elements) -> {
                if (elements.isEmpty()) {
                    yield Any.instance();
                }
                Type indexType = I32.instance();
                Type elementType = evaluateExpressionType(elements.getFirst());
                for (Expression element : elements) {
                    Type currentType = evaluateExpressionType(element);
                    if (!elementType.equals(currentType)) {
                        throw new RuntimeException("Type mismatch in collection elements: expected " + elementType + " but found " + currentType);
                    }
                }
                yield Collection.instance(indexType, elementType);
            }
        };
    }

}
