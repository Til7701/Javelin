package de.til7701.javelin.common.sdk;

import de.til7701.javelin.ast.Ast;
import de.til7701.javelin.ast.Script;
import de.til7701.javelin.ast.type_definition.TypeDefinition;
import de.til7701.javelin.common.klass.Klass;
import de.til7701.javelin.common.klass.KlassLoader;
import de.til7701.javelin.parser.Parser;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Sdk {

    private static final String[] sdkClassNames = {
//            "Array",
//            "BinaryOp",
//            "BinaryOperator",
//            "Char",
//            "Default",
//            "Getter",
//            "Setter",
//            "Str"
    };

    @Getter
    private final List<Klass> klasses;
    @Getter
    private final List<Ast> asts = new ArrayList<>();

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
            Ast ast = parser.parse(charStream, resourcePath);
            asts.add(ast);
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
