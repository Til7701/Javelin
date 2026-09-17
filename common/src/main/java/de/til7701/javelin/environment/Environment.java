package de.til7701.javelin.environment;

import de.til7701.javelin.klass.KlassLoader;
import de.til7701.javelin.klass.KlassRegister;
import de.til7701.javelin.primitive.Bool;
import de.til7701.javelin.primitive.Str;
import lombok.Getter;

@Getter
public class Environment {

    private static final Class<?>[] PRIMITIVES = new Class<?>[]{
            Bool.class,
            Str.class,
    };

    private final KlassRegister klassRegister = new KlassRegister();
    private final KlassLoader klassLoader = new KlassLoader();

    public Environment() {
        for (Class<?> javaClass : PRIMITIVES) {
            this.addJavaClass(javaClass);
        }
    }

    public void addJavaClass(Class<?> clazz) {
        klassRegister.registerKlass(klassLoader.loadJavaClass(clazz));
    }

}
