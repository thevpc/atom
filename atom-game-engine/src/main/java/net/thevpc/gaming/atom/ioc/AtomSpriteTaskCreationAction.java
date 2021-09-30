package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSpriteMainTask;
import net.thevpc.gaming.atom.annotations.Scope;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.HoldPositionSpriteMainTask;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.util.AtomUtils;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteTaskCreationAction implements PostponedAction {
    private final IoCContext context;
    private final AtomAnnotationsProcessor atomAnnotationsProcessor;

    public AtomSpriteTaskCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, IoCContext context) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.context = context;
    }
    @Override
    public String toString() {
        return "@AtomSpriteMainTask{" +
                "" + context.findCurrent().getClass().getSimpleName() +
                ", sceneEngineId=" + context.getSceneEngineId() +
                ", sceneEngine=" + context.getSceneId() +
                '}';
    }
    @Override
    public boolean isRunnable() {
        AtomSpriteMainTask s = context.get(AtomSpriteMainTask.class,null);
        if (s.sceneEngine().isEmpty()) {
            return true;
        }
        for (String n : AtomAnnotationsProcessor.split(s.sceneEngine())) {
            if (!atomAnnotationsProcessor.container.contains(n, "SceneEngine", null)) {
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
        Game game = context.get(Game.class,null);
        AtomSpriteMainTask s = context.get(AtomSpriteMainTask.class,null);
        SceneEngine[] sceneEngines = context.getSceneEngineId().equals("*") ? game.getGameEngine().getSceneEngines().toArray(new SceneEngine[0])
                : new SceneEngine[]{game.getGameEngine().getSceneEngine(context.getSceneEngineId())};
        for (SceneEngine sceneEngine : sceneEngines) {
            if (s.scope() == Scope.PROTOTYPE) {
                if (!SpriteMainTask.class.isAssignableFrom(context.findCurrent().getClass())) {
                    throw new IllegalArgumentException("context.findCurrent().getClass() must implement SpriteMainTask when scope is prototype");
                }
                sceneEngine.setSpriteMainTask(
                        s.kind(), (Class<SpriteMainTask>) context.findCurrent().getClass()
                );
            } else {
                SpriteMainTask spriteMainTask = context.findCurrentAs(SpriteMainTask.class);
                if (spriteMainTask == null) {
                    spriteMainTask = new HoldPositionSpriteMainTask();
                }
                context.register(SpriteMainTask.class,spriteMainTask, AtomUtils.trimToNull(s.name()));
                sceneEngine.setSpriteMainTask(s.kind(), spriteMainTask);
                atomAnnotationsProcessor.container.register(
                        sceneEngine.getId() + "/" + context.findCurrent().getClass().getName()
                        , "SpriteMainTask", AtomUtils.trimToNull(s.name()), spriteMainTask);
            }
       }
    }
}
