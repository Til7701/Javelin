package de.til7701.continu_num.interpreter;

import de.til7701.continu_num.core.ast.*;
import de.til7701.continu_num.core.environment.Environment;
import de.til7701.continu_num.core.reflect.*;
import de.til7701.continu_num.interpreter.variables.I32Variable;
import de.til7701.continu_num.interpreter.variables.StringVariable;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Interpreter {

    private final VariableFactory variableFactory = new VariableFactory();
    private final Operations operations = new Operations(variableFactory);

    private final KlassRegister klassRegister;
    private final OperationsRegister operationsRegister;

    private final Context context = new Context();

    public Interpreter(Environment environment) {
        this.klassRegister = environment.getKlassRegister();
        this.operationsRegister = environment.getOperationsRegister();
    }

    public void interpret(ContinuNumFile ast) {
        for (Instruction instruction : ast.instructions()) {
            execute(instruction);
        }
    }

    private void execute(Instruction instruction) {
        switch (instruction) {
            case SymbolInitialization symbolInitialization -> executeSymbolInitialization(symbolInitialization);
            case MethodCall methodCall -> executeMethodCall(methodCall);
            case Assignment assignment -> executeAssignment(assignment);
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
            context.initializeVariable(name, value.asMutable());
        } else {
            throw new RuntimeException("Variable " + name + " is not mutable");
        }
    }

    private Variable executeMethodCall(MethodCall methodCall) {
        if (methodCall.typeName().isPresent()) {
            String typeName = methodCall.typeName().get();
            String methodName = methodCall.methodName();
            Klass klass = klassRegister.getKlass(typeName).orElseThrow();
            Variable[] args = methodCall.arguments().stream()
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

    private Variable evaluateExpression(Expression expression) {
        return switch (expression) {
            case BooleanLiteralExpression _ -> throw new UnsupportedOperationException();
            case IntegerLiteralExpression(String value) -> new I32Variable(Integer.parseInt(value));
            case StringLiteralExpression(String value) -> new StringVariable(value);
            case MethodCall methodCall -> executeMethodCall(methodCall);
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
        };
    }

}
