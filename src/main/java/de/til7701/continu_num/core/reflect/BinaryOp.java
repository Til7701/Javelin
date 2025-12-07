package de.til7701.continu_num.core.reflect;

import de.til7701.continu_num.core.ast.BinaryOperator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BinaryOp {

    BinaryOperator operator();

}
