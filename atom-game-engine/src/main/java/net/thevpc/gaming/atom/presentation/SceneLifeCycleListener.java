package net.thevpc.gaming.atom.presentation;

public interface SceneLifeCycleListener {

    default void sceneInitialized(Scene scene){

    }

    default void sceneStarted(Scene scene){

    }

    default void sceneStopped(Scene scene){

    }

    default void nexFrame(Scene scene){

    }
}
