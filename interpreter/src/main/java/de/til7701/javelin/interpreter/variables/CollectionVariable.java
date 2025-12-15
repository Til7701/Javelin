package de.til7701.javelin.interpreter.variables;

import de.til7701.javelin.core.reflect.Collection;
import de.til7701.javelin.core.reflect.I32;
import de.til7701.javelin.core.reflect.Str;
import de.til7701.javelin.core.reflect.Type;
import de.til7701.javelin.interpreter.Variable;
import de.til7701.javelin.interpreter.VariableFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionVariable implements Variable, Collection {

    private static final VariableFactory VARIABLE_FACTORY = new VariableFactory();

    private final boolean mutable;

    private final Type indexType;
    private final Type elementType;

    private Map<Variable, Variable> value;

    public CollectionVariable(String value) {
        this(false, value);
    }

    public CollectionVariable(boolean mutable, Object value) {
        this.mutable = mutable;
        this.value = toCollection(value);
        Map.Entry<Variable, Variable> firstEntry = this.value.entrySet().iterator().next();
        this.indexType = firstEntry.getKey().type();
        this.elementType = firstEntry.getValue().type();
    }

    private static Map<Variable, Variable> toCollection(Object value) {
        return switch (value) {
            case CollectionVariable v -> v.value;
            case Map<?, ?> map -> map.entrySet().stream()
                    .collect(Collectors.toMap(
                            e -> VARIABLE_FACTORY.createVariableFromJavaObject(Str.instance(), e.getKey()),
                            e -> VARIABLE_FACTORY.createVariableFromJavaObject(Str.instance(), e.getValue())
                    ));
            case List<?> list -> {
                Map<Variable, Variable> result = new java.util.HashMap<>();
                for (int i = 0; i < list.size(); i++) {
                    result.put(
                            VARIABLE_FACTORY.createVariableFromJavaObject(Str.instance(), i),
                            VARIABLE_FACTORY.createVariableFromJavaObject(Str.instance(), list.get(i))
                    );
                }
                yield result;
            }
            case Object[] array -> {
                Map<Variable, Variable> result = new java.util.HashMap<>();
                for (int i = 0; i < array.length; i++) {
                    result.put(
                            VARIABLE_FACTORY.createVariableFromJavaObject(Str.instance(), i),
                            VARIABLE_FACTORY.createVariableFromJavaObject(Str.instance(), array[i])
                    );
                }
                yield result;
            }
            default -> throw new IllegalArgumentException("Cannot convert value to string: " + value);
        };
    }

    public void setValue(Map<Variable, Variable> value) {
        if (!mutable) {
            throw new UnsupportedOperationException("Variable is immutable");
        }
        this.value = value;
    }

    @Override
    public Type type() {
        return Collection.instance(indexType, elementType);
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    public Map<Variable, Variable> value() {
        return value;
    }

    @Override
    public Variable asMutable() {
        return new CollectionVariable(true, value);
    }

    @Override
    public Type indexType() {
        return indexType;
    }

    @Override
    public Type getElementType() {
        return elementType;
    }

    @Override
    public Type getElementAt(Collection collection, I32 index) {
        if (collection instanceof CollectionVariable collectionVariable) {
            if (collectionVariable.indexType.getClass().isInstance(index)) {
                return (Type) value.get(index);
            }
            throw new IllegalArgumentException("Index type mismatch: expected " + indexType + " but got " + index);
        }
        throw new UnsupportedOperationException("Unsupported collection type: " + collection.getClass().getName());
    }

}
