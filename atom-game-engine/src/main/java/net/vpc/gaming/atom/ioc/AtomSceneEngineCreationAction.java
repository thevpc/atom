package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.model.ModelDimension;
import net.vpc.gaming.atom.presentation.Game;

import java.util.HashMap;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSceneEngineCreationAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final AtomSceneEngine s;
    private final Class clazz;
    private final Game game;

    public AtomSceneEngineCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, AtomSceneEngine s, Class clazz, Game game) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.s = s;
        this.clazz = clazz;
        this.game = game;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SCENE_ENGINE;
    }

    @Override
    public boolean isRunnable() {
        return true;
    }

    @Override
    public void run() {
        String sceneEngineId = s.id();
        if (sceneEngineId.isEmpty()) {
            sceneEngineId = clazz.getSimpleName();
        }

        Object instance = null;
        final String finalSceneEngineId = sceneEngineId;
        InstancePreparator prep = new InstancePreparator() {
            @Override
            public void prepare(Object o) {
                SceneEngine sceneEngine = (SceneEngine) o;
                sceneEngine.setId(finalSceneEngineId);
                sceneEngine.getModel().setSize(new ModelDimension(
                        s.width(),
                        s.height(),
                        s.altitude()
                ));
                sceneEngine.setFps(s.fps());
            }
        };
        if (SceneEngine.class.isAssignableFrom(clazz)) {
            HashMap<Class, Object> injects = new HashMap<>();
            injects.put(Game.class, game);
            injects.put(GameEngine.class, game.getGameEngine());
            SceneEngine sceneEngine = (SceneEngine) atomAnnotationsProcessor.container.create(clazz, prep, injects);
            game.addSceneEngine(sceneEngine);
            instance = sceneEngine;
        } else {
            SceneEngine sceneEngine = game.getGameEngine().createSceneEngine(sceneEngineId);
            prep.prepare(sceneEngine);

            game.addSceneEngine(sceneEngine);

            HashMap<Class, Object> injects = new HashMap<>();
            injects.put(Game.class, game);
            injects.put(GameEngine.class, game.getGameEngine());
            injects.put(SceneEngine.class, sceneEngine);
            instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
            sceneEngine.setCompanionObject(instance);
        }
        atomAnnotationsProcessor.container.register(sceneEngineId, "SceneEngine", instance);
        atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
        if(s.welcome()){
            game.getGameEngine().setDefaultActiveSceneEngineId(sceneEngineId);
        }
    }
}
