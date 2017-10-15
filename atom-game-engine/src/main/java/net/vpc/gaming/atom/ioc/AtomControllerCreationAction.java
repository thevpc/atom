package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomSceneController;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.presentation.Game;
import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneController;

import java.util.HashMap;

/**
 * Created by vpc on 10/7/16.
 */
class AtomControllerCreationAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Class clazz;
    private final AtomSceneController s;
    private final Game game;

    public AtomControllerCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Class clazz, AtomSceneController s, Game game) {
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
        return AtomAnnotationsProcessor.ORDER_SCENE_CONTROLLER;
    }

    @Override
    public void run() {
        for (String sceneId : AtomAnnotationsProcessor.split(s.scene())) {
            final Scene scene = game.getScene(sceneId);

            InstancePreparator prep = new InstancePreparator() {
                @Override
                public void prepare(Object o) {

                }
            };
            SceneController spriteController = null;
            Object instance = null;
            HashMap<Class, Object> injects = new HashMap<>();
            injects.put(Game.class, game);
            injects.put(GameEngine.class, game.getGameEngine());
            injects.put(SceneEngine.class, scene.getSceneEngine());
            injects.put(Scene.class, scene);

            if (SceneController.class.isAssignableFrom(clazz)) {
                spriteController = (SceneController) atomAnnotationsProcessor.container.create(clazz, prep, injects);
                instance = spriteController;
            } else {
                throw new IllegalArgumentException("Controller must implement SceneController inteface");
//                spriteController = new SceneController();
//                injects.put(SceneController.class, spriteController);
//                instance = container.create(clazz, null, injects);
//                prep.prepare(spriteController);
            }
            atomAnnotationsProcessor.container.register(
                    sceneId+"/"+instance.getClass().getName()
                    , "SceneController", instance);
            scene.addSceneController(spriteController);
            atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
        }
    }
}
