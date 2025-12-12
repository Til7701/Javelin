import org.jspecify.annotations.NullMarked;

@NullMarked
module de.til7701.javelin.cli {
    requires de.til7701.javelin.ast;
    requires de.til7701.javelin.parser;
    requires de.til7701.javelin.type.checker;
    requires info.picocli;
    requires org.slf4j;
    requires org.jspecify;
}
