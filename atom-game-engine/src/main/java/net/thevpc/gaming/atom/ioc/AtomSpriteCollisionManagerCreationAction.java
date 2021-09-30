package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSpriteCollisionTask;
import net.thevpc.gaming.atom.annotations.Scope;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteCollisionManagerCreationAction implements PostponedAction {
    private final IoCContext context;
    private final AtomAnnotationsProcessor atomAnnotationsProcessor;

    public AtomSpriteCollisionManagerCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, IoCContext context) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.context = context;
    }
    @Override
    public String toString() {
        return "@AtomSpriteCollisionTask{" +
                "" + context.findCurrent().getClass().getSimpleName() +
                ", sceneEngineId=" + context.getSceneEngineId() +
                ", sceneEngine=" + context.getSceneId() +
                '}';
    }
    @Override
    public boolean isRunnable() {
        AtomSpriteCollisionTask s = context.get(AtomSpriteCollisionTask.class, null);
        Game game = context.get(Game.class, null);
        if (s.sceneEngine().isEmpty()) {
            return true;
        }
        for (String sceneId : AtomAnnotationsProcessor.splitOrAddEmpty(s.sceneEngine())) {
            final List<SceneEngine> scenes = sceneId.isEmpty() ? game.getGameEngine().getSceneEngines() : Arrays.asList(game.getGameEngine().getSceneEngine(sceneId));
            for (SceneEngine scene : scenes) {
                if (!atomAnnotationsProcessor.container.contains(scene.getId(), "SceneEngine", scene.getId())) {
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
        Game game = context.get(Game.class, null);
        SceneEngine[] sceneEngines = context.getSceneEngineId().equals("*") ? game.getGameEngine().getSceneEngines().toArray(new SceneEngine[0])
                : new SceneEngine[]{game.getGameEngine().getSceneEngine(context.getSceneEngineId())};
        for (SceneEngine sceneEngine : sceneEngines) {
            AtomSpriteCollisionTask s = context.get(AtomSpriteCollisionTask.class, null);
            if (s.scope() == Scope.PROTOTYPE) {
                if (!SpriteCollisionTask.class.isAssignableFrom(context.findCurrent().getClass())) {
                    throw new IllegalArgumentException("class " + context.findCurrent().getClass().getName() + " must implement SpriteCollisionTask if scope is prototype");
                }
                sceneEngine.setSpriteCollisionTask(
                        s.kind(), (Class<? extends SpriteCollisionTask>) context.findCurrent().getClass()
                );
            } else {

                SpriteCollisionTask spriteCollisionTask = context.findCurrentAs(SpriteCollisionTask.class);
                if (spriteCollisionTask == null) {
                    throw new IllegalArgumentException("class " + context.findCurrent().getClass().getName() + " must implement SpriteCollisionTask if scope is prototype");
                }
                sceneEngine.setSpriteCollisionTask(s.kind(), spriteCollisionTask);
                String name = AtomUtils.trimToNull(s.name());
                context.register(SpriteCollisionTask.class, spriteCollisionTask, name);
                atomAnnotationsProcessor.container.register(
                        sceneEngine.getId() + "/" + context.findCurrent().getClass().getName(), "SpriteCollisionTask", name, spriteCollisionTask);
            }
        }
    }
}
