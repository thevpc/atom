/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.SceneEngineModel;
import net.thevpc.gaming.atom.engine.collisiontasks.SceneCollisionManager;

/**
 * SceneEngine updates Callback
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public interface SceneEngineChangeListener {

    /**
     * called when model changes (when invoking <code>SceneEngine.setModel</code>)
     *
     * @param sceneEngine current sceneEngine
     * @param oldValue    old value
     * @param newValue    new value
     */
    void modelChanged(SceneEngine sceneEngine, SceneEngineModel oldValue, SceneEngineModel newValue);

    /**
     * called when fps changes
     *
     * @param sceneEngine
     * @param oldValue    old value
     * @param newValue    new value
     */
    void fpsChanged(SceneEngine sceneEngine, int oldValue, int newValue);

    /**
     * called when collisiontasks manager changes
     *
     * @param sceneEngine
     * @param oldValue    old value
     * @param newValue    new value
     */
    void collisionManagerChanged(SceneEngine sceneEngine, SceneCollisionManager oldValue, SceneCollisionManager newValue);

    void playerFactoryChanged(SceneEngine sceneEngine, PlayerFactory oldValue, PlayerFactory newValue);
}
