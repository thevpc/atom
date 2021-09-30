package net.thevpc.gaming.atom.ioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassNamedObjectMap {
    private final Map<Class, Map<String, Object>> values = new HashMap<>();


    public <T> void add(Class<T> cls, String name, T obj) {
        if (cls == null) {
            throw new IllegalArgumentException("null class");
        }
        if (obj == null) {
            throw new IllegalArgumentException("null object");
        }
        Map<String, Object> m = values.get(cls);
        if (m == null) {
            m = new HashMap<>();
            values.put(cls, m);
        } else {
            if (m.containsKey(name)) {
                throw new IllegalArgumentException("already registered " + cls.getName()
                        + ((name == null) ? "" :
                        (" named " + name)
                ));
            }
        }
        m.put(name, obj);
    }

    public <T> T get(Class<T> cls, String name) {
        Object o = find(cls, name);
        if (o == null) {
            throw new IllegalArgumentException("not found " + cls.getName()
                    + ((name == null) ? "" :
                    (" named " + name)
            )
            );
        }
        return (T) o;
    }

    public <T> List<T> findAll(Class<T> cls) {
        Map<String, Object> m = values.get(cls);
        if (m == null) {
            return new ArrayList<>();
        }
        return new ArrayList(m.values());
    }

    public <T> T find(Class<T> cls, String name) {
        Map<String, Object> m = values.get(cls);
        if (m == null) {
            return null;
        }
        Object v = m.get(name);
        if (v != null) {
            return (T) v;
        }
        if (name == null) {
            for (Object value : m.values()) {
                return (T) value;
            }
        }
        return null;
    }

}
