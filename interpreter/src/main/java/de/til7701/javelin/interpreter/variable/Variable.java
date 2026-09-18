package de.til7701.javelin.interpreter.variable;


import de.til7701.javelin.ast.type.Type;

public interface Variable {

    Type type();

    /// Sets the value of the variable using the given variable with copy semantics
    void set(Variable variable);

    Variable createNew();

    Object javaValue();
}
