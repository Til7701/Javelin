package de.til7701.javelin.checker;

import de.til7701.javelin.ast.Ast;
import de.til7701.javelin.ast.expression.ConstructorCall;
import de.til7701.javelin.ast.expression.Expression;
import de.til7701.javelin.ast.expression.InstanceMethodCall;
import de.til7701.javelin.ast.expression.StaticMethodCall;
import de.til7701.javelin.ast.statement.*;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.environment.Environment;
import de.til7701.javelin.klass.JavaMetod;
import de.til7701.javelin.klass.Klass;
import de.til7701.javelin.klass.KlassRegister;
import de.til7701.javelin.klass.Metod;
import de.til7701.javelin.operation.OperationsRegister;

import java.util.Arrays;
import java.util.List;

public class TypeChecker {

    private final OperationsRegister operationsRegister;
    private final KlassRegister klassRegister;

    private ScopeStack context;

    public TypeChecker(Environment environment) {
        this.operationsRegister = environment.getOperationsRegister();
        this.klassRegister = environment.getKlassRegister();
    }

    public void check(Ast ast) {
        context = new ScopeStack();
        context.push(new Scope());

    }

    private void check(Statement statement) {
        switch (statement) {
            case VariableInitialization variableInitialization -> checkSymbolInitialization(variableInitialization);
            case StaticMethodCall staticMethodCall -> checkStaticMethodCall(staticMethodCall);
            case Assignment assignment -> checkAssignment(assignment);
            case StatementList statementList -> {
                context.push(new Scope());
                statementList.statements().forEach(this::check);
                context.pop();
            }
            case WhenStatement whenStatement -> {
                Expression condition = whenStatement.condition();
                Type conditionType = evaluateExpressionType(condition);
                check(whenStatement.body());
            }
            case InstanceMethodCall instanceMethodCall -> checkInstanceMethodCall(instanceMethodCall);
            case ConstructorCall constructorCall -> {
            }
            case BreakStatement breakStatement -> {
            }
            case ContinueStatement continueStatement -> {
            }
            case ForStatement forStatement -> {
            }
            case IfStatement ifStatement -> {
            }
            case ReturnStatement returnStatement -> {
            }
            case Expression expression -> {
            }
        }
    }

    private void checkAssignment(Assignment assignment) {
    }

    private Type checkStaticMethodCall(StaticMethodCall staticMethodCall) {
        Klass klass = klassRegister.getKlass(staticMethodCall.type().orElseThrow()).orElseThrow();

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


    private void checkSymbolInitialization(VariableInitialization variableInitialization) {
        Type symbolType = variableInitialization.type();
        Type expressionType = evaluateExpressionType(variableInitialization.value());
        context.initializeVariable(variableInitialization.name(), symbolType);
    }

    private Type evaluateExpressionType(Expression value) {
        return null;
    }

}
