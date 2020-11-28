/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.gaming.atom.engine;

import net.thevpc.gaming.atom.model.SceneEngineModel;
import net.thevpc.gaming.atom.engine.collisiontasks.SceneCollisionManager;

/**
 * Adapter for SceneEngineUpdateListener
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class SceneEngineChangeAdapter implements SceneEngineChangeListener {

    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void collisionManagerChanged(SceneEngine sceneEngine, SceneCollisionManager oldValue, SceneCollisionManager newValue) {

    }

    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void fpsChanged(SceneEngine sceneEngine, int oldValue, int newValue) {

    }

    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void modelChanged(SceneEngine sceneEngine, SceneEngineModel oldValue, SceneEngineModel newValue) {

    }


    /**
     * {@inheritDoc}
     * Does nothing
     */
    @Override
    public void playerFactoryChanged(SceneEngine sceneEngine, PlayerFactory oldValue, PlayerFactory newValue) {

    }
}
