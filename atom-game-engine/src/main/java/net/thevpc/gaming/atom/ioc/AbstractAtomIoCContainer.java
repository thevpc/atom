package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.annotations.OnInit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by vpc on 9/25/16.
 */
public abstract class AbstractAtomIoCContainer implements AtomIoCContainer {
    private AbstractAtomIoCContainer parent;

    public AbstractAtomIoCContainer(AbstractAtomIoCContainer parent) {
        this.parent = parent;
    }

    @Override
    public boolean contains(Class type) {
        return getBeans(type).size() > 0;
    }

    @Override
    public boolean contains(String id) {
        Object o = getBeanOrNull(id);
        if (o != null) {
            return true;
        }
        if (parent != null) {
            return parent.contains(id);
        }
        return false;
    }

    @Override
    public Object getBean(String id) {
        Object o = getBeanOrNull(id);
        if (o != null) {
            return o;
        }
        if (parent != null) {
            return parent.getBean(id);
        }
        throw new IllegalArgumentException("Bean Not Found " + id);
    }

    @Override
    public Object getBean(Class type) {
        List<Object> objects = getBeans(type);
        if (objects != null) {
            if (objects.size() == 1) {
                return objects.get(0);
            }
            if (objects.size() > 1) {
                throw new RuntimeException("Too many (" + objects.size() + ") beans for type " + type);
            }
        }
        throw new RuntimeException("Bean Not found " + type);
    }

    protected abstract Object getBeanOrNull(String id);

    @Override
    public Object create(Class cls,
                         InstancePreparator[] ts,
                         Map<Class, Object> vals) {
        Object o = null;
        try {
            o = cls.getConstructor().newInstance();
        } catch (Exception e) {
            if(Modifier.isAbstract(cls.getModifiers())){
                throw new IllegalArgumentException("abstract class cannot be instantiated: "+cls);
            }
            throw new IllegalArgumentException(e);
        }
        if(ts!=null){
            for (InstancePreparator t : ts) {
                if (t != null) {
                    t.preInject(o,vals);
                }
            }
        }
        doInjects(o, vals);
        if (ts != null) {
            for (InstancePreparator t : ts) {
                if (t != null) {
                    t.postInject(o);
                }
            }
        }
        invokeMethodsByAnnotation(o, OnInit.class, new Object[0]);
        return o;
    }

    private void doInjects(Object instance, Map<Class, Object> vals) {
        if (instance != null && vals != null) {
            Class<?> cls = instance.getClass();
            while (cls != null && !cls.equals(Object.class)) {
                for (Field field : cls.getDeclaredFields()) {
                    if (field.getAnnotation(Inject.class) != null) {
                        if (vals.containsKey(field.getType())) {
                            try {
                                field.setAccessible(true);
                                field.set(instance, vals.get(field.getType()));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Object bean = null;
                            try {
                                bean = getBean(field.getType());
                            } catch (Exception e) {
                                throw new IllegalArgumentException("Unsupported Inject Type " + field.getType() + " for field " + field,e);
                            }
                            try {
                                field.setAccessible(true);
                                field.set(instance, bean);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                cls = cls.getSuperclass();
            }
        }
    }

    @Override
    public Set<Class> getInjectedTypes(Class cls) {
        Set<Class> all = new HashSet<>();
        Class cls0 = cls;
        while (cls0 != null && !cls0.equals(Object.class)) {
            for (Field field : cls0.getDeclaredFields()) {
                if (field.getAnnotation(Inject.class) != null) {
                    all.add(field.getType());
                }
            }
            cls0 = cls0.getSuperclass();
        }
        return all;
    }

    @Override
    public boolean isInjectedType(Class cls, Class toInject) {
        Class cls0 = cls;
        while (cls0 != null && !cls0.equals(Object.class)) {
            for (Field field : cls0.getDeclaredFields()) {
                if (field.getAnnotation(Inject.class) != null) {
                    if (field.getType().equals(toInject)) {
                        return true;
                    }
                }
            }
            cls0 = cls0.getSuperclass();
        }
        return false;
    }

    @Override
    public void invokeMethodsByAnnotation(Object instance, Class annClazz, Object[] args) {
        if (instance != null) {
            Class<?> cls = instance.getClass();
            while (cls != null && !cls.equals(Object.class)) {
                for (Method m : cls.getDeclaredMethods()) {
                    if (m.getAnnotation(annClazz) != null) {
                        if (m.getParameterTypes().length == 0) {
                            try {
                                m.setAccessible(true);
                                m.invoke(instance, args);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        } else {
                            throw new IllegalArgumentException("Unsupported");
                        }
                    }
                }
                cls = cls.getSuperclass();
            }
        }
    }

    private String buildId(String id, String namespace) {
        if (namespace == null) {
            namespace = "";
        }
        return (namespace + ":" + id);
    }

    @Override
    public boolean contains(String id, String namespace) {
        return contains(buildId(id, namespace));
    }


    @Override
    public Object getBean(String id, String namespace) {
        return getBean(buildId(id, namespace));
    }

    @Override
    public void register(String id, String namespace, Object instance) {
        if (id == null) {
            id = instance.getClass().getName();
        }
        register(buildId(id, namespace), instance);
    }


    @Override
    public void register(String id, Object instance) {
        register(id, (Class) null, instance);
    }
}
