package de.til7701.continu_num.core.environment;

import de.til7701.continu_num.core.reflect.*;
import lombok.Getter;

import java.nio.file.Files;

@Getter
public class Environment {

    private static final Class<?>[] DEFAULT_JAVA_CLASSES = new Class<?>[]{
            IO.class,
            Files.class,
            I16.class,
            I32.class,
            Str.class,
            None.class,
            Any.class,
    };

    private final OperationsRegister operationsRegister = new OperationsRegister();
    private final KlassRegister klassRegister = new KlassRegister();
    private final KlassLoader klassLoader = new KlassLoader();

    public Environment() {
        for (Class<?> javaClass : DEFAULT_JAVA_CLASSES) {
            klassRegister.registerKlass(klassLoader.loadJavaClass(javaClass));
        }
        discoverOperations(DEFAULT_JAVA_CLASSES);
    }

    public void discoverOperations(Class<?>[] operationClasses) {
        for (Class<?> operationClass : operationClasses) {
            operationsRegister.registerOperationsFromJavaClass(operationClass);
        }
    }

}
