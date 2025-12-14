package de.til7701.javelin.operation;

import de.til7701.javelin.ast.expression.BinaryOperator;
import de.til7701.javelin.ast.type.Type;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class OperationsRegister {

    private final Map<BinaryOperator, Map<Type, Map<Type, Operation>>> binaryOperations = new EnumMap<>(BinaryOperator.class);

    public void registerOperationsFromJavaClass(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            BinaryOp binaryOperation = method.getAnnotation(BinaryOp.class);
            if (binaryOperation != null) {
                BinaryOperator operator = binaryOperation.operator();
                Type returnType = Java.mapTypes(method.getReturnType());
                Type[] parameterTypes = Arrays.stream(method.getParameterTypes())
                        .map(p -> {
                            try {
                                return Java.mapTypes(p);
                            } catch (Exception _) {
                                return null;
                            }
                        })
                        .toArray(Type[]::new);
                if (Arrays.asList(parameterTypes).contains(null)) {
                    continue; // Skip methods with unsupported parameter types TODO handle more types
                }
                if (parameterTypes.length != 2) {
                    continue; // Skip methods that are not binary operations
                }

                Operation operation = new Operation(
                        parameterTypes[0],
                        parameterTypes[1],
                        returnType,
                        method
                );

                registerBinaryOperation(operator, operation);
            }
        }
    }

    public void registerBinaryOperation(BinaryOperator operator, Operation ops) {
        binaryOperations
                .computeIfAbsent(operator, _ -> new HashMap<>())
                .computeIfAbsent(ops.leftType(), _ -> new HashMap<>())
                .put(ops.rightType(), ops);
        log.debug("Registered binary operation: " + operator + " for types " + ops.leftType() + ", " + ops.rightType() + " -> " + ops.resultType());
    }

    public Optional<Operation> getBinaryOperation(BinaryOperator operator, Type leftType, Type rightType) {
        Map<Type, Map<Type, Operation>> leftMap = binaryOperations.get(operator);
        if (leftMap != null) {
            Map<Type, Operation> rightMap = leftMap.get(leftType);
            if (rightMap != null) {
                Operation operation = rightMap.get(rightType);
                if (operation != null) {
                    return Optional.of(operation);
                }
            }
        }
        return Optional.empty();
    }

}
