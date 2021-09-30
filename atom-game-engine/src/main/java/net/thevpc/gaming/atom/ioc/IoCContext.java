package net.thevpc.gaming.atom.ioc;

public class IoCContext {
    private ClassNamedObjectMap context = new ClassNamedObjectMap();
    private final Object current;
    private final String sceneEngineId;
    private final String sceneId;

    //	public IoCContext(Object current, Map<Class, Object> injects) {
//		this.context = injects;
//		this.current = current;
//	}
    public IoCContext(Object current, String sceneEngineId, String sceneId) {
        this.context = new ClassNamedObjectMap();
        this.current = current;
        this.sceneEngineId = sceneEngineId;
        this.sceneId = sceneId;
    }

    public ClassNamedObjectMap getInjectable() {
        return context;
    }

    public String getSceneEngineId() {
        return sceneEngineId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public Object findCurrent() {
        return current;
    }

    public <T> T findCurrentAs(Class<T> c) {
        Object o = findCurrent();
        return c.isInstance(o) ? (T) o : null;
    }

    public <T> T find(Class<T> c,String name) {
        return (T) context.find(c, name);
    }

    public <T> T get(Class<T> c,String name) {
        return (T) context.get(c, name);
    }

    public <T> void register(Class<T> c, T instance,String name) {
        context.add(c, name, instance);
    }

}