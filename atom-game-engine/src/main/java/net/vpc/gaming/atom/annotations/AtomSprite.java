package net.vpc.gaming.atom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by vpc on 9/23/16.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AtomSprite {
    double x() default 0;

    double y() default 0;

    int width() default 1;

    int height() default 1;

    int life() default 1;

    int maxLife() default 1;

    int sight() default 1;

    double direction() default 0;

    double speed() default 0;

    String scene();

    String kind() default "";

    String name() default "";

    Class task() default Void.class;

    Class collision() default Void.class;
}
