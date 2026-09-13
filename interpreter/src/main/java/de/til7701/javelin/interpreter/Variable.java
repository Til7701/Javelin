package de.til7701.javelin.interpreter;


import de.til7701.javelin.ast.type.Type;

public interface Variable {

    Type type();

    void set(Variable variable);

    /// Creates a shallow copy.
    Variable copy();

    /// Creates a deep clone.
    Variable clone();

}
