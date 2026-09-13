package de.til7701.javelin.interpreter;

import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@ToString
public class Context {

    private final Map<String, Variable> variables = new HashMap<>();

    public void initializeVariable(String name, Variable variable) {
        variables.put(name, variable);
    }

    public @Nullable Variable getVariable(String name) {
        return variables.get(name);
    }

    public void destroyVariable(String name) {
        variables.remove(name);
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

}
