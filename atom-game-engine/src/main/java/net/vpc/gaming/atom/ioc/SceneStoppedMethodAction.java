package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.presentation.Scene;
import net.vpc.gaming.atom.presentation.SceneLifeCycleListener;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
class SceneStoppedMethodAction extends AbstractSceneMethodAction {
    public SceneStoppedMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor,Method method) {
        super(atomAnnotationsProcessor,method);
    }

    public void run(Scene scene) {
        scene.addLifeCycleListener(new SceneLifeCycleListener() {
            @Override
            public void sceneStopped(Scene scene) {
                invokeDefault();
            }
        });
    }
}
