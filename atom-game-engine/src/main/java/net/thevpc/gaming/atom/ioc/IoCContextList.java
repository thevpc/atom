package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.engine.DefaultSceneEngine;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.Game;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IoCContextList {
    Map<String, IoCContext> uniques = new HashMap<>();
    Map<Class, List<IoCContext>> classContexts = new HashMap<>();
    AtomIoCContainer container;
    Game game;

    public IoCContextList(AtomIoCContainer container, Game game) {
        this.container = container;
        this.game = game;
    }

    public IoCContext add(Class clazz, String sceneEngineId, String sceneId) {
        String uniqueId = clazz.getName() + ":" + sceneEngineId + ":" + sceneId;
        IoCContext old = uniques.get(uniqueId);
        if(old!=null){
            return old;
        }
        Object o = container.instantiate(clazz);
        if(o instanceof DefaultScene){
            ((DefaultScene) o).setId(sceneId);
        }
        IoCContext ioCContext = new IoCContext(o, sceneEngineId, sceneId);
        ioCContext.register(Game.class, game,null);
        ioCContext.register(GameEngine.class, game.getGameEngine(),null);
        for (Annotation clazzAnnotation : clazz.getAnnotations()) {
            ioCContext.register((Class) clazzAnnotation.annotationType(), clazzAnnotation,null);
        }
        List<IoCContext> list = classContexts.get(clazz);
        if(list==null){
            list=new ArrayList<>();
            classContexts.put(clazz,list);
        }
        list.add(ioCContext);
        uniques.put(uniqueId,ioCContext);
        return ioCContext;
    }

    public List<IoCContext> find(Class type, String sceneEngineId, String sceneId) {
        List<IoCContext> a = classContexts.get(type);
        if (a == null) {
            return new ArrayList<>();
        }
        ArrayList<IoCContext> filtered = new ArrayList<>();
        for (IoCContext c : a) {
            if (sceneEngineId == null || sceneEngineId.equals(c.getSceneEngineId())) {
                if (sceneId == null || sceneId.equals(c.getSceneId())) {
                    filtered.add(c);
                }
            }
        }
        return filtered;
    }

    public List<IoCContext> findAll() {
        ArrayList<IoCContext> all = new ArrayList<>();
        for (List<IoCContext> value : classContexts.values()) {
            all.addAll(value);
        }
        return all;
    }

    public List<IoCContext> findByClass(Class type) {
        List<IoCContext> a = classContexts.get(type);
        if (a == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(a);
    }
}
