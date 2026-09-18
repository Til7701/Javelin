package de.til7701.javelin.cli.commands;

import de.til7701.javelin.cli.Runner;
import de.til7701.javelin.cli.VersionProvider;
import de.til7701.javelin.cli.mixins.DebugMixin;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.io.File;
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
        Runner runner = new Runner(
                source,
                System.in,
                System.out
        );
        return runner.run();
    }

}
