package de.til7701.javelin.ast.type;

import de.til7701.javelin.ast.Node;

public sealed interface Type extends Node permits CollectionType, GenericType, SimpleType {
}
