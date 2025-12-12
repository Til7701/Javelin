package de.til7701.javelin.type_checker;

import de.til7701.javelin.core.reflect.Type;

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

    public void initializeVariable(String name, Type type) {
        Context context = stack.peek();
        context.initializeVariable(name, type);
    }

    public Type getVariable(String name) {
        for (Context context : stack) {
            Type type = context.getVariable(name);
            if (type != null) {
                return type;
            }
        }
        return null;
    }

    public void destroyVariable(String name) {
        Context context = stack.peek();
        context.destroyVariable(name);
    }

}
