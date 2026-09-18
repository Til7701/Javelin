package de.til7701.javelin.interpreter.variable;

import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.common.shaft.Primitive;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class PrimitiveVariable implements Variable {

    private final Collection<Runnable> listeners = new CopyOnWriteArrayList<>();

    private Primitive value;

    public PrimitiveVariable(Primitive value) {
        this.value = value;
    }

    @Override
    public Type type() {
        return value.type();
    }

    @Override
    public void set(Variable variable) {
        if (variable instanceof PrimitiveVariable primitive) {
            this.value = primitive.value;
        } else {
            throw new IllegalArgumentException();
        }
        listeners.forEach(Runnable::run);
    }

    @Override
    public Variable createNew() {
        return new PrimitiveVariable(value);
    }

    @Override
    public Object javaValue() {
        return value;
    }

    @Override
    public void listen(Runnable onUpdate) {
        listeners.add(onUpdate);
    }

}
