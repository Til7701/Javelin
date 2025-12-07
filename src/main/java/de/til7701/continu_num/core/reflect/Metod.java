package de.til7701.continu_num.core.reflect;

public sealed interface Metod permits JavaMetod {

    String name();

    Type returnType();

    Type[] parameterTypes();

    String[] parameterNames();

}
