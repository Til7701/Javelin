package de.til7701.javelin.interpreter;

import de.til7701.javelin.ast.statement.Statement;

record Interrupt(
        Statement unterbrechungsbehandlungsprozedur,
        Stack stack
) {
}
