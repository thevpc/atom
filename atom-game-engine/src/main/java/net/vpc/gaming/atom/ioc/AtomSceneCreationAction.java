package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.RatioDimension;
import net.vpc.gaming.atom.model.ViewDimension;
import net.vpc.gaming.atom.presentation.DefaultScene;
import net.vpc.gaming.atom.presentation.Game;
import net.vpc.gaming.atom.presentation.Scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSceneCreationAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final AtomScene s;
    private final Class clazz;
    private final Game game;

    public AtomSceneCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, AtomScene s, Class clazz, Game game) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.s = s;
        this.clazz = clazz;
        this.game = game;
    }

    @Override
    public boolean isRunnable() {
        if (s.engine().isEmpty()) {
            return true;
        }
        for (String n : AtomAnnotationsProcessor.split(s.engine())) {
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
        if (s.engine().isEmpty()) {
            sceneEngineIds.add("");
        } else {
            sceneEngineIds.addAll(AtomAnnotationsProcessor.split(s.engine()));
            if (sceneEngineIds.isEmpty()) {
                sceneEngineIds.add("");
            }
        }
        for (String sceneEngineId : sceneEngineIds) {
            if (sceneEngineId.isEmpty()) {
                sceneEngineId = sceneId;
            }
            SceneEngine sceneEngine = game.getGameEngine().containsSceneEngine(sceneId) ?
                    game.getGameEngine().getSceneEngine(sceneEngineId) :
                    game.getGameEngine().createSceneEngine(sceneId);
            Object instance = null;
            InstancePreparator prep = new InstancePreparator() {
                @Override
                public void prepare(Object o) {
                    Scene scene = (Scene) o;
                    scene.setTitle(s.title());
                    scene.setTileSize(new ViewDimension(
                            s.tileWidth(),
                            s.tileHeight(),
                            s.tileAltitude()
                    ));
                    scene.setIsometric(s.isometric());
                    scene.setCameraSize(
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
                Scene scene = (Scene) atomAnnotationsProcessor.container.create(clazz, prep, injects);
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
                prep.prepare(scene);
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
}
