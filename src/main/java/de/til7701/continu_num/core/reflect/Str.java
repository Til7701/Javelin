package de.til7701.continu_num.core.reflect;

public non-sealed interface Str extends Type {

    default boolean isAssignableFrom(Type other) {
        return switch (other) {
            case Str _ -> true;
            default -> false;
        };
    }

    static Str instance() {
        return Impl.INSTANCE;
    }

    final class Impl implements Str {

        private static final Str INSTANCE = new Impl();

        private Impl() {
        }

        @Override
        public String toString() {
            return "str";
        }
    }

}
