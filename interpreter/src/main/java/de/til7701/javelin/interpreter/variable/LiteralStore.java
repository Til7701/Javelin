package de.til7701.javelin.interpreter.variable;

import lombok.ToString;

class LiteralStore {

    private boolean trueValue = true;
    private boolean falseValue = false;

    Variable forBool(boolean literal) {
        if (literal)
            return new LiteralBoolVariable(true, trueValue);
        else
            return new LiteralBoolVariable(false, falseValue);
    }

    public String valuesToString() {
        return "Bool: [ true = " + trueValue + ", false = " + falseValue + " ]";
    }

    @ToString
    private class LiteralBoolVariable extends BooleanVariable {

        private final boolean literal;

        public LiteralBoolVariable(boolean literal, boolean value) {
            super(value);
            this.literal = literal;
        }

        @Override
        public void set(Variable variable) {
            super.set(variable);
            if (literal)
                trueValue = this.value;
            else
                falseValue = this.value;
        }
    }

}
