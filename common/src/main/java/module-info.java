import org.jspecify.annotations.NullMarked;

@NullMarked
module de.til7701.javelin.common {
    requires static lombok;
    requires org.slf4j;

    requires de.til7701.javelin.ast;
    requires de.til7701.javelin.parser;
    requires org.antlr.antlr4.runtime;
    requires org.jspecify;

    exports de.til7701.javelin.environment;
    exports de.til7701.javelin.klass;
    exports de.til7701.javelin.sdk;
    exports de.til7701.javelin.common;
    exports de.til7701.javelin.util;
}
