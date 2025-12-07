package de.til7701.continu_num.interpreter;

import de.til7701.continu_num.core.reflect.*;
import de.til7701.continu_num.interpreter.variables.*;

public class VariableFactory {

    public Variable cast(Variable variable, Type target) {
        return switch (target) {
            case I32 _ -> new I32Variable(variable.isMutable(), variable.value());
            case I16 _ -> new I16Variable(variable.isMutable(), variable.value());
            case None _ -> throw new UnsupportedOperationException("Cannot cast to None type");
            case Str _ -> new StringVariable(variable.isMutable(), variable.value().toString());
            case Any _ -> new AnyVariable(variable.isMutable(), variable.value());
        };
    }

    public Variable createVariableFromJavaObject(Type returnType, Object result) {
        return switch (returnType) {
            case I32 _ -> new I32Variable(false, result);
            case I16 _ -> new I16Variable(false, result);
            case None _ -> NoneVariable.INSTANCE;
            case Str _ -> new StringVariable(false, result.toString());
            case Any _ -> new AnyVariable(false, result);
        };
    }
}
