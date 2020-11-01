package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomSpriteTask;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.annotations.Scope;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.engine.tasks.HoldPositionSpriteTask;
import net.vpc.gaming.atom.presentation.Game;
import net.vpc.gaming.atom.presentation.SpriteView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteTaskCreationAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Class clazz;
    private final AtomSpriteTask s;
    private final Game game;

    public AtomSpriteTaskCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Class clazz, AtomSpriteTask s, Game game) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.clazz = clazz;
        this.s = s;
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
        return AtomAnnotationsProcessor.ORDER_SPRITE_COLLISION_MANAGER;
    }

    @Override
    public void run() {
        for (String sceneId : AtomAnnotationsProcessor.splitOrAddEmpty(s.engine())) {
            final List<SceneEngine> scenes = sceneId.isEmpty() ? game.getGameEngine().getScenes() : Arrays.asList(game.getGameEngine().getScene(sceneId));
            for (SceneEngine sceneEngine : scenes) {
                if (s.scope() == Scope.PROTOTYPE) {
                    sceneEngine.setSpriteTask(
                            s.kind(), clazz
                    );
                } else {
                    InstancePreparator prep = new InstancePreparator() {
                        @Override
                        public void postInject(Object o) {

                        }
                    };
                    SpriteTask spriteTask = null;
                    Object instance = null;
                    HashMap<Class, Object> injects = new HashMap<>();
                    injects.put(Game.class, game);
                    injects.put(GameEngine.class, game.getGameEngine());
                    injects.put(SceneEngine.class, sceneEngine);

                    if (SpriteTask.class.isAssignableFrom(clazz)) {
                        spriteTask = (SpriteTask) atomAnnotationsProcessor.container.create(clazz, new InstancePreparator[]{
                                new InstancePreparator() {
                                    @Override
                                    public void preInject(Object o, Map<Class, Object> injects) {
                                        injects.put(SpriteTask.class, o);
                                    }
                                },
                                prep
                        }, injects);
                        instance = spriteTask;
                    } else {
                        spriteTask = new HoldPositionSpriteTask();
                        injects.put(SpriteTask.class, spriteTask);
                        instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
                        prep.postInject(spriteTask);
                    }
                    sceneEngine.setSpriteTask(s.kind(), spriteTask);
                    atomAnnotationsProcessor.container.register(
                            sceneEngine.getId() + "/" + instance.getClass().getName()
                            , "SpriteTask", instance);
                    atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
                }
            }
        }

    }
}
