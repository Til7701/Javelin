package de.til7701.javelin.interpreter;

import java.util.ArrayDeque;
import java.util.Deque;

public class ContextStack {

    private final Deque<Context> stack = new ArrayDeque<>();

    public void push(Context context) {
        stack.push(context);
    }

    public Context pop() {
        return stack.pop();
    }

    public void initializeVariable(String name, Variable variable) {
        Context context = stack.peek();
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

    public Variable getVariable(String name) {
        for (Context context : stack) {
            Variable variable = context.getVariable(name);
            if (variable != null) {
                return variable;
            }
        }
        return null;
    }

    public void destroyVariable(String name) {
        Context context = stack.peek();
        context.destroyVariable(name);
    }

}
