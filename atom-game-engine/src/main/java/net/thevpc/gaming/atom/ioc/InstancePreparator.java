package net.thevpc.gaming.atom.ioc;

import java.util.Map;

/**
 * Created by vpc on 9/25/16.
 */
public interface InstancePreparator {
    default void preInject(Object o, Map<Class, Object> injects){

    }
    default void postInject(Object o){

    }
}
