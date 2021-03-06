package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomSprite;
import net.vpc.gaming.atom.annotations.OnInstall;
import net.vpc.gaming.atom.engine.GameEngine;
import net.vpc.gaming.atom.engine.SceneEngine;
import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.engine.collision.SpriteCollisionManager;
import net.vpc.gaming.atom.model.Sprite;
import net.vpc.gaming.atom.presentation.Game;
import net.vpc.gaming.atom.presentation.Scene;

import java.util.HashMap;
import java.util.List;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteCreationAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Class clazz;
    private final AtomSprite s;
    private final Game game;

    public AtomSpriteCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Class clazz, AtomSprite s, Game game) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.clazz = clazz;
        this.s = s;
        this.game = game;
    }

    @Override
    public boolean isRunnable() {
        for (String sceneEnginId : AtomAnnotationsProcessor.split(s.engine())) {
            if (atomAnnotationsProcessor.container.isInjectedType(clazz, Scene.class)) {
                if (!(atomAnnotationsProcessor.container.contains(sceneEnginId, "SceneEngine")
                        &&
                        game.findScenesBySceneEnginId(sceneEnginId).size() > 0)
                        ) {
                    return false;
                }
            } else {
                if (!atomAnnotationsProcessor.container.contains(sceneEnginId, "SceneEngine")) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SPRITE;
    }

    @Override
    public void run() {
        for (String sceneEngineId : AtomAnnotationsProcessor.split(s.engine())) {
            final SceneEngine sceneEngine = game.getGameEngine().getSceneEngine(sceneEngineId);
            InstancePreparator prep = new InstancePreparator() {
                @Override
                public void prepare(Object o) {
                    Sprite sprite = (Sprite) o;
                    sprite.setName(s.name());
                    sprite.setSpeed(s.speed());
                    sprite.setLocation(s.x(), s.y());
                    sprite.setDirection(s.direction());
                    sprite.setSize(s.width(), s.height());
                    sprite.setLife(s.life());
                    sprite.setMaxLife(s.maxLife());
                    sprite.setSight(s.sight());
                }
            };
            Sprite sprite = null;
            Object instance = null;
            if (Sprite.class.isAssignableFrom(clazz)) {
                HashMap<Class, Object> injects = new HashMap<>();
                injects.put(Game.class, game);
                injects.put(GameEngine.class, game.getGameEngine());
                injects.put(SceneEngine.class, sceneEngine);
                List<Scene> scenesBySceneEnginId = game.findScenesBySceneEnginId(sceneEngineId);
                if (scenesBySceneEnginId.size() == 1) {
                    injects.put(Scene.class, scenesBySceneEnginId.get(0));
                } else {
                    injects.put(Scene[].class, scenesBySceneEnginId.toArray(new Scene[scenesBySceneEnginId.size()]));
                }
                sprite = (Sprite) atomAnnotationsProcessor.container.create(clazz, prep, injects);
                sceneEngine.addSprite(sprite);
                instance = sprite;
            } else {
                sprite = sceneEngine.createSprite(s.kind());
                sprite.setName(s.name());
                HashMap<Class, Object> injects = new HashMap<>();
                injects.put(Game.class, game);
                injects.put(GameEngine.class, game.getGameEngine());
                injects.put(SceneEngine.class, sceneEngine);
                injects.put(Sprite.class, sprite);
                List<Scene> scenesBySceneEnginId = game.findScenesBySceneEnginId(sceneEngineId);
                if (scenesBySceneEnginId.size() == 1) {
                    injects.put(Scene.class, scenesBySceneEnginId.get(0));
                } else {
                    injects.put(Scene[].class, scenesBySceneEnginId.toArray(new Scene[scenesBySceneEnginId.size()]));
                }

                instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
                prep.prepare(sprite);
                sceneEngine.addSprite(sprite);
            }
            if (!s.task().equals(Void.class)) {
                sceneEngine.setSpriteTask(sprite.getId(), (SpriteTask) atomAnnotationsProcessor.container.create(
                        s.task(), null, null
                ));
            }
            if (!s.collision().equals(Void.class)) {
                sceneEngine.setSpriteCollisionManager(sprite.getId(), (SpriteCollisionManager) atomAnnotationsProcessor.container.create(
                        s.collision(), null, null
                ));
            }
            atomAnnotationsProcessor.container.register(
                    sceneEngineId+"/"+s.name(), "Sprite", instance);
            atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
        }

    }
}
