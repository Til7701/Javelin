package de.til7701.javelin.interpreter.variable;

public class VariableFactory {

    private final LiteralStore literalStore = new LiteralStore();

    public Variable fromBool(boolean value) {
        return new BooleanVariable(value);
    }

    public Variable fromBoolLiteral(boolean value) {
        return literalStore.forBool(value);
    }

    public Variable fromStrLiteral(String value) {
        return new StringVariable(value);
    }

    public String literalsToString() {
        return literalStore.valuesToString();
    }

}
