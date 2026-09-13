package de.til7701.javelin.ast;

public record Span(
        int startLine,
        int startCol,
        int endLine,
        int endCol
) {

    public static Span undefined() {
        return new Span(-1, -1, -1, -1);
    }

}
