package de.til7701.javelin.cli.commands;

import de.til7701.javelin.Interpreter;
import de.til7701.javelin.ast.Ast;
import de.til7701.javelin.cli.VersionProvider;
import de.til7701.javelin.cli.mixins.DebugMixin;
import de.til7701.javelin.environment.Environment;
import de.til7701.javelin.parser.FileParser;
import de.til7701.javelin.type_checker.TypeChecker;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "jvl",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class,
        description = "The Javelin programming language."
)
public class Javelin implements Callable<Integer> {

    @CommandLine.Mixin
    private DebugMixin debugMixin;

    @CommandLine.Parameters(
            index = "0",
            description = "The source file to execute.",
            arity = "1"
    )
    private File source;

    @Override
    public Integer call() throws Exception {
        FileParser fileParser = new FileParser();
        Ast ast = fileParser.parse(source);
        final Environment env = new Environment();
        TypeChecker typeChecker = new TypeChecker(env);
        typeChecker.check(ast);
        Interpreter interpreter = new Interpreter(env);
        interpreter.interpret(ast);
        return 0;
    }

}
