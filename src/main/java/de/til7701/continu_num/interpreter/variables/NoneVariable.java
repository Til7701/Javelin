package de.til7701.continu_num.interpreter.variables;

import de.til7701.continu_num.core.reflect.None;
import de.til7701.continu_num.core.reflect.Type;
import de.til7701.continu_num.interpreter.Variable;

public class NoneVariable implements Variable, None {

    public static final NoneVariable INSTANCE = new NoneVariable();

    @Override
    public Type type() {
        return None.instance();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public Variable asMutable() {
        return INSTANCE;
    }

}
