package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Node;

import java.util.function.Function;

public sealed interface Type extends Node permits CollectionType, GenericType, IType, SimpleType {

    Type mapNames(Function<String, String> nameMapper);

}
