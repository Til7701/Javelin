package de.til7701.javelin.interpreter;

import de.til7701.javelin.interpreter.variable.Variable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@ToString
@EqualsAndHashCode
class Context {

    private final Map<String, Variable> variables;

    Context() {
        this(new HashMap<>());
    }

    private Context(Map<String, Variable> variables) {
        this.variables = variables;
    }

    void initializeVariable(String name, Variable variable) {
        variables.put(name, variable);
    }

    @Nullable Variable getVariable(String name) {
        return variables.get(name);
    }

    Context snapshot() {
        Map<String, Variable> snapshot = new HashMap<>(variables);
        return new Context(snapshot);
    }

}
