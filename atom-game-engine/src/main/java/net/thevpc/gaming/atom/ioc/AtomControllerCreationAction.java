package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSceneController;
import net.thevpc.gaming.atom.annotations.OnInstall;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vpc on 10/7/16.
 */
class AtomControllerCreationAction implements PostponedAction {
    private final Class clazz;
    private final AtomSceneController s;
    private final Game game;
    private AtomAnnotationsProcessor atomAnnotationsProcessor;

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
        for (String n : AtomAnnotationsProcessor.splitOrAddEmpty(s.scene())) {
            if (n.isEmpty()) {
                return true;
            }
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
        for (String sceneId : AtomAnnotationsProcessor.splitOrAddEmpty(s.scene())) {
            final List<Scene> scenes = sceneId.isEmpty() ? game.getScenes() : Arrays.asList(game.getScene(sceneId));
            for (Scene scene : scenes) {
                InstancePreparator prep = new InstancePreparator() {
                    @Override
                    public void postInject(Object o) {

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
                    spriteController = (SceneController) atomAnnotationsProcessor.container.create(clazz, new InstancePreparator[]{
                            new InstancePreparator() {
                                @Override
                                public void preInject(Object o, Map<Class, Object> injects) {
                                    injects.put(SceneController.class, o);
                                }
                            }
                            , prep
                    }, injects);
                    instance = spriteController;
                } else {
                    throw new IllegalArgumentException("Controller must implement SceneController inteface");
//                spriteController = new SceneController();
//                injects.put(SceneController.class, spriteController);
//                instance = container.create(clazz, null, injects);
//                prep.prepare(spriteController);
                }
                atomAnnotationsProcessor.container.register(
                        scene.getId() + "/" + instance.getClass().getName()
                        , "SceneController", instance);
                scene.addController(spriteController);
                atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
            }

        }
    }
}
