package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSprite;
import net.thevpc.gaming.atom.annotations.OnInstall;
import net.thevpc.gaming.atom.engine.GameEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        for (String sceneEnginId : AtomAnnotationsProcessor.split(s.scene())) {
            if (atomAnnotationsProcessor.container.isInjectedType(clazz, Scene.class)) {
                if (!(atomAnnotationsProcessor.container.contains(sceneEnginId, "SceneEngine")
                        &&
                        game.findScenesBySceneEngineId(sceneEnginId).size() > 0)
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
        for (String sceneId : AtomAnnotationsProcessor.splitOrAddEmpty(s.scene())) {
            final List<SceneEngine> scenes = sceneId.isEmpty() ? game.getGameEngine().getScenes() : Arrays.asList(game.getGameEngine().getScene(sceneId));
            for (SceneEngine sceneEngine : scenes) {
                InstancePreparator prep = new InstancePreparator() {
                    @Override
                    public void postInject(Object o) {
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
                    List<Scene> scenesBySceneEngineId = game.findScenesBySceneEngineId(sceneEngine.getId());
                    if (scenesBySceneEngineId.size() == 1) {
                        injects.put(Scene.class, scenesBySceneEngineId.get(0));
                    } else {
                        injects.put(Scene[].class, scenesBySceneEngineId.toArray(new Scene[scenesBySceneEngineId.size()]));
                    }
                    sprite = (Sprite) atomAnnotationsProcessor.container.create(clazz, new InstancePreparator[]{
                            new InstancePreparator() {
                                @Override
                                public void preInject(Object o, Map<Class, Object> injects) {
                                    injects.put(Sprite.class, o);
                                }
                            },
                            prep
                    }, injects);
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
                    List<Scene> scenesBySceneEnginId = game.findScenesBySceneEngineId(sceneEngine.getId());
                    if (scenesBySceneEnginId.size() == 1) {
                        injects.put(Scene.class, scenesBySceneEnginId.get(0));
                    } else {
                        injects.put(Scene[].class, scenesBySceneEnginId.toArray(new Scene[scenesBySceneEnginId.size()]));
                    }

                    instance = atomAnnotationsProcessor.container.create(clazz, null, injects);
                    prep.postInject(sprite);
                    sceneEngine.addSprite(sprite);
                }
                if (!s.mainTask().equals(Void.class)) {
                    sceneEngine.setSpriteMainTask(sprite.getId(), (SpriteMainTask) atomAnnotationsProcessor.container.create(
                            s.mainTask(), null, null
                    ));
                }
                if (!s.collisionTask().equals(Void.class)) {
                    sceneEngine.setSpriteCollisionTask(sprite.getId(), (SpriteCollisionTask) atomAnnotationsProcessor.container.create(
                            s.collisionTask(), null, null
                    ));
                }
                atomAnnotationsProcessor.container.register(
                        sceneEngine.getId() + "/" + s.name(), "Sprite", instance);
                atomAnnotationsProcessor.container.invokeMethodsByAnnotation(instance, OnInstall.class, new Object[0]);
            }
        }

    }
}
