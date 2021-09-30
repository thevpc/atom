package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.presentation.Scene;
import net.thevpc.gaming.atom.presentation.SceneLifeCycleListener;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
class NextFrameMethodAction extends AbstractSceneMethodAction {
    public NextFrameMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor,Method method,Object instance) {
        super(atomAnnotationsProcessor,method,instance);
    }

    public void run(Scene scene) {
        scene.addLifeCycleListener(new SceneLifeCycleListener(){
            @Override
            public void nexFrame(Scene scene) {
                invokeDefault();
            }
        });
    }

}
