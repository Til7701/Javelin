module de.til7701.javelin.test {
    requires de.til7701.javelin.cli;

    requires info.picocli;
    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;

    opens de.til7701.javelin.test.cli to org.junit.platform.commons;
}
