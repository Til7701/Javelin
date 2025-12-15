package de.til7701.javelin.klass;

import de.til7701.javelin.ast.methods.MethodParameter;
import de.til7701.javelin.ast.type.GenericType;
import de.til7701.javelin.ast.type.SimpleType;
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
            case ClassDefinition classDefinition -> loadKlassFromClassDefinition(klassName, classDefinition);
            case AnnotationTypeDefinition annotationTypeDefinition ->
                    loadKlassFromAnnotationTypeDefinition(klassName, annotationTypeDefinition);
            case EnumTypeDefinition enumTypeDefinition ->
                    loadKlassFromEnumTypeDefinition(klassName, enumTypeDefinition);
        };
    }

    private Klass loadKlassFromClassDefinition(String klassName, ClassDefinition classDefinition) {
        Type klassType = new SimpleType(classDefinition.span(), klassName);
        if (!classDefinition.generics().types().isEmpty())
            klassType = new GenericType(classDefinition.span(), klassType, classDefinition.generics().types());
        final Type finalKlassType = klassType;
        List<KlassField> fields = classDefinition.fields().stream()
                .map(fieldDefinition -> new KlassField(
                        fieldDefinition.name(),
                        fieldDefinition.type()
                ))
                .toList();
        List<JavelinConstructor> constructors = classDefinition.constructors().stream()
                .map(constructorDefinition -> new JavelinConstructor(
                        klassName,
                        finalKlassType,
                        (Type[]) constructorDefinition.parameters().parameters().stream()
                                .map(MethodParameter::type)
                                .toArray(),
                        constructorDefinition.parameters().parameters().stream()
                                .map(MethodParameter::name)
                                .toArray(String[]::new),
                        Optional.of(constructorDefinition.body())
                ))
                .toList();
        List<Metod> methods = classDefinition.methods().stream()
                .map(methodDefinition -> (Metod) new JavelinMetod(
                        methodDefinition.name(),
                        methodDefinition.returnType().orElse(new SimpleType(methodDefinition.span(), "None")),
                        (Type[]) methodDefinition.parameters().parameters().stream()
                                .map(MethodParameter::type)
                                .toArray(),
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
