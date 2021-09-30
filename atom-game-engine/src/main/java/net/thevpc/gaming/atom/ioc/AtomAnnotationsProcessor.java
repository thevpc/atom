package net.thevpc.gaming.atom.ioc;

import net.thevpc.common.classpath.AnnotationVisitor;
import net.thevpc.gaming.atom.annotations.*;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by vpc on 9/23/16.
 */
public class AtomAnnotationsProcessor implements AnnotationVisitor {
    public static final int ORDER_SCENE_ENGINE = 100;

    public static final int ORDER_SCENE = 200;

    public static final int ORDER_AFTER_SCENES = 299;
    public static final int ORDER_SPRITE = 300;
    public static final int ORDER_SPRITE_COLLISION_MANAGER = 400;

    public static final int ORDER_SPRITE_VIEW = 500;
    public static final int ORDER_SCENE_CONTROLLER = 600;
    public static final int ORDER_INJECT = 1000;
    public static final int ORDER_METHOD = 2000;

    java.util.List<IndexedPostponedAction> postponedActions = new ArrayList();
    AtomIoCContainer container;
    IoCContextList contextList;

    public AtomAnnotationsProcessor(AtomIoCContainer container) {
        this.container = container;
        contextList = new IoCContextList(container, getGame());
        add(new AbstractPostponedAction(ORDER_INJECT) {
                @Override
                public void run() {
                    for (IoCContext c : contextList.findAll()) {
                        ClassNamedObjectMap injectable = c.getInjectable();
                        if(injectable.find(SceneEngine.class,null)==null){
                            injectable.add(SceneEngine.class,null,getGame().getSceneEngine(c.getSceneEngineId()));
                        }
                        if(injectable.find(Scene.class,null)==null){
                            injectable.add(Scene.class,null,getGame().getScene(c.getSceneEngineId()));
                        }
                        container.inject(c.findCurrent(), null, injectable);
                    }
                }

                @Override
                public String toString() {
                    return "INJECT FIELDS AND METHODS";
                }
            }
        );
    }

    public static Set<String> splitOrAddEmpty(String name) {
        Set<String> u = split(name);
        if (u.isEmpty()) {
            u = new HashSet<>();
            u.add("");
        }
        return u;
    }

    public static Set<String> split(String name) {
        HashSet<String> all = new HashSet<>();
        for (String s : name.split(" +|,|;")) {
            if (s.length() > 0) {
                all.add(s);
            }
        }
        return all;
    }

    private Set<String> sceneEngineIds0(Class clazz) {
        LinkedHashSet<String> engineIds = new LinkedHashSet<>();
        {
            AtomSceneEngine s = (AtomSceneEngine) clazz.getAnnotation(AtomSceneEngine.class);
            if (s != null && s.id().trim().length() > 0) {

                engineIds.addAll(split(s.id()));
            }
        }
        if (!engineIds.isEmpty()) {
            return engineIds;
        }
        {
            AtomScene s = (AtomScene) clazz.getAnnotation(AtomScene.class);
            if (s != null && !s.sceneEngine().isEmpty()) {
                engineIds.addAll(split(s.id()));
            }else if (s != null && !s.id().isEmpty()) {
                engineIds.add(s.id());
            }
        }
        if (!engineIds.isEmpty()) {
            return engineIds;
        }
        {
            AtomSprite s = (AtomSprite) clazz.getAnnotation(AtomSprite.class);
            if (s != null && !s.sceneEngine().isEmpty()) {
                engineIds.addAll(split(s.sceneEngine()));
            }
        }
        if (!engineIds.isEmpty()) {
            return engineIds;
        }
        {
            AtomSpriteCollisionTask s = (AtomSpriteCollisionTask) clazz.getAnnotation(AtomSpriteCollisionTask.class);
            if (s != null && !s.sceneEngine().isEmpty()) {
                engineIds.addAll(split(s.sceneEngine()));
            }
        }
        if (!engineIds.isEmpty()) {
            return engineIds;
        }
        {
            AtomSpriteMainTask s = (AtomSpriteMainTask) clazz.getAnnotation(AtomSpriteMainTask.class);
            if (s != null && !s.sceneEngine().isEmpty()) {
                engineIds.addAll(split(s.sceneEngine()));
            }
        }
        return engineIds;
    }

