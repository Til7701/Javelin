package de.til7701.javelin.cli;

import de.til7701.javelin.core.parser.SyntaxErrorException;
import picocli.CommandLine;

public class ExceptionExitCodeMapper implements CommandLine.IExitCodeExceptionMapper {

    @Override
    public int getExitCode(Throwable exception) {
        return switch (exception) {
            case SyntaxErrorException _ -> 2;
            default -> 1;
        };
    }

}
