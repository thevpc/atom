package net.thevpc.gaming.atom.presentation;

/**
 * Created by vpc on 9/23/16.
 */
public interface SceneListener {
    default void sceneStarted(Scene scene) {}

    default void sceneStopped(Scene scene) {}
}
