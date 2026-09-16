package de.til7701.javelin.interpreter;

import de.til7701.javelin.interpreter.variable.Variable;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;

@ToString
public class ContextStack {

    private final Deque<Context> stack = new ArrayDeque<>();

    public void push(Context context) {
        stack.push(context);
    }

    public @Nullable Context pop() {
        if (stack.isEmpty())
            return null;
        return stack.pop();
    }

    public void initializeVariable(String name, Variable variable) {
        Context context = stack.peek();
        if (context == null) {
            context = new Context();
            push(context);
        }
        context.initializeVariable(name, variable);
    }

    public void updateVariable(String name, Variable variable) {
        for (Context context : stack) {
            if (context.hasVariable(name)) {
                context.initializeVariable(name, variable);
                return;
            }
        }
        throw new RuntimeException("Variable " + name + " not found in any context");
    }

    public @Nullable Variable getVariable(String name) {
        for (Context context : stack) {
            Variable variable = context.getVariable(name);
            if (variable != null) {
                return variable;
            }
        }
        return null;
    }

}
