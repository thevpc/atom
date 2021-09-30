package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.model.RatioDimension;
import net.thevpc.gaming.atom.model.ViewDimension;
import net.thevpc.gaming.atom.presentation.DefaultScene;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSceneCreationAction implements PostponedAction {

    private final IoCContext context;
    private final AtomAnnotationsProcessor atomAnnotationsProcessor;

    public AtomSceneCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, IoCContext context) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.context = context;
    }

    @Override
    public String toString() {
        return "@AtomScene{" +
                "" + context.findCurrent().getClass().getSimpleName() +
                ", sceneEngineId=" + context.getSceneEngineId() +
                ", sceneEngine=" + context.getSceneId() +
                '}';
    }

    public static String resolveSceneId(IoCContext context) {
        AtomScene annScene = context.find(AtomScene.class,null);
        if (annScene != null) {
            String id = annScene.id();
            if (!id.isEmpty()) {
                return id;
            }
        }
        AtomSceneEngine annSceneEngine = context.find(AtomSceneEngine.class,null);
        if (annSceneEngine != null) {
            String id = annSceneEngine.id();
            if (!id.isEmpty()) {
                return id;
            }
        }
        Object o = context.findCurrent();
        return o.getClass().getSimpleName();
    }

    @Override
    public boolean isRunnable() {
        if (!atomAnnotationsProcessor.container.contains(context.getSceneEngineId(), "SceneEngine", context.getSceneEngineId())) {
            return false;
        }
        return true;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SCENE;
    }


    @Override
    public void run() {
        Game game = context.get(Game.class,null);
        AtomScene annAtomScene = context.find(AtomScene.class,null);
        String sceneId = resolveSceneId(context);
        String sceneEngineId = context.getSceneEngineId();
        SceneEngine sceneEngine = game.getGameEngine().containsSceneEngine(sceneId)
                ? game.getGameEngine().getSceneEngine(sceneEngineId)
                : game.getGameEngine().createSceneEngine(sceneId);
        Scene scene = context.findCurrentAs(Scene.class);
        if(scene==null){
            scene = new DefaultScene(
                    new ViewDimension(
                            annAtomScene.tileWidth(),
                            annAtomScene.tileHeight(),
                            annAtomScene.tileAltitude()
                    )
            );
        }
        scene.setCompanionObject(context.findCurrent());
        scene.setTitle(annAtomScene.title());
        scene.setTileSize(new ViewDimension(
                annAtomScene.tileWidth(),
                annAtomScene.tileHeight(),
                annAtomScene.tileAltitude()
        ));
        scene.setIsometric(annAtomScene.isometric());
        scene.getCamera().setDimension(
                new RatioDimension(
                        annAtomScene.cameraWidth(),
                        annAtomScene.cameraHeight()
                )
        );
        game.addScene(scene, sceneEngine.getId());
        context.register(Scene.class,scene,scene.getId());
        atomAnnotationsProcessor.container.register(sceneId, "Scene", scene.getId(), scene);
    }

}
