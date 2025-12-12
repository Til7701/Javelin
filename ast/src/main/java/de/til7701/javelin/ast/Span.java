package de.til7701.javelin.ast;

public record Span(
        int startLine,
        int startCol,
        int endLine,
        int endCol
) {
}
