package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SceneEngineStateAdapter;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
class SceneEngineActivatingMethodAction extends AbstractSceneEngineMethodAction {
    public SceneEngineActivatingMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor,Method method,Object instance) {
        super(atomAnnotationsProcessor,method,instance);
    }

    public void run(SceneEngine sceneEngine) {
        sceneEngine.addStateListener(new SceneEngineStateAdapter() {
            @Override
            public void sceneActivating(SceneEngine sceneEngine) {
                invokeDefault();
            }
        });
    }
}
