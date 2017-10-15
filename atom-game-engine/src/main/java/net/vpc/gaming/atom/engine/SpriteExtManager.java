package net.vpc.gaming.atom.engine;

import net.vpc.gaming.atom.ioc.AtomIoCContainer;
import net.vpc.gaming.atom.ioc.InstancePreparator;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vpc on 9/25/16.
 */
public class SpriteExtManager<T> {
    private Map<Integer, T> extInstById = new HashMap<Integer, T>();
    private Map<Integer, Class<? extends T>> extClassById = new HashMap<Integer, Class<? extends T>>();
    private Map<String, T> extInstByKind = new HashMap<String, T>();
    private Map<String, Class<? extends T>> extClassByKind = new HashMap<String, Class<? extends T>>();
    private SceneEngine engine;

    public SpriteExtManager(SceneEngine engine) {
        this.engine = engine;
    }

    public T getInstanceById(int id){
        return extInstById.get(id);
    }

    public T getInstanceByKind(String kind){
        return extInstByKind.get(kind);
    }

    public Class<? extends T> getBeanById(int id){
        return extClassById.get(id);
    }

    public Class<? extends T> getBeanByKind(String kind){
        return extClassByKind.get(kind);
    }

    public void setInstanceById(int id, T instance){
        if(instance==null){
            extInstById.remove(id);
        }else{
            extInstById.put(id,instance);
        }
    }

    public void setBeanById(int id,Class<? extends T> type){
        if(type==null){
            extClassById.remove(id);
        }else{
            extClassById.put(id,type);
        }
    }

    public void setInstanceByKind(String kind,T instance){
        if(instance==null){
            extInstByKind.remove(kind);
        }else{
            extInstByKind.put(kind,instance);
        }
    }

    public void setBeanByKind(String kind,Class<? extends T> type){
        if(type==null){
            extClassByKind.remove(kind);
        }else{
            extClassByKind.put(kind,type);
        }
    }

    public T create(Class<? extends T> type){
        AtomIoCContainer container=engine.getGameEngine().getContainer();
        HashMap<Class, Object> map = new HashMap<>();
        map.put(SceneEngine.class,engine);
        map.put(GameEngine.class,engine.getGameEngine());
        return (T) container.create(type, new InstancePreparator() {
            @Override
            public void prepare(Object o) {

            }
        }, map);
    }


    public T get(Sprite s){
        AtomIoCContainer container=engine.getGameEngine().getContainer();
        T t = extInstById.get(s.getId());
        if(t!=null){
            return t;
        }
        Class<? extends T> t2 = extClassById.get(s.getId());
        if(t2!=null){
            HashMap<Class, Object> map = new HashMap<>();
            map.put(SceneEngine.class,engine);
            map.put(GameEngine.class,engine.getGameEngine());
            return (T) container.create(t2, new InstancePreparator() {
                @Override
                public void prepare(Object o) {

                }
            }, map);
        }

        t = extInstByKind.get(s.getKind());
        if(t!=null){
            return t;
        }
        t2 = extClassByKind.get(s.getKind());
        if(t2!=null){
            HashMap<Class, Object> map = new HashMap<>();
            map.put(SceneEngine.class,engine);
            map.put(GameEngine.class,engine.getGameEngine());
            return (T) container.create(t2, new InstancePreparator() {
                @Override
                public void prepare(Object o) {

                }
            }, map);
        }
        return null;
    }
}
