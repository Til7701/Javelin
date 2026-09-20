import org.jspecify.annotations.NullMarked;

@NullMarked
module de.til7701.javelin.common {
    requires static lombok;
    requires org.slf4j;

    requires de.til7701.javelin.ast;
    requires de.til7701.javelin.parser;
    requires org.antlr.antlr4.runtime;
    requires org.jspecify;

    exports de.til7701.javelin.common.environment;
    exports de.til7701.javelin.common.klass;
    exports de.til7701.javelin.common.head;
    exports de.til7701.javelin.common;
    exports de.til7701.javelin.common.shaft;
    exports de.til7701.javelin.common.util;
    exports de.til7701.javelin.common.util.ints;
}
