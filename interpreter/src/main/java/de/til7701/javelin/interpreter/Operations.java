package de.til7701.javelin.interpreter;

import de.til7701.javelin.core.reflect.Operation;
import picocli.CommandLine;

import java.lang.reflect.Method;

public class Operations {

    private final VariableFactory variableFactory;

    public Operations(VariableFactory variableFactory) {
        this.variableFactory = variableFactory;
    }

    public Variable invokeBinary(Operation operation, Variable leftValue, Variable rightValue) {
        Method method = operation.declaringJavaMethod();
        try {
            Object result = method.invoke(leftValue, leftValue, rightValue);
            return variableFactory.createVariableFromJavaObject(operation.resultType(), result);
        } catch (Exception e) {
            CommandLine.tracer().debug("Error invoking binary operation: %s with left: %s and right: %s", operation, leftValue, rightValue);
            throw new RuntimeException("Failed to invoke operation method", e);
        }
    }

}
