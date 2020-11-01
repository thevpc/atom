package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomSpriteView;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.presentation.Game;
import net.vpc.gaming.atom.presentation.LocationIndicatorView;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SpriteView;

import java.util.*;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteViewCreationAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Class clazz;
    private final AtomSpriteView s;
    private final Game game;

    public AtomSpriteViewCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Class clazz, AtomSpriteView s, Game game) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.clazz = clazz;
        this.s = s;
        this.game = game;
    }

    @Override
    public boolean isRunnable() {
        if (s.scene().isEmpty()) {
            return true;
        }
        for (String n : AtomAnnotationsProcessor.split(s.scene())) {
            if (!atomAnnotationsProcessor.container.contains(n, "Scene")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SPRITE_VIEW;
    }

    @Override
    public void run() {
        for (String sceneId : AtomAnnotationsProcessor.splitOrAddEmpty(s.scene())) {
            final List<Scene> scenes = sceneId.isEmpty() ? game.getScenes() : Arrays.asList(game.getScene(sceneId));
            for (Scene scene : scenes) {
                runOneScene(scene);
            }
        }
    }

    private void runOneScene(Scene scene) {
        InstancePreparator prep = new InstancePreparator() {
            @Override
            public void postInject(Object o) {

            }
        };
        SpriteView spriteView = null;
        Object instance = null;
        HashMap<Class, Object> injects = new HashMap<>();
        injects.put(Game.class, game);
        injects.put(GameEngine.class, game.getGameEngine());
        injects.put(Scene.class, scene);
        injects.put(SceneEngine.class, scene.getSceneEngine());

        if (SpriteView.class.isAssignableFrom(clazz)) {
            spriteView = (SpriteView) atomAnnotationsProcessor.container.create(clazz, new InstancePreparator[]{
                    new InstancePreparator() {
                        @Override
                        public void preInject(Object o, Map<Class, Object> injects) {
                            injects.put(SpriteView.class,o);
                        }
                    },
                    prep
            }, injects);
            instance = spriteView;
        } else {
            spriteView = new LocationIndicatorView();
            injects.put(SpriteView.class, spriteView);
            instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
            prep.postInject(spriteView);
        }
        scene.setSpriteView(s.kind(), spriteView);
        atomAnnotationsProcessor.container.register(
                scene.getId() +"/"+instance.getClass().getName()
                , "SpriteView", instance);
        atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
    }
}
