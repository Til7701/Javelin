package de.til7701.continu_num.core.ast;

import de.til7701.continu_num.core.reflect.Type;

public record SymbolInitialization(
        boolean isMutable,
        Type type,
        String name,
        Expression value
) implements Instruction {

}
