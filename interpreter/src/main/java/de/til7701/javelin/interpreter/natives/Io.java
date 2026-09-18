package de.til7701.javelin.interpreter.natives;

import de.til7701.javelin.common.primitive.Str;
import de.til7701.javelin.common.util.Ignore;

public final class Io {

    @Ignore
    private Io() {
    }

    public static void println(Str line) {
        System.out.println(line.getValue());
    }

}
