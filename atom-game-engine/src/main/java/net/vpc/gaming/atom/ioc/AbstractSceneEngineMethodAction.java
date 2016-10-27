package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.annotations.AtomSceneEngine;
import net.vpc.gaming.atom.engine.SceneEngine;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
abstract class AbstractSceneEngineMethodAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Method method;

    public AbstractSceneEngineMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Method method) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.method = method;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_METHOD;
    }

    @Override
    public boolean isRunnable() {
        final AtomSceneEngine sceneAnn = method.getDeclaringClass().getAnnotation(AtomSceneEngine.class);
        if (sceneAnn != null) {
            return atomAnnotationsProcessor.container.contains(sceneAnn.id(), "SceneEngine");
        }
        final AtomScene sceneAnnScene = method.getDeclaringClass().getAnnotation(AtomScene.class);
        if (sceneAnnScene != null) {
            if (atomAnnotationsProcessor.container.contains(sceneAnnScene.id(), "Scene")) {
                SceneEngine ee = atomAnnotationsProcessor.getGame().getScene(sceneAnnScene.id()).getSceneEngine();
                return atomAnnotationsProcessor.container.contains(ee.getId(), "SceneEngine");
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
            final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
            if (sceneAnn != null) {
                method.invoke(atomAnnotationsProcessor.container.getBean(sceneAnn.id(), "Scene"));
                run(atomAnnotationsProcessor.getGame().getScene(sceneAnn.id()).getSceneEngine());
            }
            final AtomSceneEngine sceneEngineAnn = method.getDeclaringClass().getAnnotation(AtomSceneEngine.class);
            if (sceneEngineAnn != null) {
                method.invoke(atomAnnotationsProcessor.container.getBean(sceneEngineAnn.id(), "SceneEngine"));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
