package de.til7701.continu_num.core.parser;

import lombok.Getter;

@Getter
public class SyntaxErrorException extends RuntimeException {

    private final Object offendingSymbol;
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
