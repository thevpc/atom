package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomSpriteCollisionManager;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.annotations.Scope;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.collision.DefaultSpriteCollisionManager;
import net.vpc.gaming.atom.engine.collision.SpriteCollisionManager;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.Game;
import net.vpc.gaming.atom.presentation.Scene;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteCollisionManagerCreationAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Class clazz;
    private final AtomSpriteCollisionManager s;
    private final Game game;

    public AtomSpriteCollisionManagerCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Class clazz, AtomSpriteCollisionManager s, Game game) {
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
        for (String sceneId : AtomAnnotationsProcessor.splitOrAddEmpty(s.engine())) {
            final List<SceneEngine> scenes = sceneId.isEmpty() ? game.getGameEngine().getScenes() : Arrays.asList(game.getGameEngine().getScene(sceneId));
            for (SceneEngine scene : scenes) {
                if (!atomAnnotationsProcessor.container.contains(scene.getId(), "SceneEngine")) {
                    return false;
                }
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
                    sceneEngine.setSpriteCollisionManager(
                            s.kind(), clazz
                    );
                } else {

                    InstancePreparator prep = new InstancePreparator() {
                        @Override
                        public void postInject(Object o) {

                        }
                    };
                    SpriteCollisionManager spriteCollisionManager = null;
                    Object instance = null;
                    HashMap<Class, Object> injects = new HashMap<>();
                    injects.put(Game.class, game);
                    injects.put(GameEngine.class, game.getGameEngine());
                    injects.put(SceneEngine.class, sceneEngine);

                    if (SpriteCollisionManager.class.isAssignableFrom(clazz)) {
                        spriteCollisionManager = (SpriteCollisionManager) atomAnnotationsProcessor.container.create(clazz, new InstancePreparator[]{
                                new InstancePreparator() {
                                    @Override
                                    public void preInject(Object o, Map<Class, Object> injects) {
                                        injects.put(SpriteCollisionManager.class, o);
                                    }
                                },
                                prep
                        }, injects);
                        instance = spriteCollisionManager;
                    } else {
                        spriteCollisionManager = new DefaultSpriteCollisionManager();
                        injects.put(SpriteCollisionManager.class, spriteCollisionManager);
                        instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
                        prep.postInject(spriteCollisionManager);
                    }
                    sceneEngine.setSpriteCollisionManager(s.kind(), spriteCollisionManager);
                    atomAnnotationsProcessor.container.register(
                            sceneEngine.getId() + "/" + instance.getClass().getName(), "SpriteCollisionManager", instance);
                    atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
                }
            }
        }

    }
}
