package net.thevpc.gaming.atom.annotations;

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

    double columns() default -1;

    double rows() default -1;

    double stacks() default -1;

    int fps() default -1;

    boolean welcome() default false;
}
