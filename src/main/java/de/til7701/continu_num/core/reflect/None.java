package de.til7701.continu_num.core.reflect;

public non-sealed interface None extends Type {

    default boolean isAssignableFrom(Type other) {
        return other instanceof None;
    }

    static None instance() {
        return Impl.INSTANCE;
    }

    final class Impl implements None {

        private static final None INSTANCE = new Impl();

        private Impl() {
        }

        @Override
        public String toString() {
            return "None";
        }
    }

}
