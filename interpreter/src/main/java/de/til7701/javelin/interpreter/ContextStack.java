package de.til7701.javelin.interpreter;

import de.til7701.javelin.interpreter.variable.Variable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

@ToString
@EqualsAndHashCode
class ContextStack {

    private final Deque<Context> stack;

    ContextStack() {
        this(new ArrayDeque<>());
    }

    private ContextStack(Deque<Context> stack) {
        this.stack = stack;
    }

    void push(Context context) {
        stack.push(context);
    }

    @Nullable Context pop() {
        if (stack.isEmpty())
            return null;
        return stack.pop();
    }

    void initializeVariable(String name, Variable variable) {
        Context context = stack.peek();
        if (context == null) {
            context = new Context();
            push(context);
        }
        context.initializeVariable(name, variable);
    }

    @Nullable Variable getVariable(String name) {
        for (Context context : stack) {
            Variable variable = context.getVariable(name);
            if (variable != null) {
                return variable;
            }
        }
        return null;
    }

    ContextStack snapshot() {
        Deque<Context> snapshot = stack.stream()
                .map(Context::snapshot)
                .collect(new Collector<Context, ArrayDeque<Context>, Deque<Context>>() {

                    @Override
                    public Supplier<ArrayDeque<Context>> supplier() {
                        return ArrayDeque::new;
                    }

                    @Override
                    public BiConsumer<ArrayDeque<Context>, Context> accumulator() {
                        return ArrayDeque::push;
                    }

                    @Override
                    public BinaryOperator<ArrayDeque<Context>> combiner() {
                        return (left, right) -> {
                            left.addAll(right);
                            return left;
                        };
                    }

                    @Override
                    public Function<ArrayDeque<Context>, Deque<Context>> finisher() {
                        return array -> array;
                    }

                    @Override
                    public Set<Characteristics> characteristics() {
                        return Set.of();
                    }
                });
        return new ContextStack(snapshot);
    }

}
