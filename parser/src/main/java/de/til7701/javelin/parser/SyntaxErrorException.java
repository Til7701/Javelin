package de.til7701.javelin.parser;

import lombok.Getter;
import org.jspecify.annotations.NullMarked;

@Getter
@NullMarked
public class SyntaxErrorException extends RuntimeException {

    private final transient Object offendingSymbol;
    private final int line;
    private final int charPositionInLine;
    private final String msg;

    public SyntaxErrorException(Object offendingSymbol, int line, int charPositionInLine, String msg, Throwable cause) {
        super(cause);
        this.offendingSymbol = offendingSymbol;
        this.line = line;
        this.charPositionInLine = charPositionInLine;
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return "[ERROR] line " + line + ": " + msg;
    }

}
