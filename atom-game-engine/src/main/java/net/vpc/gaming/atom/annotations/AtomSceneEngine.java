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
public @interface AtomSceneEngine {
    String id() default "";

    double width() default 1;

    double height() default 1;

    double altitude() default 1;

    int fps() default 25;

    boolean welcome() default false;
}
