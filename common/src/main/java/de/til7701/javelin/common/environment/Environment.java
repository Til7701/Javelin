package de.til7701.javelin.common.environment;

import de.til7701.javelin.common.klass.KlassLoader;
import de.til7701.javelin.common.klass.KlassRegister;
import de.til7701.javelin.common.shaft.Array;
import de.til7701.javelin.common.shaft.Bool;
import de.til7701.javelin.common.shaft.Io;
import de.til7701.javelin.common.shaft.Str;
import de.til7701.javelin.common.util.Natives;
import lombok.Getter;

@Getter
public class Environment {

    private static final Class<?>[] SHAFT = new Class<?>[]{
            Void.class,
            Bool.class,
            Str.class,
            Array.class,
            Io.class,
    };

    private final KlassRegister klassRegister = new KlassRegister();
    private final KlassLoader klassLoader = new KlassLoader();

    private final Natives natives;

    public Environment(Natives natives) {
        this.natives = natives;
        for (Class<?> javaClass : SHAFT) {
            this.addJavaClass(javaClass);
        }
    }

    public void addJavaClass(Class<?> clazz) {
        klassRegister.registerKlass(klassLoader.loadJavaClass(clazz));
    }

}
