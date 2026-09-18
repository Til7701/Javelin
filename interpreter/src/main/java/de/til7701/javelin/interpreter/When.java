package de.til7701.javelin.interpreter;

import de.til7701.javelin.ast.expression.Expression;
import de.til7701.javelin.ast.statement.Statement;
import de.til7701.javelin.common.primitive.Bool;
import de.til7701.javelin.interpreter.variable.PrimitiveVariable;
import de.til7701.javelin.interpreter.variable.Variable;

import java.util.Collection;

public class When {

    private final Interpreter interpreter;
    private final ContextStack context;
    private final Expression condition;
    private final Statement body;

    When(Interpreter interpreter, ContextStack context, Expression condition, Statement body, Collection<Variable> topVariables) {
        this.interpreter = interpreter;
        this.context = context;
        this.condition = condition;
        this.body = body;

        topVariables.forEach(v -> v.listen(this::eval));
    }

    void eval() {
        Variable result = interpreter.evaluateExpression(condition, context);
        if (result instanceof PrimitiveVariable primitiveVariable
                && primitiveVariable.javaValue() instanceof Bool bool
                && bool.isValue()) {
            interpreter.executeStatement(body, context);
        }
    }

}
