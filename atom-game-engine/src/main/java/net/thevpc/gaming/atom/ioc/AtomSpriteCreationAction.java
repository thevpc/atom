package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSprite;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteMainTask;
import net.thevpc.gaming.atom.engine.collisiontasks.SpriteCollisionTask;
import net.thevpc.gaming.atom.model.DefaultSprite;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSpriteCreationAction implements PostponedAction {
    private final IoCContext context;
    private final AtomAnnotationsProcessor atomAnnotationsProcessor;

    public AtomSpriteCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, IoCContext context) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.context = context;
    }

    @Override
    public String toString() {
        return "@AtomSprite{" +
                "" + context.findCurrent().getClass().getSimpleName() +
                ", sceneEngineId=" + context.getSceneEngineId() +
                ", sceneEngine=" + context.getSceneId() +
                '}';
    }

    @Override
    public boolean isRunnable() {
        Class<?> clazz = context.findCurrent().getClass();
        Game game = context.get(Game.class, null);
        AtomSprite s = clazz.getAnnotation(AtomSprite.class);
        for (String sceneEngineId : AtomAnnotationsProcessor.split(s.sceneEngine())) {
            if (atomAnnotationsProcessor.container.isInjectedType(clazz, Scene.class)) {
                if (!(atomAnnotationsProcessor.container.contains(sceneEngineId, "SceneEngine", sceneEngineId)
                        &&
                        game.findScenesBySceneEngineId(sceneEngineId).size() > 0)
                ) {
                    return false;
                }
            } else {
                if (!atomAnnotationsProcessor.container.contains(sceneEngineId, "SceneEngine", sceneEngineId)) {
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
        Game game = context.get(Game.class, null);
        SceneEngine[] scenes = context.getSceneEngineId().equals("*") ? game.getGameEngine().getSceneEngines().toArray(new SceneEngine[0])
                : new SceneEngine[]{game.getGameEngine().getSceneEngine(context.getSceneEngineId())};
        for (SceneEngine sceneEngine : scenes) {

            Sprite sprite = context.findCurrentAs(Sprite.class);
            if (sprite == null) {
                sprite = new DefaultSprite();
            }
//            sprite.setCompanionObject(context.findCurrent());
            AtomSprite s = context.get(AtomSprite.class, null);
            sprite.setName(s.name());
            sprite.setKind(s.kind());
            sprite.setSpeed(s.speed());
            sprite.setLocation(s.x(), s.y());
            sprite.setDirection(s.direction());
            sprite.setSize(s.width(), s.height());
            sprite.setLife(s.life());
            sprite.setMaxLife(s.maxLife());
            sprite.setSight(s.sight());

            sceneEngine.addSprite(sprite);
            context.register(Sprite.class, sprite, sprite.getName());
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
                    sceneEngine.getId() + "/" + s.name(), "Sprite", sprite.getName(), sprite);
        }
    }
}
