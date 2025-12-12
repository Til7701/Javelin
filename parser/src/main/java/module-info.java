module de.til7701.javelin.parser {
    requires transitive de.til7701.javelin.ast;

    requires static lombok;

    requires org.slf4j;
    requires org.antlr.antlr4.runtime;
    requires org.jspecify;

    exports de.til7701.javelin.parser;
}
