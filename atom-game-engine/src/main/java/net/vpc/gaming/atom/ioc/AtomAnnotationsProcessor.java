package net.vpc.gaming.atom.ioc;

import net.vpc.common.vclasspath.AnnotationVisitor;
import net.vpc.gaming.atom.annotations.*;
import net.vpc.gaming.atom.presentation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by vpc on 9/23/16.
 */
public class AtomAnnotationsProcessor implements AnnotationVisitor {
    public static final int ORDER_SCENE_ENGINE = 100;
    public static final int ORDER_SCENE = 200;
    public static final int ORDER_SPRITE = 300;
    public static final int ORDER_SPRITE_VIEW = 400;
    public static final int ORDER_SPRITE_COLLISION_MANAGER = 400;
    public static final int ORDER_SCENE_CONTROLLER = 400;
    public static final int ORDER_METHOD = 1000;

    java.util.List<IndexedPostponedAction> postponedActions = new ArrayList();
    AtomIoCContainer container;

    public AtomAnnotationsProcessor(AtomIoCContainer container) {
        this.container = container;
    }

    @Override
    public void visitTypeAnnotation(Annotation annotation, final Class clazz) {
        final Game game = getGame();
        if (annotation.annotationType().equals(AtomScene.class)) {
            final AtomScene s = (AtomScene) annotation;
            //scenes.put(s.id(),null);
            add(new AtomSceneCreationAction(this, s, clazz, game));
        } else if (annotation.annotationType().equals(AtomSceneEngine.class)) {
            final AtomSceneEngine s = (AtomSceneEngine) annotation;
            //scenes.put(s.id(),null);
            add(new AtomSceneEngineCreationAction(this, s, clazz, game));
        } else if (annotation.annotationType().equals(AtomSprite.class)) {
            final AtomSprite s = (AtomSprite) annotation;
            //scenes.put(s.id(),null);
            add(new AtomSpriteCreationAction(this, clazz, s, game));
        } else if (annotation.annotationType().equals(AtomSpriteView.class)) {
            final AtomSpriteView s = (AtomSpriteView) annotation;
            //scenes.put(s.id(),null);
            add(new AtomSpriteViewCreationAction(this, clazz, s, game));
        } else if (annotation.annotationType().equals(AtomSpriteCollisionManager.class)) {
            final AtomSpriteCollisionManager s = (AtomSpriteCollisionManager) annotation;
            //scenes.put(s.id(),null);
            add(new AtomSpriteCollisionManagerCreationAction(this, clazz, s, game));
        } else if (annotation.annotationType().equals(AtomSpriteTask.class)) {
            final AtomSpriteTask s = (AtomSpriteTask) annotation;
            //scenes.put(s.id(),null);
            add(new AtomSpriteTaskCreationAction(this, clazz, s, game));
        } else if (annotation.annotationType().equals(AtomSceneController.class)) {
            final AtomSceneController s = (AtomSceneController) annotation;
            //scenes.put(s.id(),null);
            add(new AtomControllerCreationAction(this, clazz, s, game));
        }
    }


    @Override
    public void visitMethodAnnotation(Annotation annotation, final Method method) {
        if (annotation.annotationType().equals(OnSceneStarted.class)) {
            add(new SceneStartedMethodAction(this,method));
        } else if (annotation.annotationType().equals(OnSceneInitialized.class)) {
            add(new SceneInitializedMethodAction(this,method));
        } else if (annotation.annotationType().equals(OnSceneStopped.class)) {
            add(new SceneStoppedMethodAction(this,method));
        } else if (annotation.annotationType().equals(OnNextFrame.class)) {
            add(new NextFrameMethodAction(this,method));
        } else if (annotation.annotationType().equals(OnSceneActivating.class)) {
            add(new SceneEngineActivatingMethodAction(this,method));
        }
    }

    @Override
    public void visitFieldAnnotation(Annotation annotation, Field field) {

    }

    public void build() {
        while (true) {
            int x = 0;
            int vcount = postponedActions.size();
            Collections.sort(postponedActions, new Comparator<IndexedPostponedAction>() {
                @Override
                public int compare(IndexedPostponedAction o1, IndexedPostponedAction o2) {
                    int x = Integer.compare(o1.action.getOrder(), o2.action.getOrder());
                    if (x != 0) {
                        return x;
                    }
                    x = Integer.compare(o1.index, o2.index);
                    if (x != 0) {
                        return x;
                    }
                    return 0;
                }
            });
            while (x < postponedActions.size()) {
                if (postponedActions.get(x).isRunnable()) {
                    IndexedPostponedAction d = postponedActions.remove(x);
                    d.run();
                } else {
                    x++;
                }
            }
            int vcount2 = postponedActions.size();
            if (vcount2 == 0) {
                break;
            }
            if (vcount2 == vcount) {
                throw new RuntimeException("Problem");
            }
        }
    }

    private void add(PostponedAction action) {
        postponedActions.add(new IndexedPostponedAction(
                postponedActions.size(), action
        ));
    }

    public static Set<String> split(String name) {
        HashSet<String> all = new HashSet<>();
        for (String s : name.split(" +|,")) {
            if (s.length() > 0) {
                all.add(s);
            }
        }
        return all;
    }

    public Game getGame() {
        return (Game) container.getBean(Game.class);
    }

    private static class IndexedPostponedAction implements Comparable<IndexedPostponedAction> {
        private int index;
        private PostponedAction action;

        public IndexedPostponedAction(int index, PostponedAction action) {
            this.index = index;
            this.action = action;
        }

        public boolean isRunnable() {
            return action.isRunnable();
        }

        public void run() {
            action.run();
        }

        @Override
        public int compareTo(IndexedPostponedAction o) {

            int x = Integer.compare(action.getOrder(), o.action.getOrder());
            if (x != 0) {
                return x;
            }
            x = Integer.compare(index, o.index);
            if (x != 0) {
                return x;
            }
            return 0;
        }
    }
}
