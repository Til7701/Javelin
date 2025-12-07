package de.til7701.continu_num.cli;

import de.til7701.continu_num.core.parser.SyntaxErrorException;
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
