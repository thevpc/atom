package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneLifeCycleListener;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
class SceneInitializedMethodAction extends AbstractSceneMethodAction {

    public SceneInitializedMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor,Method method) {
        super(atomAnnotationsProcessor,method);
    }

    public void run(Scene scene) {
        scene.addLifeCycleListener(new SceneLifeCycleListener() {
            @Override
            public void sceneInitialized(Scene scene) {
                invokeDefault();
            }
        });
    }
}
