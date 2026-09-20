module de.til7701.javelin.test {
    requires de.til7701.javelin.common;

    requires org.junit.jupiter.api;
    requires org.junit.jupiter.params;

    opens de.til7701.javelin.test.common.util.ints to org.junit.platform.commons;
}
