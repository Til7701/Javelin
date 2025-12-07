package de.til7701.continu_num.core.reflect;

import picocli.CommandLine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Klass(
        String name,
        Map<String, List<Metod>> methods
) {

    public Optional<Metod> getMethod(String methodName, Type[] argumentTypes) {
        List<Metod> metods = methods.get(methodName);
        if (metods == null) {
            return Optional.empty();
        }

        for (Metod metod : metods) {
            if (metod instanceof JavaMetod javaMetod) {
                CommandLine.tracer().debug("Checking method: " + metod);
                Type[] parameterTypes = javaMetod.parameterTypes();
                if (parameterTypes.length != argumentTypes.length) {
                    continue;
                }
                boolean match = true;
                for (int i = 0; i < parameterTypes.length; i++) {
                    if (!parameterTypes[i].isAssignableFrom(argumentTypes[i])) {
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
