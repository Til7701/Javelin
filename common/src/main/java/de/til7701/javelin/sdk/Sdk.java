package de.til7701.javelin.sdk;

import de.til7701.javelin.ast.Ast;
import de.til7701.javelin.ast.Script;
import de.til7701.javelin.ast.type_definition.TypeDefinition;
import de.til7701.javelin.klass.Klass;
import de.til7701.javelin.klass.KlassLoader;
import de.til7701.javelin.parser.Parser;
import lombok.Getter;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class Sdk {

    private static final String[] sdkClassNames = {
            "Array",
            "BinaryOp",
            "BinaryOperator",
            "Char",
            "Default",
            "Getter",
            "Setter",
            "Str"
    };

    @Getter
    private final List<Klass> klasses;

    private final KlassLoader klassLoader = new KlassLoader();

    public Sdk() {
        this.klasses = loadDefaultClasses();
    }

    private List<Klass> loadDefaultClasses() {
        return Arrays.stream(sdkClassNames)
                .map(name -> "/sdk/" + name + ".jvl")
                .map(this::loadClassFromResource)
                .toList();
    }

    private Klass loadClassFromResource(String resourcePath) {
        try (InputStream stream = Sdk.class.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new RuntimeException("Sdk class not found: " + resourcePath);
            }
            String name = resourcePath.substring(resourcePath.lastIndexOf('/') + 1, resourcePath.length() - 4);
            CharStream charStream = CharStreams.fromStream(stream);
            Parser parser = new Parser();
            Ast ast = parser.parse(charStream);
            return switch (ast) {
                case TypeDefinition typeDef -> klassLoader.loadKlassFromAst(name, typeDef);
                case Script _ ->
                        throw new RuntimeException("Expected TypeDefinition, got Script in SDK class: " + resourcePath);
            };
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
