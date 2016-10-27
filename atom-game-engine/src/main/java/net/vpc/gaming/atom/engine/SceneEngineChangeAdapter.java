/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.gaming.atom.engine;

import net.vpc.gaming.atom.engine.collision.SceneCollisionManager;
import net.vpc.gaming.atom.model.SceneEngineModel;

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
