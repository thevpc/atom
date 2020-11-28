package net.thevpc.gaming.atom.ioc;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vpc on 9/25/16.
 */
public interface AtomIoCContainer {
    void start();

    boolean contains(String id, String namespace);

    boolean contains(String id);

    boolean contains(Class type);

    Object getBean(String id, String namespace);

    Object getBean(String id);

    Object getBean(Class type);

    public List<Object> getBeans(Class type);

    void register(String id, String namespace, Object instance);

    void register(String id, Object instance);

    void register(String id, Class cls, Object instance);

    Object create(Class cls,
                  InstancePreparator[] ts,
                  Map<Class, Object> vals);

    Set<Class> getInjectedTypes(Class cls);

    boolean isInjectedType(Class cls, Class toInject);

    void invokeMethodsByAnnotation(Object instance, Class annClazz, Object[] args);
}