    private Set<String> sceneIds0(Class clazz) {
        LinkedHashSet<String> result = new LinkedHashSet<>();
        AtomSceneController sc=(AtomSceneController)clazz.getAnnotation(AtomSceneController.class);
        if (sc != null && !sc.scene().isEmpty()) {
            result.addAll(split(sc.scene()));
        }
        if (!result.isEmpty()) {
            return result;
        }
        AtomSpriteView sv=(AtomSpriteView)clazz.getAnnotation(AtomSpriteView.class);
        if (sv != null && !sv.scene().isEmpty()) {
            result.addAll(split(sv.scene()));
        }
        if (!result.isEmpty()) {
            return result;
        }
        AtomScene s = (AtomScene) clazz.getAnnotation(AtomScene.class);
        if (s != null && !s.id().isEmpty()) {
            result.add(s.id());
        }
        if (result.isEmpty()) {
            {
                AtomSceneEngine se = (AtomSceneEngine) clazz.getAnnotation(AtomSceneEngine.class);
                if (se != null && se.id().trim().length() > 0) {

                    result.add(se.id().trim());
                }
            }
        }
        return result;
    }

    private Set<String> sceneEngineIds(Class clazz) {
        boolean effScene = false;
        boolean effSceneEngine = false;
        Set<String> engineIds = new HashSet<>();
        {
            AtomScene s = (AtomScene) clazz.getAnnotation(AtomScene.class);
            if (s != null) {
                effScene = true;
                engineIds.addAll(split(s.sceneEngine().trim()));
            }
        }
        {
            AtomSprite s = (AtomSprite) clazz.getAnnotation(AtomSprite.class);
            if (s != null) {
                engineIds.addAll(split(s.sceneEngine().trim()));
            }
        }
        {
            AtomSpriteMainTask s = (AtomSpriteMainTask) clazz.getAnnotation(AtomSpriteMainTask.class);
            if (s != null) {
                engineIds.addAll(split(s.sceneEngine().trim()));
            }
        }
        {
            AtomSpriteCollisionTask s = (AtomSpriteCollisionTask) clazz.getAnnotation(AtomSpriteCollisionTask.class);
            if (s != null) {
                engineIds.addAll(split(s.sceneEngine().trim()));
            }
        }
        {
            AtomSceneEngine s = (AtomSceneEngine) clazz.getAnnotation(AtomSceneEngine.class);
            if (s != null) {
                effSceneEngine = true;
            }
            if (s != null && s.id().trim().length() > 0) {

                engineIds.add(s.id().trim());
            }
        }
        if (engineIds.isEmpty()) {
            AtomScene s = (AtomScene) clazz.getAnnotation(AtomScene.class);
            if (s != null && !s.id().isEmpty()) {
                engineIds.add(s.id());
            }
        }
        if (engineIds.isEmpty()) {
            if (effSceneEngine || effScene) {
                engineIds.add(clazz.getSimpleName());
            } else {
                engineIds.add("*");
            }
        }
        return engineIds;
    }

    private Set<String> sceneIds(Class clazz) {
        boolean eff = false;
        Set<String> ids = new HashSet<>();
        {
            AtomSpriteView sp = (AtomSpriteView) clazz.getAnnotation(AtomSpriteView.class);
            if (sp != null) {
                ids.addAll(split(sp.scene()));
            }
        }
        {
            AtomSceneController sp = (AtomSceneController) clazz.getAnnotation(AtomSceneController.class);
            if (sp != null) {
                ids.addAll(split(sp.scene()));
            }
        }
        {
            AtomScene s = (AtomScene) clazz.getAnnotation(AtomScene.class);
            if (s != null) {
                eff = true;
                if (s.id().length() > 0) {
                    ids.add(s.id());
                }
            }
        }
        if (ids.isEmpty()) {
            ids.addAll(sceneEngineIds(clazz));
        }
        return ids;
    }


    private List<SceneEngine> findSceneEngines(List<String> all) {
        if (all.isEmpty()) {
            return getGame().getGameEngine().getSceneEngines();
        }
        List<SceneEngine> r = new ArrayList<>();
        for (SceneEngine e : getGame().getGameEngine().getSceneEngines()) {
            boolean accepted = false;
            for (String s : all) {
                if (s.equals(e.getId())) {
                    accepted = true;
                    break;
                }
            }
            if (accepted) {
                r.add(e);
            }
        }
        return r;
    }

