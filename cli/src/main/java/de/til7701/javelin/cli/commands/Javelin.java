package de.til7701.javelin.cli.commands;

import de.til7701.javelin.ast.Ast;
import de.til7701.javelin.checker.TypeChecker;
import de.til7701.javelin.cli.VersionProvider;
import de.til7701.javelin.cli.mixins.DebugMixin;
import de.til7701.javelin.cli.pretty.AstPrettyPrinter;
import de.til7701.javelin.environment.Environment;
import de.til7701.javelin.interpreter.Interpreter;
import de.til7701.javelin.klass.Klass;
import de.til7701.javelin.parser.Parser;
import de.til7701.javelin.sdk.Sdk;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
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
        Parser fileParser = new Parser();
        Ast ast = fileParser.parseFile(source);

        if (log.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder();
            AstPrettyPrinter prettyPrinter = new AstPrettyPrinter(builder::append);
            prettyPrinter.print(ast, 0);
            log.debug("Parsed AST:\n{}", builder);
        }

        final Environment env = new Environment();
        Sdk sdk = new Sdk();
        if (log.isDebugEnabled()) {
            List<Ast> asts = sdk.getAsts();
            for (int i = 0; i < asts.size(); i++) {
                StringBuilder builder = new StringBuilder();
                AstPrettyPrinter prettyPrinter = new AstPrettyPrinter(builder::append);
                prettyPrinter.print(asts.get(i), 0);
                log.debug("Parsed SDK class '{}':\n{}", sdk.getKlasses().get(i).name(), builder);
            }
        }
        for (Klass klass : sdk.getKlasses()) {
            env.getKlassRegister().registerKlass(klass);
        }
        TypeChecker typeChecker = new TypeChecker(env);
        typeChecker.check(ast);
        Interpreter interpreter = new Interpreter(env);
        interpreter.interpret(ast);
        return 0;
    }

}
