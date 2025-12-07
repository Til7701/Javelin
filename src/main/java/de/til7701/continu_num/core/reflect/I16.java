package de.til7701.continu_num.core.reflect;

import de.til7701.continu_num.core.ast.BinaryOperator;

public non-sealed interface I16 extends Type {

    default boolean isAssignableFrom(Type other) {
        return switch (other) {
            case I16 _ -> true;
            default -> false;
        };
    }

    I16 minValue();

    I16 maxValue();

    @BinaryOp(operator = BinaryOperator.ADD)
    I16 add(I16 a, I16 b);

    @BinaryOp(operator = BinaryOperator.SUBTRACT)
    I16 sub(I16 a, I16 b);

    @BinaryOp(operator = BinaryOperator.MULTIPLY)
    I16 mul(I16 a, I16 b);

    @BinaryOp(operator = BinaryOperator.DIVIDE)
    I16 div(I16 a, I16 b);

    static I16 instance() {
        return I16.Impl.INSTANCE;
    }

    final class Impl implements I16 {

        private static final I16 INSTANCE = new Impl();

        private Impl() {
        }

        @Override
        public I16 minValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public I16 maxValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public I16 add(I16 a, I16 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public I16 sub(I16 a, I16 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public I16 mul(I16 a, I16 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public I16 div(I16 a, I16 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "i16";
        }
    }

}
