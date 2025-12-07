package de.til7701.continu_num.core.reflect;

import de.til7701.continu_num.core.ast.BinaryOperator;

public non-sealed interface I32 extends Type {

    default boolean isAssignableFrom(Type other) {
        return switch (other) {
            case I16 _,
                 I32 _ -> true;
            default -> false;
        };
    }

    I32 minValue();

    I32 maxValue();

    @BinaryOp(operator = BinaryOperator.ADD)
    I32 add(I32 a, I32 b);

    @BinaryOp(operator = BinaryOperator.SUBTRACT)
    I32 sub(I32 a, I32 b);

    @BinaryOp(operator = BinaryOperator.MULTIPLY)
    I32 mul(I32 a, I32 b);

    @BinaryOp(operator = BinaryOperator.DIVIDE)
    I32 div(I32 a, I32 b);

    static I32 instance() {
        return Impl.INSTANCE;
    }

    final class Impl implements I32 {

        private static final I32 INSTANCE = new Impl();

        private Impl() {
        }

        @Override
        public I32 minValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public I32 maxValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public I32 add(I32 a, I32 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public I32 sub(I32 a, I32 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public I32 mul(I32 a, I32 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public I32 div(I32 a, I32 b) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return "i32";
        }
    }

}
