import org.jspecify.annotations.NullMarked;

@NullMarked
module de.til7701.javelin.cli {
    requires de.til7701.javelin.ast;
    requires de.til7701.javelin.common;
    requires de.til7701.javelin.parser;
    requires de.til7701.javelin.checker;
    requires de.til7701.javelin.interpreter;

    requires info.picocli;
    requires org.slf4j;
    requires org.jspecify;
    requires static lombok;

    opens de.til7701.javelin.cli.commands to info.picocli;
    opens de.til7701.javelin.cli.mixins to info.picocli;

    provides org.slf4j.spi.SLF4JServiceProvider with de.til7701.javelin.cli.log.LoggerServiceProvider;

    exports de.til7701.javelin.cli;
}