    private List<Scene> findScenes(List<String> all, String sceneEngine) {
        if (all.isEmpty()) {
            if (sceneEngine == null) {
                return getGame().getScenes();
            }
            return getGame().getScenes().stream().filter(x -> x.getSceneEngine().getId().equals(sceneEngine)).collect(Collectors.toList());
        }
        List<Scene> r = new ArrayList<>();
        for (Scene e : getGame().getScenes()) {
            boolean accepted = false;
            for (String s : all) {
                if (s.equals(e.getId())) {
                    if (sceneEngine == null || sceneEngine.equals(e.getSceneEngine().getId())) {
                        accepted = true;
                    }
                    break;
                }
            }
            if (accepted) {
                r.add(e);
            }
        }
        return r;
    }

    @Override
    public void visitClassAnnotation(Annotation annotation, final Class clazz) {
        if (annotation.annotationType().equals(AtomScene.class)) {
            for (String s : sceneEngineIds0(clazz)) {
                Set<String> ss = sceneIds0(clazz);
                if(ss.size()>1){
                    throw new IllegalArgumentException("too many scenes");
                }
                if(ss.isEmpty()){
                    ss=new HashSet<>(Arrays.asList(s));
                }
                add(new AtomSceneCreationAction(this, contextList.add(clazz,s, new ArrayList<>(ss).get(0))));
            }
        } else if (annotation.annotationType().equals(AtomSceneEngine.class)) {
            for (String s : sceneEngineIds0(clazz)) {
                Set<String> ss = sceneIds0(clazz);
                if(ss.size()>1){
                    throw new IllegalArgumentException("too many scenes");
                }
                if(ss.isEmpty()){
                    ss=new HashSet<>(Arrays.asList(s));
                }
                add(new AtomSceneEngineCreationAction(this, contextList.add(clazz,s, new ArrayList<>(ss).get(0))));
            }
        } else if (annotation.annotationType().equals(AtomSprite.class)) {
            add(new AbstractPostponedAction(ORDER_AFTER_SCENES) {
                @Override
                public void run() {
                    for (SceneEngine sceneEngine : findSceneEngines(new ArrayList<>(sceneEngineIds0(clazz)))) {
                        for (Scene scene : findScenes(new ArrayList<>(sceneIds0(clazz)), sceneEngine.getId())) {
                            add(new AtomSpriteCreationAction(AtomAnnotationsProcessor.this, contextList.add(clazz, sceneEngine.getId(), scene.getId())));
                        }
                    }
                }

                @Override
                public String toString() {
                    return "DEFER @AtomSprite";
                }
            });
        } else if (annotation.annotationType().equals(AtomSpriteView.class)) {
            add(new AbstractPostponedAction(ORDER_AFTER_SCENES) {
                @Override
                public void run() {
                    for (SceneEngine sceneEngine : findSceneEngines(new ArrayList<>(sceneEngineIds0(clazz)))) {
                        for (Scene scene : findScenes(new ArrayList<>(sceneIds0(clazz)), sceneEngine.getId())) {
                            add(new AtomSpriteViewCreationAction(AtomAnnotationsProcessor.this, contextList.add(clazz, sceneEngine.getId(), scene.getId())));
                        }
                    }
                }
                @Override
                public String toString() {
                    return "DEFER @AtomSpriteView";
                }
            });

        } else if (annotation.annotationType().equals(AtomSpriteCollisionTask.class)) {
            add(new AbstractPostponedAction(ORDER_AFTER_SCENES) {
                @Override
                public void run() {
                    List<SceneEngine> sceneEngines = findSceneEngines(new ArrayList<>(sceneEngineIds0(clazz)));
                    for (SceneEngine sceneEngine : sceneEngines) {
                        List<Scene> scenes = findScenes(new ArrayList<>(sceneIds0(clazz)), sceneEngine.getId());
                        for (Scene scene : scenes) {
                            add(new AtomSpriteCollisionManagerCreationAction(AtomAnnotationsProcessor.this, contextList.add(clazz, sceneEngine.getId(), scene.getId())));
                        }
                    }
                }
                @Override
                public String toString() {
                    return "DEFER @AtomSpriteCollisionTask";
                }
            });
        } else if (annotation.annotationType().equals(AtomSpriteMainTask.class)) {
            add(new AbstractPostponedAction(ORDER_AFTER_SCENES) {
                @Override
                public void run() {
                    for (SceneEngine sceneEngine : findSceneEngines(new ArrayList<>(sceneEngineIds0(clazz)))) {
                        for (Scene scene : findScenes(new ArrayList<>(sceneIds0(clazz)), sceneEngine.getId())) {
                            add(new AtomSpriteTaskCreationAction(AtomAnnotationsProcessor.this, contextList.add(clazz, sceneEngine.getId(), scene.getId())));
                        }
                    }
                }
                @Override
                public String toString() {
                    return "DEFER @AtomSpriteMainTask";
                }
            });
        } else if (annotation.annotationType().equals(AtomSceneController.class)) {
            add(new AbstractPostponedAction(ORDER_AFTER_SCENES) {
                @Override
                public void run() {
                    List<SceneEngine> sceneEngines = findSceneEngines(new ArrayList<>(sceneEngineIds0(clazz)));
                    for (SceneEngine sceneEngine : sceneEngines) {
                        List<Scene> scenes = findScenes(new ArrayList<>(sceneIds0(clazz)), sceneEngine.getId());
                        for (Scene scene : scenes) {
                            add(new AtomSceneControllerCreationAction(AtomAnnotationsProcessor.this, contextList.add(clazz, sceneEngine.getId(), scene.getId())));
                        }
                    }
                }
                @Override
                public String toString() {
                    return "DEFER @AtomSceneController";
                }
            });
        }
    }

