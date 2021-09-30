package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.annotations.AtomSceneEngine;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.util.AtomUtils;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
abstract class AbstractSceneEngineMethodAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Method method;
    private final Object instance;

    public AbstractSceneEngineMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Method method,Object instance) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.method = method;
        this.instance = instance;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_METHOD;
    }

    @Override
    public boolean isRunnable() {
        final AtomSceneEngine sceneEngineAnn = method.getDeclaringClass().getAnnotation(AtomSceneEngine.class);
        if (sceneEngineAnn != null) {
            return atomAnnotationsProcessor.container.contains(sceneEngineAnn.id(), "SceneEngine", AtomUtils.trimToNull(sceneEngineAnn.id()));
        }
        final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
        if (sceneAnn != null) {
            if (atomAnnotationsProcessor.container.contains(sceneAnn.id(), "Scene", AtomUtils.trimToNull(sceneAnn.id()))) {
                SceneEngine ee = atomAnnotationsProcessor.getGame().getScene(sceneAnn.id()).getSceneEngine();
                return atomAnnotationsProcessor.container.contains(ee.getId(), "SceneEngine", AtomUtils.trimToNull(sceneAnn.id()));
            }
            return false;
        }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public void run() {
        final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
        if (sceneAnn != null) {
            run(atomAnnotationsProcessor.getGame().getScene(sceneAnn.id()).getSceneEngine());
        }
        final AtomSceneEngine sceneEngineAnn = method.getDeclaringClass().getAnnotation(AtomSceneEngine.class);
        if (sceneEngineAnn != null) {
            run(atomAnnotationsProcessor.getGame().getGameEngine().getSceneEngine(sceneEngineAnn.id()));
        }
    }

    public abstract void run(SceneEngine scene);

    public void invokeDefault() {
        try {

//            final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
//            if (sceneAnn != null) {
//                method.invoke(atomAnnotationsProcessor.container.getBean(sceneAnn.id(), "Scene"));
//                run(atomAnnotationsProcessor.getGame().getScene(sceneAnn.id()).getSceneEngine());
//            }
//            final AtomSceneEngine sceneEngineAnn = method.getDeclaringClass().getAnnotation(AtomSceneEngine.class);
//            if (sceneEngineAnn != null) {
//                method.invoke(atomAnnotationsProcessor.container.getBean(sceneEngineAnn.id(), "SceneEngine"));
//            }
            method.invoke(instance);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
