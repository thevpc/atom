package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.presentation.DefaultSceneController;
import net.vpc.gaming.atom.presentation.Scene;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
class SceneInitializedMethodAction extends AbstractSceneMethodAction {

    public SceneInitializedMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor,Method method) {
        super(atomAnnotationsProcessor,method);
    }

    public void run(Scene scene) {
        scene.addSceneController(new DefaultSceneController() {
            @Override
            public void sceneInitialized(Scene scene) {
                invokeDefault();
            }
        });
    }
}
