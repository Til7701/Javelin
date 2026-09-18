package de.til7701.javelin.common.shaft;

import de.til7701.javelin.common.util.Ignore;
import de.til7701.javelin.common.util.Natives;

public final class Io {

    @Ignore
    private Io() {
    }

    public static void println(Natives natives, Str line) {
        natives.getStdOut().println(line.getValue());
    }

}
