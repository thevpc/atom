package net.thevpc.gaming.atom.ioc;

import net.thevpc.gaming.atom.annotations.AtomScene;
import net.thevpc.gaming.atom.presentation.Scene;

import java.lang.reflect.Method;

/**
 * Created by vpc on 10/7/16.
 */
abstract class AbstractSceneMethodAction implements PostponedAction {
    private AtomAnnotationsProcessor atomAnnotationsProcessor;
    private final Method method;
    private final Object instance;

    public AbstractSceneMethodAction(AtomAnnotationsProcessor atomAnnotationsProcessor, Method method,Object instance) {
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
        final AtomScene sceneAnn = method.getDeclaringClass().getAnnotation(AtomScene.class);
        if (sceneAnn != null) {
            return atomAnnotationsProcessor.container.contains(sceneAnn.id(), "Scene", null);
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
            //atomAnnotationsProcessor.container.getBean(sceneAnn.id(), "Scene")
            method.invoke(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
