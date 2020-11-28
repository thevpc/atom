package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSpriteTask;
import net.thevpc.gaming.atom.annotations.OnInstall;
import net.thevpc.gaming.atom.annotations.Scope;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.HoldPositionSpriteMainTask;
import net.thevpc.gaming.atom.presentation.Game;

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
                    sceneEngine.setSpriteMainTask(
                            s.kind(), clazz
                    );
                } else {
                    InstancePreparator prep = new InstancePreparator() {
                        @Override
                        public void postInject(Object o) {

                        }
                    };
                    SpriteMainTask spriteMainTask = null;
                    Object instance = null;
                    HashMap<Class, Object> injects = new HashMap<>();
                    injects.put(Game.class, game);
                    injects.put(GameEngine.class, game.getGameEngine());
                    injects.put(SceneEngine.class, sceneEngine);

                    if (SpriteMainTask.class.isAssignableFrom(clazz)) {
                        spriteMainTask = (SpriteMainTask) atomAnnotationsProcessor.container.create(clazz, new InstancePreparator[]{
                                new InstancePreparator() {
                                    @Override
                                    public void preInject(Object o, Map<Class, Object> injects) {
                                        injects.put(SpriteMainTask.class, o);
                                    }
                                },
                                prep
                        }, injects);
                        instance = spriteMainTask;
                    } else {
                        spriteMainTask = new HoldPositionSpriteMainTask();
                        injects.put(SpriteMainTask.class, spriteMainTask);
                        instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
                        prep.postInject(spriteMainTask);
                    }
                    sceneEngine.setSpriteMainTask(s.kind(), spriteMainTask);
                    atomAnnotationsProcessor.container.register(
                            sceneEngine.getId() + "/" + instance.getClass().getName()
                            , "SpriteMainTask", instance);
                    atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
                }
            }
        }

    }
}
