package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.ModelDimension;
import net.thevpc.gaming.atom.presentation.Game;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSceneEngineCreationAction implements PostponedAction {
    private final IoCContext context;
    private final AtomAnnotationsProcessor atomAnnotationsProcessor;

    public AtomSceneEngineCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, IoCContext context) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.context = context;
    }
    @Override
    public String toString() {
        return "@AtomSceneEngine{" +
                "" + context.findCurrent().getClass().getSimpleName() +
                ", sceneEngineId=" + context.getSceneEngineId() +
                ", sceneEngine=" + context.getSceneId() +
                '}';
    }
    public static String resolveSceneEngineId(IoCContext context) {
        AtomSceneEngine annSceneEngine = context.find(AtomSceneEngine.class,null);
        if (annSceneEngine != null) {
            String id = annSceneEngine.id();
            if (!id.isEmpty()) {
                return id;
            }
        }
        AtomScene annScene = context.find(AtomScene.class,null);
        if (annScene != null) {
            String id = annScene.id();
            if (id.isEmpty()) {
                return id;
            }
        }
        Object o = context.findCurrent();
        return o.getClass().getSimpleName();
    }

    @Override
    public boolean isRunnable() {
        return true;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SCENE_ENGINE;
    }

    @Override
    public void run() {
        Game game= context.get(Game.class,null);
        AtomSceneEngine annSceneEngine = context.find(AtomSceneEngine.class,null);
        if (annSceneEngine != null) {
            String sceneEngineId = resolveSceneEngineId(context);
            SceneEngine sceneEngine = context.findCurrentAs(SceneEngine.class);
            if (sceneEngine == null) {
                sceneEngine = game.getGameEngine().createSceneEngine(sceneEngineId);
            } else {
                sceneEngine.setId(sceneEngineId);
                sceneEngine.setCompanionObject(sceneEngine);
            }
            context.register(SceneEngine.class, sceneEngine,sceneEngine.getId());
            game.getGameEngine().addSceneEngine(sceneEngine);
            if (annSceneEngine.welcome()) {
                game.getGameEngine().setDefaultSceneEngineId(sceneEngineId);
            }
            atomAnnotationsProcessor.container.register(sceneEngineId, "SceneEngine", sceneEngine.getId(), sceneEngine);
            if (annSceneEngine.columns() > 0) {
                sceneEngine.getModel()
                        .setSize(new ModelDimension(annSceneEngine.columns(),
                                annSceneEngine.rows() <= 0 ? annSceneEngine.columns() : annSceneEngine.rows(),
                                annSceneEngine.stacks() <= 0 ? 1 : annSceneEngine.stacks()));
            }
            if (annSceneEngine.fps() > 0) {
                sceneEngine.setFps(annSceneEngine.fps());
            }
        }
    }

}
