package de.til7701.javelin.klass;

import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.ast.type_definition.TypeDefinition;
import de.til7701.javelin.ast.type_definition.TypeModifierValue;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationTypeDefinition;
import de.til7701.javelin.ast.type_definition.classes.ClassDefinition;
import de.til7701.javelin.ast.type_definition.enums.EnumTypeDefinition;
import de.til7701.javelin.ast.type_definition.enums.EnumValueDefinition;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Slf4j
public class KlassLoader {

    public Klass loadJavaClass(Class<?> javaClass) {
        String className = javaClass.getSimpleName();
        Map<String, List<Metod>> methods = loadJavaMethods(javaClass);
        methods = Collections.unmodifiableMap(methods);
        Klass klass = new Klass(className, methods);
        log.debug(String.format("Loaded Klass: %s", klass));
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

    public Klass loadKlassFromAst(String klassName, TypeDefinition typeDefinition) {
        return switch (typeDefinition) {
            case ClassDefinition classDefinition -> loadKlassFromClassDefinition(classDefinition);
            case AnnotationTypeDefinition annotationTypeDefinition ->
                    loadKlassFromAnnotationTypeDefinition(annotationTypeDefinition);
            case EnumTypeDefinition enumTypeDefinition ->
                    loadKlassFromEnumTypeDefinition(klassName, enumTypeDefinition);
        };
    }

    private Klass loadKlassFromClassDefinition(ClassDefinition classDefinition) {
        // TODO implement
        return null;
    }

    private Klass loadKlassFromAnnotationTypeDefinition(AnnotationTypeDefinition annotationTypeDefinition) {
        // TODO implement
        return null;
    }

    private Klass loadKlassFromEnumTypeDefinition(String klassName, EnumTypeDefinition enumTypeDefinition) {
        List<String> values = enumTypeDefinition.values().stream()
                .map(EnumValueDefinition::name)
                .toList();
        return new EnumKlass(
                enumTypeDefinition.modifiers().stream().anyMatch(m -> m.value() == TypeModifierValue.PUB),
                enumTypeDefinition.modifiers().stream().anyMatch(m -> m.value() == TypeModifierValue.NATIVE),
                klassName,
                values,
                List.of()
        );
    }

}
