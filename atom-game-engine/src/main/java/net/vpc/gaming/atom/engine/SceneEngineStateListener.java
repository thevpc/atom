/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;

/**
 * Scene Engine State Transition Callback
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngineStateListener {

    /**
     * called when SceneEngine is Initializing
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneInitializing(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Initialized
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneInitialized(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Starting
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneStarting(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Started
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneStarted(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Stopping
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneStopping(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Stopped
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneStopped(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Pausing
     *
     * @param sceneEngine current scene sceneEngine
     */
    void scenePausing(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Paused
     *
     * @param sceneEngine current scene sceneEngine
     */
    void scenePaused(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Resuming
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneResuming(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Resumed
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneResumed(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Activating
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneActivating(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Activated
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneActivated(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Deactivating
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneDeactivating(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Deactivated
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneDeactivated(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Disposing
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneDisposing(SceneEngine sceneEngine);

    /**
     * called when SceneEngine is Disposed
     *
     * @param sceneEngine current scene sceneEngine
     */
    void sceneDisposed(SceneEngine sceneEngine);
}
