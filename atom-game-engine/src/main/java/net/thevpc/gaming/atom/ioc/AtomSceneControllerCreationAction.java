package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomSceneController;
import net.thevpc.gaming.atom.presentation.Game;
import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneController;
import net.thevpc.gaming.atom.util.AtomUtils;

/**
 * Created by vpc on 10/7/16.
 */
class AtomSceneControllerCreationAction implements PostponedAction {
    private final AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final IoCContext context;

    public AtomSceneControllerCreationAction(AtomAnnotationsProcessor atomAnnotationsProcessor, IoCContext context) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.context = context;
    }
    @Override
    public String toString() {
        return "@AtomSceneController{" +
                "" + context.findCurrent().getClass().getSimpleName() +
                ", sceneEngineId=" + context.getSceneEngineId() +
                ", sceneEngine=" + context.getSceneId() +
                '}';
    }
    @Override
    public boolean isRunnable() {
        AtomSceneController s = context.get(AtomSceneController.class, null);
        if (s.scene().isEmpty()) {
            return true;
        }
        for (String n : AtomAnnotationsProcessor.splitOrAddEmpty(s.scene())) {
            if (n.isEmpty()) {
                return true;
            }
            if (!atomAnnotationsProcessor.container.contains(n, "Scene", n)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_SCENE_CONTROLLER;
    }

    @Override
    public void run() {
        Game game = context.get(Game.class, null);
        AtomSceneController s=context.get(AtomSceneController.class, null);
        Scene scene =game.getScene(context.getSceneId());

        InstancePreparator prep = new InstancePreparator() {
            @Override
            public void postInject(Object o) {

            }
        };
        SceneController spriteController = context.findCurrentAs(SceneController.class);
        if (spriteController == null) {
            throw new IllegalArgumentException("Controller must implement SceneController interface");
        }
        String name = AtomUtils.trimToNull(s.name());
        context.register(SceneController.class, spriteController, name);
        atomAnnotationsProcessor.container.register(
                scene.getId() + "/" + spriteController.getClass().getName()
                , "SceneController", name, spriteController);
        scene.addController(spriteController);

    }
}