    @Override
    public void visitMethodAnnotation(Annotation annotation, final Method method) {
        if (annotation.annotationType().equals(OnSceneStarted.class)) {
            List<IoCContext> ioCContexts = contextList.findByClass(method.getDeclaringClass());
            for (IoCContext ioCContext : ioCContexts) {
                add(new SceneStartedMethodAction(this, method, ioCContext.findCurrent()));
            }
        } else if (annotation.annotationType().equals(OnSceneInitialized.class)) {
            List<IoCContext> ioCContexts = contextList.findByClass(method.getDeclaringClass());
            for (IoCContext ioCContext : ioCContexts) {
                add(new SceneInitializedMethodAction(this, method, ioCContext.findCurrent()));
            }
        } else if (annotation.annotationType().equals(OnSceneStopped.class)) {
            List<IoCContext> ioCContexts = contextList.findByClass(method.getDeclaringClass());
            for (IoCContext ioCContext : ioCContexts) {
                add(new SceneStoppedMethodAction(this, method, ioCContext.findCurrent()));
            }
        } else if (annotation.annotationType().equals(OnNextFrame.class)) {
            List<IoCContext> ioCContexts = contextList.findByClass(method.getDeclaringClass());
            for (IoCContext ioCContext : ioCContexts) {
                add(new NextFrameMethodAction(this, method, ioCContext.findCurrent()));
            }
        } else if (annotation.annotationType().equals(OnSceneActivating.class)) {
            List<IoCContext> ioCContexts = contextList.findByClass(method.getDeclaringClass());
            for (IoCContext ioCContext : ioCContexts) {
                add(new SceneEngineActivatingMethodAction(this, method, ioCContext.findCurrent()));
            }
        }
    }

    @Override
    public void visitFieldAnnotation(Annotation annotation, Field field) {

    }

    public void build() {

        while (true) {
            int x = 0;
            int vcount = postponedActions.size();
            Collections.sort(postponedActions, new IndexedPostponedActionComparator());
            boolean someUpdates=false;
            List<IndexedPostponedAction> unprocessable=new ArrayList<>();
            while (x < postponedActions.size()) {
                IndexedPostponedAction aa = postponedActions.get(x);
                if (aa.isRunnable()) {
                    IndexedPostponedAction d = postponedActions.remove(x);
                    int before=postponedActions.size();
                    d.run();
                    int after=postponedActions.size();
                    if(after!=before){
                        someUpdates=true;
                        break;
                    }
                } else {
                    unprocessable.add(aa);
                    x++;
                }
            }
            if(!someUpdates) {
                int vcount2 = postponedActions.size();
                if (vcount2 == 0) {
                    break;
                }
                if (vcount2 == vcount) {
                    System.err.println("unable to process :");
                    for (IndexedPostponedAction indexedPostponedAction : unprocessable) {
                        System.err.println("\t "+indexedPostponedAction);
                    }
                    throw new RuntimeException("Problem");
                }
            }
        }
    }

    private void add(PostponedAction action) {
        postponedActions.add(new IndexedPostponedAction(
                postponedActions.size(), action
        ));
    }

    public Game getGame() {
        return (Game) container.getBean(Game.class);
    }

    private static class IndexedPostponedActionComparator implements Comparator<IndexedPostponedAction> {
        @Override
        public int compare(IndexedPostponedAction o1, IndexedPostponedAction o2) {
            int x = Integer.compare(o1.getAction().getOrder(), o2.getAction().getOrder());
            if (x != 0) {
                return x;
            }
            x = Integer.compare(o1.getIndex(), o2.getIndex());
            return x;
        }
    }
}
