package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.OnInstall;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.RatioDimension;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSceneCreationAction implements PostponedAction {

    private final AtomScene s;
    private final Class clazz;
    private final Game game;
    private AtomAnnotationsProcessor atomAnnotationsProcessor;

    public AtomSceneCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, AtomScene s, Class clazz, Game game) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.s = s;
        this.clazz = clazz;
        this.game = game;
    }

    @Override
    public boolean isRunnable() {
//        if (effSceneId().isEmpty()) {
//            return true;
//        }
        for (String n : AtomAnnotationsProcessor.split(effSceneId())) {
            if (!atomAnnotationsProcessor.container.contains(n, "SceneEngine")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SCENE;
    }

    @Override
    public void run() {
        String sceneId = s.id();
        if (sceneId.isEmpty()) {
            sceneId = clazz.getSimpleName();
        }
        Set<String> sceneEngineIds = new HashSet<>();
//        if (effSceneId().isEmpty()) {
//            sceneEngineIds.add("");
//        } else {
        sceneEngineIds.addAll(AtomAnnotationsProcessor.split(effSceneId()));
        if (sceneEngineIds.isEmpty()) {
            sceneEngineIds.add("");
        }
//        }
        for (String sceneEngineId : sceneEngineIds) {
            SceneEngine sceneEngine = game.getGameEngine().containsScene(sceneId)
                    ? game.getGameEngine().getScene(sceneEngineId)
                    : game.getGameEngine().createScene(sceneId);
            Object instance = null;
            InstancePreparator prep = new InstancePreparator() {
                @Override
                public void postInject(Object o) {
                    Scene scene = (Scene) o;
                    scene.setTitle(s.title());
                    scene.setTileSize(new ViewDimension(
                            s.tileWidth(),
                            s.tileHeight(),
                            s.tileAltitude()
                    ));
                    scene.setIsometric(s.isometric());
                    scene.getCamera().setDimension(
                            new RatioDimension(
                                    s.cameraWidth(),
                                    s.cameraHeight()
                            )
                    );
                }
            };
            if (Scene.class.isAssignableFrom(clazz)) {

                HashMap<Class, Object> injects = new HashMap<>();
                injects.put(SceneEngine.class, sceneEngine);
                injects.put(Game.class, game);
                injects.put(GameEngine.class, game.getGameEngine());
                Scene scene = (Scene) atomAnnotationsProcessor.container.create(clazz, new InstancePreparator[]{
                        new InstancePreparator() {
                            @Override
                            public void preInject(Object o, Map<Class, Object> injects) {
                                injects.put(Scene.class, o);
                            }
                        },
                        prep
                }, injects);
                game.addScene(scene, sceneEngine.getId());
                instance = scene;
            } else {
                DefaultScene scene = new DefaultScene(
                        new ViewDimension(
                                s.tileWidth(),
                                s.tileHeight(),
                                s.tileAltitude()
                        )
                );
                prep.postInject(scene);
                HashMap<Class, Object> injects = new HashMap<>();
                injects.put(SceneEngine.class, sceneEngine);
                injects.put(Scene.class, scene);
                injects.put(Game.class, game);
                injects.put(GameEngine.class, game.getGameEngine());
                instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
                game.addScene(scene, sceneEngine.getId());
                scene.setCompanionObject(instance);
            }
            atomAnnotationsProcessor.container.register(sceneId, "Scene", instance);
            atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);

        }
    }

    private String effSceneId() {
        String se = s.engine();
        if (se.isEmpty()) {
            se = s.id();
        }
        return se;
    }
}
