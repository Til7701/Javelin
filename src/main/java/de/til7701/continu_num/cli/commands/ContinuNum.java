package de.til7701.continu_num.cli.commands;

import de.til7701.continu_num.cli.VersionProvider;
import de.til7701.continu_num.cli.mixins.DebugMixin;
import de.til7701.continu_num.core.ast.ContinuNumFile;
import de.til7701.continu_num.core.environment.Environment;
import de.til7701.continu_num.core.parser.FileParser;
import de.til7701.continu_num.core.type_checker.TypeChecker;
import de.til7701.continu_num.interpreter.Interpreter;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "conum",
        mixinStandardHelpOptions = true,
        versionProvider = VersionProvider.class,
        description = "The ContinuNum programming language."
)
public class ContinuNum implements Callable<Integer> {

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
        ContinuNumFile ast = fileParser.parse(source);
        final Environment env = new Environment();
        TypeChecker typeChecker = new TypeChecker(env);
        typeChecker.check(ast);
        Interpreter interpreter = new Interpreter(env);
        interpreter.interpret(ast);
        return 0;
    }

}
