package de.til7701.javelin.interpreter;

import de.til7701.javelin.core.reflect.*;
import de.til7701.javelin.interpreter.variables.*;

public class VariableFactory {

    public Variable cast(Variable variable, Type target) {
        return switch (target) {
            case I32 _ -> new I32Variable(variable.isMutable(), variable.value());
            case I16 _ -> new I16Variable(variable.isMutable(), variable.value());
            case None _ -> throw new UnsupportedOperationException("Cannot cast to None type");
            case Str _ -> new StrVariable(variable.isMutable(), variable.value());
            case Bool _ -> new BoolVariable(variable.isMutable(), variable.value());
            case Any _ -> new AnyVariable(variable.isMutable(), variable.value());
            case Collection _ -> new CollectionVariable(variable.isMutable(), variable.value());
        };
    }

    public Variable createVariableFromJavaObject(Type returnType, Object result) {
        return switch (returnType) {
            case I32 _ -> new I32Variable(false, result);
            case I16 _ -> new I16Variable(false, result);
            case None _ -> NoneVariable.INSTANCE;
            case Str _ -> new StrVariable(false, result);
            case Bool _ -> new BoolVariable(false, result);
            case Any _ -> new AnyVariable(false, result);
            case Collection _ -> new CollectionVariable(false, result);
        };
    }
}
