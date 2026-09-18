package de.til7701.javelin.common.klass;

import de.til7701.javelin.ast.methods.MethodModifierValue;
import de.til7701.javelin.ast.methods.MethodParameter;
import de.til7701.javelin.ast.type.GenericType;
import de.til7701.javelin.ast.type.SimpleType;
import de.til7701.javelin.ast.type.Type;
import de.til7701.javelin.ast.type_definition.TypeDefinition;
import de.til7701.javelin.ast.type_definition.TypeModifierValue;
import de.til7701.javelin.ast.type_definition.classes.ClassDefinition;
import de.til7701.javelin.common.util.Ignore;
import de.til7701.javelin.common.util.Java;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        Klass klass = new JavaKlass(true, javaClass, className, methods, methodsGroupedByName);
        log.debug("Loaded Klass: {}", klass);
        return klass;
    }

    private List<Metod> loadJavaMethods(Class<?> javaClass) {
        List<Metod> metods = new ArrayList<>();

        Method[] javaMethods = javaClass.getDeclaredMethods();
        for (Method javaMethod : javaMethods) {
            if (((javaMethod.getModifiers() & Modifier.PUBLIC) == 0) || javaMethod.isAnnotationPresent(Ignore.class)) {
                continue;
            }

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
                    (javaMethod.getModifiers() & Modifier.STATIC) != 0,
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
            case ClassDefinition classDefinition -> loadKlassFromClassDefinition(klassName, classDefinition);
            default -> throw new IllegalStateException("Unexpected value: " + typeDefinition);
        };
    }

    private Klass loadKlassFromClassDefinition(String klassName, ClassDefinition classDefinition) {
        Type klassType = new SimpleType(classDefinition.span(), klassName);
        if (!classDefinition.generics().types().isEmpty())
            klassType = new GenericType(classDefinition.span(), klassType, classDefinition.generics());
        final Type finalKlassType = klassType;
        List<KlassField> fields = classDefinition.fields().stream()
                .map(fieldDefinition -> new KlassField(
                        fieldDefinition.name(),
                        fieldDefinition.type()
                ))
                .toList();
        List<JavelinMetod> constructors = classDefinition.constructors().stream()
                .map(constructorDefinition -> new JavelinMetod(
                        true,
                        klassName,
                        finalKlassType,
                        constructorDefinition.parameters().parameters().stream()
                                .map(MethodParameter::type)
                                .toArray(Type[]::new),
                        constructorDefinition.parameters().parameters().stream()
                                .map(MethodParameter::name)
                                .toArray(String[]::new),
                        Optional.of(constructorDefinition.body())
                ))
                .toList();
        List<Metod> methods = classDefinition.methods().stream()
                .map(methodDefinition -> (Metod) new JavelinMetod(
                        methodDefinition.modifiers().stream().anyMatch(m -> m.value() == MethodModifierValue.STATIC),
                        methodDefinition.name(),
                        methodDefinition.returnType().orElse(new SimpleType(methodDefinition.span(), "None")),
                        methodDefinition.parameters().parameters().stream()
                                .map(MethodParameter::type)
                                .toArray(Type[]::new),
                        methodDefinition.parameters().parameters().stream()
                                .map(MethodParameter::name)
                                .toArray(String[]::new),
                        Optional.of(methodDefinition.body())
                ))
                .toList();
        return new JavelinKlass(
                classDefinition.modifiers().stream().anyMatch(m -> m.value() == TypeModifierValue.PUB),
                classDefinition.modifiers().stream().anyMatch(m -> m.value() == TypeModifierValue.NATIVE),
                klassName,
                fields,
                constructors,
                methods,
                methods.stream().collect(Collectors.groupingBy(Metod::name))
        );
    }

}
