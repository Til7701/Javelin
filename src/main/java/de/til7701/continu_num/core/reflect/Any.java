package de.til7701.continu_num.core.reflect;

public non-sealed interface Any extends Type {

    default boolean isAssignableFrom(Type other) {
        return true;
    }

    static Any instance() {
        return Impl.INSTANCE;
    }

    final class Impl implements Any {

        private static final Any INSTANCE = new Impl();

        private Impl() {
        }

        @Override
        public String toString() {
            return "Any";
        }
    }

}
