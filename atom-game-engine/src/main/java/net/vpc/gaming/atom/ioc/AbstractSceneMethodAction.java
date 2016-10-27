package net.vpc.gaming.atom.ioc;

import net.vpc.gaming.atom.annotations.AtomScene;
import net.vpc.gaming.atom.presentation.Scene;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
abstract class AbstractSceneMethodAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Method method;

    public AbstractSceneMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Method method) {
        this.atomAnnotationsProcessor = atomAnnotationsProcessor;
        this.method = method;
    }

    @Override
    public int getOrder() {
        return AtomAnnotationsProcessor.ORDER_METHOD;
    }

    @Override
    public boolean isRunnable() {
        final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
        if (sceneAnn != null) {
            return atomAnnotationsProcessor.container.contains(sceneAnn.id(), "Scene");
        }
//                    final AtomSceneEngine sceneAnnSceneEngine = method.getDeclaringClass().getAnnotation(AtomSceneEngine.class);
//                    if (sceneAnnSceneEngine != null) {
//                        return engines.containsKey(sceneAnnSceneEngine.id());
//                    }
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public void run() {
        final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
        if (sceneAnn != null) {
            run(atomAnnotationsProcessor.getGame().getScene(sceneAnn.id()));
            return;
        }
    }

    public abstract void run(Scene scene);

    public void invokeDefault() {
        final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
        try {
            method.setAccessible(true);
            method.invoke(atomAnnotationsProcessor.container.getBean(sceneAnn.id(), "Scene"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
