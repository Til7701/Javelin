package de.til7701.continu_num.core.reflect;

import de.til7701.continu_num.core.util.Java;
import picocli.CommandLine;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class KlassLoader {

    public Klass loadJavaClass(Class<?> javaClass) {
        String className = javaClass.getSimpleName();
        Map<String, List<Metod>> methods = loadJavaMethods(javaClass);
        methods = Collections.unmodifiableMap(methods);
        Klass klass = new Klass(className, methods);
        CommandLine.tracer().debug(String.format("Loaded Klass: %s", klass));
        return klass;
    }

    private Map<String, List<Metod>> loadJavaMethods(Class<?> javaClass) {
        Map<String, List<Metod>> metods = new HashMap<>();

        Method[] javaMethods = javaClass.getDeclaredMethods();
        for (Method javaMethod : javaMethods) {
            String methodName = javaMethod.getName();
            Type returnType;
            try {
                returnType = Java.mapTypes(javaMethod.getReturnType());
            } catch (Exception _) {
                continue; // Skip methods with unsupported return types TODO handle more types
            }
            Type[] parameterTypes = Arrays.stream(javaMethod.getParameterTypes())
                    .map(p -> {
                        try {
                            return Java.mapTypes(p);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .toArray(Type[]::new);
            if (Arrays.asList(parameterTypes).contains(null)) {
                continue; // Skip methods with unsupported parameter types TODO handle more types
            }
            String[] parameterNames = Arrays.stream(javaMethod.getParameters())
                    .map(Parameter::getName)
                    .toArray(String[]::new);

            Metod metod = new JavaMetod(
                    javaClass,
                    methodName,
                    returnType,
                    parameterTypes,
                    javaMethod.getParameterTypes(),
                    parameterNames
            );
            metods.computeIfAbsent(methodName, _ -> new ArrayList<>())
                    .add(metod);
        }

        for (Map.Entry<String, List<Metod>> entry : metods.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }
        return metods;
    }

}
