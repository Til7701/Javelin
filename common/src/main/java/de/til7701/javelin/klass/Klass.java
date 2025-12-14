package de.til7701.javelin.klass;

import de.til7701.javelin.ast.type.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Klass {

    boolean isPub();

    boolean isNative();

    String name();

    List<Metod> methods();

    Map<String, List<Metod>> methodsGroupedByName();

    default Optional<Metod> getMethod(String methodName, Type[] argumentTypes) {
        List<Metod> metods = methodsGroupedByName().get(methodName);
        if (metods == null) {
            return Optional.empty();
        }

        for (Metod metod : metods) {
            if (metod instanceof JavaMetod javaMetod) {
                Type[] parameterTypes = javaMetod.parameterTypes();
                if (parameterTypes.length != argumentTypes.length) {
                    continue;
                }
                boolean match = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!parameterTypes[i].equals(argumentTypes[i])) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return Optional.of(metod);
                }
            }
        }

        return Optional.empty();
    }

}
