package de.til7701.javelin.interpreter;

import de.til7701.javelin.ast.expression.Expression;
import de.til7701.javelin.ast.statement.Statement;
import de.til7701.javelin.common.environment.Imports;
import de.til7701.javelin.common.shaft.Bool;
import de.til7701.javelin.interpreter.variable.PrimitiveVariable;
import de.til7701.javelin.interpreter.variable.Variable;

import java.util.Collection;

class When {

    private final Interpreter interpreter;
    private final Stack stack;
    private final Imports imports;
    private final Expression condition;
    private final Statement body;

    When(Interpreter interpreter, Stack stack, Imports imports, Expression condition, Statement body, Collection<Variable> topVariables) {
        this.interpreter = interpreter;
        this.stack = stack;
        this.imports = imports;
        this.condition = condition;
        this.body = body;

        topVariables.forEach(v -> v.listen(this::eval));
    }

    void eval() {
        Variable result = interpreter.evaluateExpression(condition, stack);
        if (result instanceof PrimitiveVariable primitiveVariable
                && primitiveVariable.javaValue() instanceof Bool bool
                && bool.isValue()) {
            interpreter.interrupt(new Interrupt(body, stack, imports));
        }
    }

}
