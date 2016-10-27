package net.vpc.gaming.atom.engine;

/**
 * The listener that's notified when game changes (scenes list changes)
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface GameEngineChangeListener {

    /**
     * called when new scene is added
     *
     * @param sceneEngine scene added
     */
    public void sceneAdded(SceneEngine sceneEngine);

    /**
     * called when new scene is removed
     *
     * @param sceneEngine scene removed
     */
    public void sceneRemoved(SceneEngine sceneEngine);

    /**
     * called when active scene changes
     *
     * @param oldEngine old scene sceneEngine
     * @param newEngine new scene sceneEngine
     */
    public void activeSceneChanged(SceneEngine oldEngine, SceneEngine newEngine);
}
