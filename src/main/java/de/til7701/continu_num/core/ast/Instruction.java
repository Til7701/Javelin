package de.til7701.continu_num.core.ast;

public sealed interface Instruction extends Node permits Assignment, MethodCall, SymbolInitialization {
}
