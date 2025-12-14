module de.til7701.javelin.environment {
    requires de.til7701.javelin.ast;
    requires static lombok;
    requires de.til7701.javelin.parser;
    requires org.antlr.antlr4.runtime;

    exports de.til7701.javelin.environment;
    exports de.til7701.javelin.klass;
    exports de.til7701.javelin.operation;
}
