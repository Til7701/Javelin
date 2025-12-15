package de.til7701.javelin.interpreter;

import de.til7701.javelin.core.ast.*;
import de.til7701.javelin.core.environment.Environment;
import de.til7701.javelin.core.reflect.*;
import de.til7701.javelin.interpreter.variables.BoolVariable;
import de.til7701.javelin.interpreter.variables.CollectionVariable;
import de.til7701.javelin.interpreter.variables.I32Variable;
import de.til7701.javelin.interpreter.variables.StrVariable;
import de.til7701.javelin.klass.KlassRegister;
import de.til7701.javelin.operation.OperationsRegister;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class Interpreter {

    private final VariableFactory variableFactory = new VariableFactory();
    private final Operations operations = new Operations(variableFactory);

    private final KlassRegister klassRegister;
    private final OperationsRegister operationsRegister;

    private final ContextStack context = new ContextStack();

    public Interpreter(Environment environment) {
        this.klassRegister = environment.getKlassRegister();
        this.operationsRegister = environment.getOperationsRegister();
    }

    public void interpret(Ast ast) {
        context.push(new Context());
        for (Instruction instruction : ast.instructions()) {
            execute(instruction);
        }
    }

    private void execute(Instruction instruction) {
        switch (instruction) {
            case SymbolInitialization symbolInitialization -> executeSymbolInitialization(symbolInitialization);
            case StaticMethodCall staticMethodCall -> executeStaticMethodCall(staticMethodCall);
            case Assignment assignment -> executeAssignment(assignment);
            case InstructionList instructionList -> {
                context.push(new Context());
                for (Instruction instr : instructionList.instructions()) {
                    execute(instr);
                }
                context.pop();
            }
            case WhileStatement whileStatement -> {
                while (true) {
                    Variable conditionValue = evaluateExpression(whileStatement.condition());
                    if (!(conditionValue instanceof BoolVariable boolVar)) {
                        throw new RuntimeException("While condition must evaluate to a Bool");
                    }
                    if (!boolVar.value()) {
                        break;
                    }
                    execute(whileStatement.body());
                }
            }
            case InstanceMethodCall instanceMethodCall -> executeInstanceMethodCall(instanceMethodCall);
        }
    }

    private void executeSymbolInitialization(SymbolInitialization symbolInitialization) {
        String name = symbolInitialization.name();
        Variable value = evaluateExpression(symbolInitialization.value());
        if (symbolInitialization.isMutable()) {
            value = value.asMutable();
        }

        Type type = symbolInitialization.type();
        if (!type.isAssignableFrom(value.type())) {
            throw new RuntimeException("Type mismatch: cannot assign " + value.type() + " to " + type);
        }
        if (!type.equals(value.type())) { // avoid with strict types and explicit casts with new language feature
            value = new VariableFactory().cast(value, type);
        }

        context.initializeVariable(name, value);
    }

    private void executeAssignment(Assignment assignment) {
        String name = assignment.name();
        Variable value = evaluateExpression(assignment.value());
        Variable previousValue = context.getVariable(name);
        if (previousValue == null) {
            throw new RuntimeException("Variable " + name + " is not initialized");
        }
        if (previousValue.isMutable()) {
            if (!previousValue.type().isAssignableFrom(value.type())) {
                throw new RuntimeException("Type mismatch: cannot assign " + value.type() + " to " + previousValue.type());
            }
            if (!previousValue.type().equals(value.type())) { // avoid with strict types and explicit casts with new language feature
                value = variableFactory.cast(value, previousValue.type());
            }
            context.updateVariable(name, value.asMutable());
        } else {
            throw new RuntimeException("Variable " + name + " is not mutable");
        }
    }

    private Variable executeStaticMethodCall(StaticMethodCall staticMethodCall) {
        if (staticMethodCall.typeName().isPresent()) {
            String typeName = staticMethodCall.typeName().get();
            String methodName = staticMethodCall.methodName();
            Klass klass = klassRegister.getKlass(typeName).orElseThrow();
            Variable[] args = staticMethodCall.arguments().stream()
                    .map(this::evaluateExpression)
                    .toArray(Variable[]::new);

            Type[] argTypes = Arrays.stream(args)
                    .map(Variable::type)
                    .toArray(Type[]::new);
            Metod metod = klass.getMethod(methodName, argTypes).orElseThrow();

            if (metod instanceof JavaMetod javaMetod) {
                try {
                    Object[] javaArgs = Arrays.stream(args)
                            .map(Variable::value)
                            .toArray();

                    Class<?> declaringClass = javaMetod.javaClass();
                    Method method = declaringClass.getDeclaredMethod(methodName, javaMetod.javaParameterClasses());
                    Object result = method.invoke(null, javaArgs);
                    Type returnType = javaMetod.returnType();
                    return variableFactory.createVariableFromJavaObject(returnType, result);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to invoke method " + methodName + " of class " + typeName, e);
                }
            }
        } else {
            throw new UnsupportedOperationException();
        }
        return null;
    }

    private Variable executeInstanceMethodCall(InstanceMethodCall instanceMethodCall) {
        Expression instanceExpr = instanceMethodCall.instance();
        Variable instanceVar = evaluateExpression(instanceExpr);
        String methodName = instanceMethodCall.methodName();
        Variable[] args = instanceMethodCall.arguments().stream()
                .map(this::evaluateExpression)
                .toArray(Variable[]::new);

        Type instanceType = instanceVar.type();
        Klass klass = klassRegister.getKlass(instanceType.toString()).orElseThrow();
        Type[] argTypes = Arrays.stream(args)
                .map(Variable::type)
                .toArray(Type[]::new);
        Metod metod = klass.getMethod(methodName, argTypes).orElseThrow();

        if (metod instanceof JavaMetod javaMetod) {
            try {
                Object[] javaArgs = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    javaArgs[i] = args[i].value();
                }

                Class<?> declaringClass = javaMetod.javaClass();
                Method method = declaringClass.getDeclaredMethod(methodName, javaMetod.javaParameterClasses());
                Object result = method.invoke(instanceVar, javaArgs);
                Type returnType = javaMetod.returnType();
                return variableFactory.createVariableFromJavaObject(returnType, result);
            } catch (Exception e) {
                throw new RuntimeException("Failed to invoke method " + methodName + " of class " + instanceType, e);
            }
        }
        return null;
    }

    private Variable evaluateExpression(Expression expression) {
        return switch (expression) {
            case BooleanLiteralExpression _ -> throw new UnsupportedOperationException();
            case IntegerLiteralExpression(String value) -> new I32Variable(Integer.parseInt(value));
            case StringLiteralExpression(String value) -> new StrVariable(value);
            case StaticMethodCall staticMethodCall -> executeStaticMethodCall(staticMethodCall);
            case SymbolExpression(String identifier) -> context.getVariable(identifier);
            case BinaryExpression(Expression left, BinaryOperator operator, Expression right) -> {
                Variable leftValue = evaluateExpression(left);
                Variable rightValue = evaluateExpression(right);
                Operation operation = operationsRegister.getBinaryOperation(
                        operator,
                        leftValue.type(),
                        rightValue.type()
                ).orElseThrow(() -> new RuntimeException("Operation " + operator.getSymbol() +
                        " not found for types " + leftValue.type() + " and " + rightValue.type()));
                yield operations.invokeBinary(operation, leftValue, rightValue);
            }
            case InstanceMethodCall instanceMethodCall -> executeInstanceMethodCall(instanceMethodCall);
            case CollectionCreationExpression(List<Expression> elements) -> new CollectionVariable(false,
                    elements.stream()
                            .map(this::evaluateExpression)
                            .toList()
            );
        };
    }

}
