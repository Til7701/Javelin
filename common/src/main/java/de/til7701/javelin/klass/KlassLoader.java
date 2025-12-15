package de.til7701.javelin.klass;

import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.ast.type_definition.TypeDefinition;
import de.til7701.javelin.ast.type_definition.TypeModifierValue;
import de.til7701.javelin.ast.type_definition.annotations.AnnotationTypeDefinition;
import de.til7701.javelin.ast.type_definition.classes.ClassDefinition;
import de.til7701.javelin.ast.type_definition.enums.EnumTypeDefinition;
import de.til7701.javelin.ast.type_definition.enums.EnumValueDefinition;
import de.til7701.javelin.util.Java;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class KlassLoader {

    public Klass loadJavaClass(Class<?> javaClass) {
        String className = javaClass.getSimpleName();
        List<Metod> methods = loadJavaMethods(javaClass);
        Map<String, List<Metod>> methodsGroupedByName = methods.stream()
                .collect(Collectors.groupingBy(Metod::name));
        Klass klass = new JavaKlass(true, false, javaClass, className, methods, methodsGroupedByName);
        log.debug("Loaded Klass: {}", klass);
        return klass;
    }

    private List<Metod> loadJavaMethods(Class<?> javaClass) {
        List<Metod> metods = new ArrayList<>();

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
                        } catch (Exception _) {
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
            metods.add(metod);
        }
        return Collections.unmodifiableList(metods);
    }

    public Klass loadKlassFromAst(String klassName, TypeDefinition typeDefinition) {
        return switch (typeDefinition) {
            case ClassDefinition classDefinition -> loadKlassFromClassDefinition(classDefinition);
            case AnnotationTypeDefinition annotationTypeDefinition ->
                    loadKlassFromAnnotationTypeDefinition(klassName, annotationTypeDefinition);
            case EnumTypeDefinition enumTypeDefinition ->
                    loadKlassFromEnumTypeDefinition(klassName, enumTypeDefinition);
        };
    }

    private Klass loadKlassFromClassDefinition(ClassDefinition classDefinition) {
        // TODO implement
        return null;
    }

    private Klass loadKlassFromAnnotationTypeDefinition(String klassName, AnnotationTypeDefinition annotationTypeDefinition) {
        List<AnnotationField> fields = annotationTypeDefinition.fields().stream()
                .map(fieldDefinition -> new AnnotationField(
                        fieldDefinition.name(),
                        fieldDefinition.type()
                ))
                .toList();
        List<Metod> methods = annotationTypeDefinition.fields().stream()
                .map(fieldDefinition -> (Metod) new JavelinMetod(
                        fieldDefinition.name(),
                        fieldDefinition.type()
                ))
                .toList();
        return new AnnotationKlass(
                annotationTypeDefinition.modifiers().stream().anyMatch(m -> m.value() == TypeModifierValue.PUB),
                annotationTypeDefinition.modifiers().stream().anyMatch(m -> m.value() == TypeModifierValue.NATIVE),
                klassName,
                fields,
                methods,
                methods.stream().collect(Collectors.groupingBy(Metod::name))
        );
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
