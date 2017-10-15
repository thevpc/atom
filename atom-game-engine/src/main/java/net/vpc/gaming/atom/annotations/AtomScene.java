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
public @interface AtomScene {
    String id() default "";

    String title() default "Atom Scene";

    String engine() default "";

    int tileWidth() default 20;

    int tileHeight() default -1;

    int tileAltitude() default 1;

    float cameraWidth() default -1;

    float cameraHeight() default -1;

    boolean isometric() default false;

}
