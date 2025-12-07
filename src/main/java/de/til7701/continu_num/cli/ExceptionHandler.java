package de.til7701.continu_num.cli;

import picocli.CommandLine;

public class ExceptionHandler implements CommandLine.IExecutionExceptionHandler {

    public int handleExecutionException(Exception ex, CommandLine cmd, CommandLine.ParseResult parseResult) {
        cmd.getErr().println(cmd.getColorScheme().errorText(ex.getMessage()));
        if (CommandLine.tracer().isDebug())
            ex.printStackTrace(cmd.getErr());

        return cmd.getExitCodeExceptionMapper() != null
                ? cmd.getExitCodeExceptionMapper().getExitCode(ex)
                : cmd.getCommandSpec().exitCodeOnExecutionException();
    }

}
